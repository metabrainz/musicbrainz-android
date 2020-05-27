package org.metabrainz.mobile.presentation.features.tagger;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.musicbrainz.ktaglib.AudioFile;
import com.musicbrainz.ktaglib.KTagLib;

import org.metabrainz.mobile.R;

import java.io.FileNotFoundException;
import java.util.Objects;

public class TaglibtestActivity extends AppCompatActivity {

    private static final String EXTRA_FILE_PATH = "file_path";
    TextView titleValue,filepathshow,titleKey;
    Button filepicker;
    KTagLib kTagLib=new KTagLib();
    AudioFile metadata=new AudioFile("",0,0,"","","","",0,0,0,0,0,0,"");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taglibtest);
        titleKey=findViewById(R.id.title_key_id);
        titleValue=findViewById(R.id.title_value_id);
        filepathshow=findViewById(R.id.file_path_id);
        filepicker=findViewById(R.id.file_picker_id);

        filepicker.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            startActivityForResult(intent,1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            Uri uri=data.getData();
            try {
                assert uri != null;
                int fd= Objects.requireNonNull(getContentResolver().openFileDescriptor(uri, "r")).getFd();
                String file_path=data.getData().getPath();
                String file_name=getFileName(uri);
                metadata = kTagLib.getAudioFile(fd,file_path,file_name);
                if(metadata!=null) titleValue.setText(metadata.component1());
                else titleValue.setText(file_name);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (Objects.requireNonNull(uri.getScheme()).equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}