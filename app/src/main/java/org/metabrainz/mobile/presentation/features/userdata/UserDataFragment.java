package org.metabrainz.mobile.presentation.features.userdata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.userdata.Tag;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserTag;

import java.util.ArrayList;
import java.util.List;

public class UserDataFragment extends Fragment {

    private RecyclerView tagsList, userTagsList;
    private RatingBar ratingBar, userRatingBar;
    private TagAdapter tagsAdapter;
    private UserTagAdapter userTagsAdapter;
    private List<Tag> tags = new ArrayList<>();
    private List<UserTag> userTags = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagsAdapter = new TagAdapter(tags);
        userTagsAdapter = new UserTagAdapter(userTags);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_data_fragment, container, false);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        tagsList = view.findViewById(R.id.tags_list);
        tagsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        tagsList.setAdapter(tagsAdapter);

        userTagsList = view.findViewById(R.id.user_tags_list);
        userTagsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        userTagsList.setAdapter(userTagsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        tagsList.addItemDecoration(dividerItemDecoration);
        userTagsList.addItemDecoration(dividerItemDecoration);

        ratingBar = view.findViewById(R.id.rating);
        userRatingBar = view.findViewById(R.id.user_rating);
    }

    private void displayRating(MBEntity entity) {
        if (entity != null && entity.getRating() != null)
            ratingBar.setRating(entity.getRating().getValue());

        if (entity != null && entity.getUserRating() != null)
            userRatingBar.setRating(entity.getUserRating().getValue());
    }

    private void addTags(MBEntity entity) {
        if (entity != null && entity.getTags() != null) {
            tags.clear();
            tags.addAll(entity.getTags());
            tagsAdapter.notifyDataSetChanged();
        }

        if (entity != null && entity.getUserTags() != null) {
            userTags.clear();
            userTags.addAll(entity.getUserTags());
            userTagsAdapter.notifyDataSetChanged();
        }
    }

    protected void updateData(MBEntity entity) {
        addTags(entity);
        displayRating(entity);
    }

}
