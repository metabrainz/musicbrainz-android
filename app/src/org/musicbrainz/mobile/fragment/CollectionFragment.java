package org.musicbrainz.mobile.fragment;

import org.musicbrainz.android.api.data.UserCollection;
import org.musicbrainz.android.api.data.ReleaseInfo;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.ReleaseActivity;
import org.musicbrainz.mobile.adapter.list.ReleaseInfoAdapter;
import org.musicbrainz.mobile.async.CollectionEditLoader;
import org.musicbrainz.mobile.async.CollectionLoader;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.intent.IntentFactory.Extra;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CollectionFragment extends SherlockListFragment {

    private static final String RELEASE_MBID = "releaseMbid";
    private static final int COLLECTION_LOADER = 0;
    private static final int COLLECTION_EDIT_LOADER = 1;

    private Context appContext;
    private String mbid;
    private View loading;
    private View error;
    private FragmentLoadingCallbacks activityCallbacks;

    public interface FragmentLoadingCallbacks {
        public void onLoadStart();
        public void onLoadFinish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appContext = activity.getApplicationContext();
        mbid = activity.getIntent().getStringExtra(Extra.COLLECTION_MBID);
        try {
            activityCallbacks = (FragmentLoadingCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + FragmentLoadingCallbacks.class.getSimpleName());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(COLLECTION_LOADER, savedInstanceState, loaderCallbacks);
        getListView().setOnItemLongClickListener(longPressListener);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_collection, container, false);
        loading = layout.findViewById(R.id.loading);
        error = layout.findViewById(R.id.error);
        return layout;
    }

    private void handleResult(AsyncResult<UserCollection> result) {
        switch (result.getStatus()) {
        case SUCCESS:
            UserCollection collection = result.getData();
            setListAdapter(new ReleaseInfoAdapter(getActivity(), R.layout.list_collection_release,
                    collection.getReleases()));
            break;
        case EXCEPTION:
            showConnectionErrorWarning();
        }
    }

    private void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    private void showConnectionErrorWarning() {
        error.setVisibility(View.VISIBLE);
        Button retry = (Button) error.findViewById(R.id.retry_button);
        retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                getLoaderManager().restartLoader(COLLECTION_LOADER, null, loaderCallbacks);
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ReleaseInfoAdapter adapter = (ReleaseInfoAdapter) getListAdapter();
        String releaseMbid = adapter.getItem(position).getReleaseMbid();
        Intent intent = new Intent(appContext, ReleaseActivity.class);
        intent.putExtra(Extra.RELEASE_MBID, releaseMbid);
        startActivity(intent);
    }

    OnItemLongClickListener longPressListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
            if (getListView().getCheckedItemPosition() == position) {
                return false;
            }
            getListView().setItemChecked(position, true);
            getSherlockActivity().startActionMode(new RemoveReleasesActionMode());
            return true;
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_collection, menu);
    }

    private LoaderCallbacks<AsyncResult<UserCollection>> loaderCallbacks = new LoaderCallbacks<AsyncResult<UserCollection>>() {

        @Override
        public Loader<AsyncResult<UserCollection>> onCreateLoader(int id, Bundle args) {
            return new CollectionLoader(mbid);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<UserCollection>> loader, AsyncResult<UserCollection> data) {
            hideLoading();
            handleResult(data);
            activityCallbacks.onLoadFinish();
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<UserCollection>> loader) {
            loader.reset();
        }
    };

    private LoaderCallbacks<AsyncResult<Void>> editCallbacks = new LoaderCallbacks<AsyncResult<Void>>() {

        @Override
        public Loader<AsyncResult<Void>> onCreateLoader(int id, Bundle args) {
            return new CollectionEditLoader(mbid, args.getString(RELEASE_MBID), false);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<Void>> loader, AsyncResult<Void> data) {
            getLoaderManager().destroyLoader(COLLECTION_EDIT_LOADER);
            switch (data.getStatus()) {
            case EXCEPTION:
                activityCallbacks.onLoadFinish();
                Toast.makeText(appContext, R.string.collection_remove_fail, Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS:
                getLoaderManager().restartLoader(COLLECTION_LOADER, null, loaderCallbacks);
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<Void>> loader) {
            loader.reset();
        }
    };

    private final class RemoveReleasesActionMode implements ActionMode.Callback {

        private int selectedPosition;
        private ReleaseInfo selectedRelease;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_collection, menu);
            selectedPosition = getListView().getCheckedItemPosition();
            selectedRelease = (ReleaseInfo) getListAdapter().getItem(selectedPosition);
            mode.setTitle(selectedRelease.getTitle());
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.menu_delete) {
                activityCallbacks.onLoadStart();
                startDeleteLoader();
                getListView().setItemChecked(selectedPosition, false);
                mode.finish();
                return true;
            }
            return false;
        }

        private void startDeleteLoader() {
            Bundle args = new Bundle();
            args.putString(RELEASE_MBID, selectedRelease.getReleaseMbid());
            getLoaderManager().initLoader(COLLECTION_EDIT_LOADER, args, editCallbacks);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            getListView().setItemChecked(selectedPosition, false);
        }

    }

}
