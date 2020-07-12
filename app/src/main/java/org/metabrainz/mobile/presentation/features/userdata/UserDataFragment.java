package org.metabrainz.mobile.presentation.features.userdata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.userdata.Tag;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserTag;
import org.metabrainz.mobile.databinding.FragmentUserDataBinding;

import java.util.ArrayList;
import java.util.List;

public class UserDataFragment extends Fragment {

    private FragmentUserDataBinding binding;

    private TagAdapter tagsAdapter;
    private UserTagAdapter userTagsAdapter;
    private final List<Tag> tags = new ArrayList<>();
    private final List<UserTag> userTags = new ArrayList<>();
    private UserViewModel viewModel;

    public static UserDataFragment newInstance() {
        return new UserDataFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagsAdapter = new TagAdapter(tags);
        userTagsAdapter = new UserTagAdapter(userTags);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserDataBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        viewModel.getUserData().observe(getViewLifecycleOwner(), this::updateData);
        bindViews();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void bindViews() {
        binding.tagsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.tagsList.setAdapter(tagsAdapter);

        binding.userTagsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.userTagsList.setAdapter(userTagsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL);
        binding.tagsList.addItemDecoration(dividerItemDecoration);
        binding.userTagsList.addItemDecoration(dividerItemDecoration);

        binding.noRating.setVisibility(View.GONE);
        binding.noUserRating.setVisibility(View.GONE);
        binding.noUserTag.setVisibility(View.GONE);
        binding.noTag.setVisibility(View.GONE);
    }

    private void displayRating(MBEntity entity) {
        if (entity != null && entity.getRating() != null && entity.getRating().getValue() != 0)
            binding.rating.setRating(entity.getRating().getValue());
        else {
            binding.noRating.setVisibility(View.VISIBLE);
            binding.rating.setVisibility(View.GONE);
        }
      
        if (entity != null && entity.getUserRating() != null && entity.getUserRating().getValue() != 0)
            binding.userRating.setRating(entity.getUserRating().getValue());
        else {
            binding.noUserRating.setVisibility(View.VISIBLE);
            binding.userRating.setVisibility(View.GONE);
        }
    }

    private void addTags(MBEntity entity) {
        if (entity != null && entity.getTags() != null) {
            tags.clear();
            tags.addAll(entity.getTags());
            tagsAdapter.notifyDataSetChanged();

            if (tags.size() == 0) {
                binding.noTag.setVisibility(View.VISIBLE);
                binding.tagsList.setVisibility(View.GONE);
            }

        } else {
            binding.noTag.setVisibility(View.VISIBLE);
            binding.tagsList.setVisibility(View.GONE);
        }

        if (entity != null && entity.getUserTags() != null) {
            userTags.clear();
            userTags.addAll(entity.getUserTags());
            userTagsAdapter.notifyDataSetChanged();

            if (userTags.size() == 0) {
                binding.noUserTag.setVisibility(View.VISIBLE);
                binding.userTagsList.setVisibility(View.GONE);
            }
        } else {
            binding.noUserTag.setVisibility(View.VISIBLE);
            binding.userTagsList.setVisibility(View.GONE);
        }
    }

    private void updateData(MBEntity entity) {
        addTags(entity);
        displayRating(entity);
    }

}
