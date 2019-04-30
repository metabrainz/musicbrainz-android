package org.metabrainz.mobile.presentation.features.label;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;

public class LabelInfoFragment extends Fragment {

    private LabelViewModel labelViewModel;
    private TextView labelType, labelFounded, labelArea, labelCode;

    public static LabelInfoFragment newInstance() {
        return new LabelInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_label_info, container, false);
        labelViewModel = ViewModelProviders.of(getActivity()).get(LabelViewModel.class);
        labelViewModel.initializeLabelData().observe(getViewLifecycleOwner(), this::setLabelInfo);
        findViews(layout);
        return layout;
    }

    private void findViews(View layout) {
        labelType = layout.findViewById(R.id.label_type);
        labelFounded = layout.findViewById(R.id.label_founded);
        labelArea = layout.findViewById(R.id.label_area);
        labelCode = layout.findViewById(R.id.label_code);
    }

    private void setLabelInfo(Label label) {
        String type, founded, area, code;

        if (label != null) {
            type = label.getType();
            code = label.getCode();

            if (label.getLifeSpan() != null && label.getLifeSpan().getBegin() != null &&
                    !label.getLifeSpan().getBegin().isEmpty())
                founded = label.getLifeSpan().getBegin();
            else founded = "";
            if (label.getArea() != null) area = label.getArea().getName();
            else area = "";

            if (type != null && !type.isEmpty())
                labelType.setText(type);
            if (founded != null && !founded.isEmpty())
                labelFounded.setText(founded);
            if (area != null && !area.isEmpty())
                labelArea.setText(area);
            if (code != null && !code.isEmpty())
                labelCode.setText(code);
        }
    }
}
