package org.metabrainz.mobile.presentation.features.label;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.databinding.FragmentLabelInfoBinding;
import org.metabrainz.mobile.util.Resource;

public class LabelInfoFragment extends Fragment {

    private FragmentLabelInfoBinding binding;
    private LabelViewModel labelViewModel;

    public static LabelInfoFragment newInstance() {
        return new LabelInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLabelInfoBinding.inflate(inflater, container, false);
        labelViewModel = new ViewModelProvider(requireActivity()).get(LabelViewModel.class);
        labelViewModel.getData().observe(getViewLifecycleOwner(), this::setLabelInfo);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setLabelInfo(Resource<Label> resource) {
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            Label label = resource.getData();
            String type, founded, area, code;

            type = label.getType();
            code = label.getCode();

            if (label.getLifeSpan() != null && label.getLifeSpan().getBegin() != null &&
                    !label.getLifeSpan().getBegin().isEmpty())
                founded = label.getLifeSpan().getBegin();
            else founded = "";
            if (label.getArea() != null) area = label.getArea().getName();
            else area = "";

            if (type != null && !type.isEmpty())
                binding.labelType.setText(type);
            if (founded != null && !founded.isEmpty())
                binding.labelFounded.setText(founded);
            if (area != null && !area.isEmpty())
                binding.labelArea.setText(area);
            if (code != null && !code.isEmpty())
                binding.labelCode.setText(code);
        }
    }

}
