package org.metabrainz.mobile.presentation.features.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;

import java.util.List;

public class SearchAdapterInstrument extends SearchAdapter {
    private final List<Instrument> data;

    public SearchAdapterInstrument(List<Instrument> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public InstrumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instrument, parent, false);
        return new InstrumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InstrumentViewHolder viewHolder = (InstrumentViewHolder) holder;
        Instrument instrument = data.get(position);
        viewHolder.instrumentName.setText(instrument.getName());
        setViewVisibility(instrument.getDescription(), viewHolder.instrumentDescription);
        setViewVisibility(instrument.getType(), viewHolder.instrumentType);
        setViewVisibility(instrument.getDisambiguation(), viewHolder.instrumentDisambiguation);
        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class InstrumentViewHolder extends EntityViewHolder {
        final TextView instrumentName;
        final TextView instrumentType;
        final TextView instrumentDisambiguation;
        final TextView instrumentDescription;

        InstrumentViewHolder(@NonNull View itemView) {
            super(itemView);
            instrumentName = itemView.findViewById(R.id.instrument_name);
            instrumentDescription = itemView.findViewById(R.id.instrument_description);
            instrumentDisambiguation = itemView.findViewById(R.id.instrument_disambiguation);
            instrumentType = itemView.findViewById(R.id.instrument_type);
        }

    }
}
