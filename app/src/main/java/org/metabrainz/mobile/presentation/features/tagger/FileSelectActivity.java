package org.metabrainz.mobile.presentation.features.tagger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.UserPreferences;
import org.metabrainz.mobile.util.FileEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSelectActivity extends AppCompatActivity {


    public static final String EXTRA_FILE_PATH = "file_path";
    public static final int ACTION_SELECT_AUDIO_FILE = 1;
    public static final int ACTION_SELECT_DIRECTORY = 2;
    public static final String FILE_SELECT_TYPE = "select_type";
    private static final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().toString();
    private static final String ROOT_PATH = ROOT_DIRECTORY.substring(0, ROOT_DIRECTORY.lastIndexOf('/'));

    private int requestType;
    private List<FileEntry> fileEntries;
    private FileSelectAdapter adapter;
    private RecyclerView recyclerView;
    private Button buttonSelect, buttonUp;
    private String currentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        fileEntries = new ArrayList<>();
        adapter = new FileSelectAdapter(fileEntries);

        buttonSelect = findViewById(R.id.button_select);
        buttonUp = findViewById(R.id.button_up);

        recyclerView = findViewById(R.id.file_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        requestType = getIntent().getIntExtra(FILE_SELECT_TYPE, ACTION_SELECT_AUDIO_FILE);
        if (requestType == ACTION_SELECT_AUDIO_FILE) {
            buttonSelect.setVisibility(View.GONE);
            buttonUp.setVisibility(View.GONE);
            adapter.setFileClickAction(this::selectFile);
        } else if (requestType == ACTION_SELECT_DIRECTORY) {
            buttonSelect.setOnClickListener(v -> selectDirectory());
            buttonUp.setOnClickListener(v -> goToParentDirectory());
            adapter.setFileClickAction(this::chooseDirectory);
        }

        recyclerView.setAdapter(adapter);
        displayFileList(UserPreferences.getTaggerDirectoryPreference());
    }

    private void displayFileList(String path) {
        File directory = new File(path);
        if (!directory.exists()) directory.mkdir();

        fileEntries.clear();
        currentPath = path;
        ((TextView) (findViewById(R.id.directory_path))).setText(path);

        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                int index = f.getAbsolutePath().lastIndexOf('.');
                String extension = f.getAbsolutePath().substring(index + 1);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                boolean isAudio = mimeType != null && mimeType.startsWith("audio") &&
                        requestType == ACTION_SELECT_AUDIO_FILE;
                boolean isDirectory = f.isDirectory() && requestType == ACTION_SELECT_DIRECTORY;
                if (isAudio || isDirectory) {
                    FileEntry entry = new FileEntry();
                    entry.setName(f.getName());
                    entry.setPath(f.getAbsolutePath());
                    fileEntries.add(entry);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void selectFile(FileEntry fileEntry) {
        Toast.makeText(this, fileEntry.getName() + " is selected", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FILE_PATH, fileEntry.getPath());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void selectDirectory() {
        FileEntry entry = new FileEntry();
        entry.setName(new File(currentPath).getName());
        entry.setPath(currentPath);
        selectFile(entry);
    }

    private void goToParentDirectory() {
        File file = new File(currentPath);
        if (file.isDirectory() && file.getParentFile() != null) {
            if (checkHasParentDirectory(file.getParentFile().getAbsolutePath()))
                buttonUp.setEnabled(true);
            displayFileList(file.getParentFile().getAbsolutePath());
        }
    }

    private boolean checkHasParentDirectory(String checkPath) {
        File file = new File(checkPath);
        if (file.isDirectory() && file.getParentFile() != null) {
            String path = file.getParentFile().getAbsolutePath();
            // Restrict app to go to external storage at most
            if (!path.equalsIgnoreCase(ROOT_PATH)) return true;
            buttonUp.setEnabled(false);
        }
        return false;
    }

    private void chooseDirectory(FileEntry fileEntry) {
        File file = new File(fileEntry.getPath());
        if (checkHasParentDirectory(fileEntry.getPath()))
            buttonUp.setEnabled(true);
        if (file.isDirectory()) displayFileList(fileEntry.getPath());
    }
}
