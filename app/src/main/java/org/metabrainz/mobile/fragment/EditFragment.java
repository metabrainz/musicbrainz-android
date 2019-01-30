package org.metabrainz.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.Tag;
import org.metabrainz.mobile.api.data.UserData;
import org.metabrainz.mobile.api.webservice.Entity;
import org.metabrainz.mobile.async.SubmitRatingLoader;
import org.metabrainz.mobile.async.SubmitTagsLoader;
import org.metabrainz.mobile.async.result.AsyncResult;
import org.metabrainz.mobile.fragment.contracts.ContractFragment;
import org.metabrainz.mobile.fragment.contracts.EntityTab;
import org.metabrainz.mobile.string.StringFormat;

import java.util.List;

public class EditFragment extends ContractFragment<EditFragment.Callback> implements OnClickListener, EntityTab<Object> {

    private static final int RATING_LOADER = 10;
    private static final int TAG_LOADER = 11;

    private RatingBar ratingInput;
    private EditText tagInput;
    private ImageButton tagBtn;

    private boolean doingTag, doingRate;
    private LoaderCallbacks<AsyncResult<List<Tag>>> tagSubmissionCallbacks = new LoaderCallbacks<AsyncResult<List<Tag>>>() {

        @Override
        public Loader<AsyncResult<List<Tag>>> onCreateLoader(int id, Bundle args) {
            String tags = tagInput.getText().toString();
            Entity type = (Entity) getArguments().get("type");
            return new SubmitTagsLoader(type, getContract().getMbid(), tags);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<List<Tag>>> loader, AsyncResult<List<Tag>> data) {
            getLoaderManager().destroyLoader(TAG_LOADER);
            onFinishedTagging();
            switch (data.getStatus()) {
                case EXCEPTION:
                    Toast.makeText(App.getContext(), R.string.toast_tag_fail, Toast.LENGTH_LONG).show();
                    break;
                case SUCCESS:
                    Toast.makeText(App.getContext(), R.string.toast_tag, Toast.LENGTH_SHORT).show();
                    getContract().updateTags(data.getData());
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<List<Tag>>> loader) {
            loader.reset();
        }
    };
    private LoaderCallbacks<AsyncResult<Float>> ratingSubmissionCallbacks = new LoaderCallbacks<AsyncResult<Float>>() {

        @Override
        public Loader<AsyncResult<Float>> onCreateLoader(int id, Bundle args) {
            int rating = (int) ratingInput.getRating();
            Entity type = (Entity) getArguments().get("type");
            return new SubmitRatingLoader(type, getContract().getMbid(), rating);
        }

        @Override
        public void onLoadFinished(Loader<AsyncResult<Float>> loader, AsyncResult<Float> data) {
            getLoaderManager().destroyLoader(RATING_LOADER);
            onFinishedRating();
            switch (data.getStatus()) {
                case EXCEPTION:
                    Toast.makeText(App.getContext(), R.string.toast_rate_fail, Toast.LENGTH_LONG).show();
                    break;
                case SUCCESS:
                    Toast.makeText(App.getContext(), R.string.toast_rate, Toast.LENGTH_SHORT).show();
                    getContract().updateRating(data.getData());
            }
        }

        @Override
        public void onLoaderReset(Loader<AsyncResult<Float>> loader) {
            loader.reset();
        }
    };
    private OnRatingBarChangeListener ratingListener = new OnRatingBarChangeListener() {
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if (fromUser) {
                doingRate = true;
                updateProgress();
                ratingInput.setEnabled(false);
                getLoaderManager().initLoader(RATING_LOADER, null, ratingSubmissionCallbacks);
            }
        }
    };

    public static EditFragment newInstance(Entity type) {
        EditFragment f = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable("type", type);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_edit, container, false);
        findViews(layout);
        setListeners();
        return layout;
    }

    private void findViews(View layout) {
        tagInput = (EditText) layout.findViewById(R.id.tag_input);
        ratingInput = (RatingBar) layout.findViewById(R.id.rating_input);
        tagBtn = (ImageButton) layout.findViewById(R.id.tag_btn);
    }

    private void setListeners() {
        ratingInput.setOnRatingBarChangeListener(ratingListener);
        tagBtn.setOnClickListener(this);
    }

    @Override
    public void update(Object entity) {
        if (App.isUserLoggedIn()) {
            UserData userData = getContract().getUserData();
            tagInput.setText(StringFormat.commaSeparate(userData.getTags()));
            ratingInput.setRating(userData.getRating());
        } else {
            disableEditViews();
            getView().findViewById(R.id.login_warning).setVisibility(View.VISIBLE);
        }
    }

    private void disableEditViews() {
        ratingInput.setEnabled(false);
        ratingInput.setFocusable(false);
        tagInput.setEnabled(false);
        tagInput.setFocusable(false);
        tagBtn.setEnabled(false);
        tagBtn.setFocusable(false);
    }

    private void updateProgress() {
        if (doingTag || doingRate) {
            getContract().showLoading();
        } else {
            getContract().hideLoading();
        }
    }

    @Override
    public void onClick(View v) {
        String tagString = tagInput.getText().toString();
        if (tagString.length() == 0) {
            Toast.makeText(App.getContext(), R.string.toast_tag_err, Toast.LENGTH_SHORT).show();
        } else {
            doingTag = true;
            updateProgress();
            tagBtn.setEnabled(false);
            getLoaderManager().initLoader(TAG_LOADER, null, tagSubmissionCallbacks);
        }

    }

    private void onFinishedTagging() {
        doingTag = false;
        updateProgress();
        tagBtn.setEnabled(true);
    }

    private void onFinishedRating() {
        doingRate = false;
        updateProgress();
        ratingInput.setEnabled(true);
    }

    public interface Callback {
        void showLoading();

        void hideLoading();

        String getMbid();

        UserData getUserData();

        void updateTags(List<Tag> tags);

        void updateRating(Float rating);
    }

}
