package org.metabrainz.mobile.presentation.features.tagger;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.util.MetadataChange;

import java.util.List;

public class MetadataChangesAdapter extends RecyclerView.Adapter
        <MetadataChangesAdapter.MetadataChangeViewHolder> {

    private final List<MetadataChange> changes;

    public MetadataChangesAdapter(List<MetadataChange> changes) {
        this.changes = changes;
        setHasStableIds(true);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MetadataChangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new MetadataChangeViewHolder(inflater
                .inflate(R.layout.item_metadata_change, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MetadataChangeViewHolder holder, int position) {
        holder.bind(changes.get(position));
    }

    @Override
    public int getItemCount() {
        return changes.size();
    }

    class MetadataChangeViewHolder extends RecyclerView.ViewHolder {
        private final TextView tagNameView;
        private final TextView originalValueView;
        private final EditText newValueView;
        private final CheckBox saveCheckBox;
        private TextWatcher watcher;

        public MetadataChangeViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNameView = itemView.findViewById(R.id.tag_name);
            originalValueView = itemView.findViewById(R.id.original_value);
            newValueView = itemView.findViewById(R.id.new_value);
            saveCheckBox = itemView.findViewById(R.id.save_change);
        }

        public void bind(MetadataChange change) {

            if (!change.isDiscardChange()) {
                itemView.setAlpha(1.0f);
                saveCheckBox.setChecked(true);
                newValueView.setEnabled(true);
            } else {
                itemView.setAlpha(0.5f);
                saveCheckBox.setChecked(false);
                newValueView.setEnabled(false);
            }

            if (watcher != null) newValueView.removeTextChangedListener(watcher);

            watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    change.setNewValue(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            };

            if (change.getOriginalValue().isEmpty())
                originalValueView.setVisibility(View.GONE);

            if (!change.isChanged()) {
                originalValueView.setBackgroundColor(originalValueView.getContext()
                        .getResources().getColor(android.R.color.background_light));
                newValueView.setVisibility(View.GONE);
                saveCheckBox.setEnabled(false);
            } else {
                originalValueView.setBackgroundColor(originalValueView.getContext()
                        .getResources().getColor(R.color.red));
                newValueView.setVisibility(View.VISIBLE);
                newValueView.setVisibility(newValueView.getContext()
                        .getResources().getColor(R.color.green));
                saveCheckBox.setEnabled(true);
                saveCheckBox.setOnClickListener(v -> {
                    boolean value = saveCheckBox.isChecked();
                    change.setDiscardChange(!value);
                    if (!value) itemView.setAlpha(0.5f);
                    else itemView.setAlpha(1.0f);
                    newValueView.setEnabled(value);
                });
            }


            tagNameView.setText(change.getTagName().toString());
            originalValueView.setText(change.getOriginalValue());
            newValueView.setText(change.getNewValue());
            newValueView.setHint(change.getNewValue());
            newValueView.addTextChangedListener(watcher);
        }

    }
}
