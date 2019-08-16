package org.metabrainz.mobile.presentation.features.tagger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.util.FileEntry;

import java.util.List;

public class FileSelectAdapter extends RecyclerView.Adapter<FileSelectAdapter.FileViewHolder> {

    private List<FileEntry> fileEntries;
    private OnFileClickAction fileClickAction;

    public FileSelectAdapter(List<FileEntry> fileEntries, OnFileClickAction fileClickAction) {
        this.fileEntries = fileEntries;
        this.fileClickAction = fileClickAction;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FileViewHolder(inflater.inflate(R.layout.item_file, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.bind(fileEntries.get(position), fileClickAction);
    }

    @Override
    public int getItemCount() {
        return fileEntries.size();
    }

    interface OnFileClickAction {
        void onClick(FileEntry fileEntry);
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView fileNameView;
        private FileEntry file;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameView = itemView.findViewById(R.id.file_name);
        }

        public void bind(FileEntry fileEntry, OnFileClickAction fileClickAction) {
            this.file = fileEntry;
            fileNameView.setText(fileEntry.getName());
            itemView.setOnClickListener(v -> fileClickAction.onClick(file));
        }
    }
}
