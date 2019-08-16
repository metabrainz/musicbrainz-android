package org.metabrainz.mobile.presentation.features.tagger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.util.FileEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.metabrainz.mobile.App.EXTRA_FILE_PATH;
import static org.metabrainz.mobile.App.TAGGER_ROOT_DIRECTORY;

public class FileSelectActivity extends AppCompatActivity {

    private List<FileEntry> fileEntries;
    private FileSelectAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        fileEntries = new ArrayList<>();
        adapter = new FileSelectAdapter(fileEntries, this::selectFile);
        recyclerView = findViewById(R.id.file_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        displayFileList();
        ((TextView) (findViewById(R.id.directory_path))).setText("/Internal Storage/Picard");
    }

    private void displayFileList() {
        File file = new File(TAGGER_ROOT_DIRECTORY);
        if (!file.exists()) file.mkdir();
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                FileEntry entry = new FileEntry();
                entry.setName(f.getName());
                entry.setPath(f.getAbsolutePath());
                fileEntries.add(entry);
            }
            adapter.notifyDataSetChanged();
        }
        /*
        Uri uri = Uri.fromFile(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ContentResolver contentResolver = getContentResolver();
            Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                    DocumentsContract.getTreeDocumentId(uri));
            Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri,
                    DocumentsContract.getTreeDocumentId(uri));

            Cursor childCursor = contentResolver.query(childrenUri, new String[]{
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME, DocumentsContract.Document.COLUMN_MIME_TYPE},
                    null, null, null);

            try {
                fileEntries.clear();
                while (childCursor.moveToNext()){
                    FileEntry entry = new FileEntry();
                    entry.setName(childCursor.getString(0));
                    fileEntries.add(entry);
                }
                adapter.notifyDataSetChanged();
            }catch (Exception e){
                Log.e(e.getMessage());
            }finally {
                childCursor.close();
            }
        */

    }

    private void selectFile(FileEntry fileEntry) {
        Toast.makeText(this, fileEntry.getName() + " is selected", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FILE_PATH, fileEntry.getPath());
        setResult(RESULT_OK, intent);
        finish();
    }
}
