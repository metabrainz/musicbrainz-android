package org.metabrainz.mobile.presentation.features.userdata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

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

    private TextView noRating, noUserRating, noTag, noUserTag;
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
        View view = inflater.inflate(R.layout.fragment_user_data, container, false);
        bindViews(view);
        return view;
    }

    protected void bindViews(View view) {
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

        noRating = view.findViewById(R.id.no_rating);
        noUserRating = view.findViewById(R.id.no_user_rating);
        noTag = view.findViewById(R.id.no_tag);
        noUserTag = view.findViewById(R.id.no_user_tag);

        noRating.setVisibility(View.GONE);
        noUserRating.setVisibility(View.GONE);
        noUserTag.setVisibility(View.GONE);
        noTag.setVisibility(View.GONE);
    }

    private void displayRating(MBEntity entity) {
        if (entity != null && entity.getRating() != null && entity.getRating().getValue() != 0)
            ratingBar.setRating(entity.getRating().getValue());
        else {
            noRating.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.GONE);
        }


        if (entity != null && entity.getUserRating() != null && entity.getUserRating().getValue() != 0)
            userRatingBar.setRating(entity.getUserRating().getValue());
        else {
            noUserRating.setVisibility(View.VISIBLE);
            userRatingBar.setVisibility(View.GONE);
        }
    }

    private void addTags(MBEntity entity) {
        if (entity != null && entity.getTags() != null) {
            tags.clear();
            tags.addAll(entity.getTags());
            tagsAdapter.notifyDataSetChanged();
            if (tags.size() == 0) {
                noTag.setVisibility(View.VISIBLE);
                tagsList.setVisibility(View.GONE);
            }
        } else {
            noTag.setVisibility(View.VISIBLE);
            tagsList.setVisibility(View.GONE);
        }

        if (entity != null && entity.getUserTags() != null) {
            userTags.clear();
            userTags.addAll(entity.getUserTags());
            userTagsAdapter.notifyDataSetChanged();

            if (userTags.size() == 0) {
                noUserTag.setVisibility(View.VISIBLE);
                userTagsList.setVisibility(View.GONE);
            }
        } else {
            noUserTag.setVisibility(View.VISIBLE);
            userTagsList.setVisibility(View.GONE);
        }
    }

    protected void updateData(MBEntity entity) {
        addTags(entity);
        displayRating(entity);
    }

}
