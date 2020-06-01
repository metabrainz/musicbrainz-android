
package org.metabrainz.mobile.presentation.features.tagger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.musicbrainz.ktaglib.AudioFile
import com.musicbrainz.ktaglib.KTagLib
import org.metabrainz.mobile.R
import java.io.FileNotFoundException
import java.util.*

class TaglibtestActivity : AppCompatActivity() {
    var titleValue: TextView? = null
    var filepathshow: TextView? = null
    var titleKey: TextView? = null
    var kTagLib = KTagLib()
    var metadata: AudioFile? = AudioFile()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.taglibtest)

        titleKey = findViewById(R.id.title_key_id)
        titleValue = findViewById(R.id.title_value_id)
        filepathshow = findViewById(R.id.file_path_id)

        findViewById<Button>(R.id.file_picker_id).setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "audio/*"
            startActivityForResult(intent, 1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("Taglib", "OnActivityResult Called")
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Log.i("Taglib", "Result_OK")
            val uri = data?.data
            try {
                assert(uri != null)
                val fd = uri?.let { contentResolver.openFileDescriptor(it, "r") }
                Log.i("Taglib", "fd fetched")
                val file_path = data?.data!!.path
                val file_name = getFileName(uri)
                if (file_path != null && file_name != null) Log.i("Taglib", "File Fetched")
                Log.i("Taglib", "Fetching metadata")
                metadata = kTagLib.getAudioFile(fd.toString().toInt(), file_path!!, file_name!!)
                if (metadata != null) {
                    Log.i("Taglib", "metadata fetched")
                } else titleValue!!.text = file_name
            } catch (e: FileNotFoundException) {
                Log.i("Taglib", "Metadata extraction failed")
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getFileName(uri: Uri?): String? {
        var result: String? = null
        if (Objects.requireNonNull(uri!!.scheme) == "content") {
            contentResolver.query(uri, null, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }
        return result
    }

    companion object {
        private const val EXTRA_FILE_PATH = "file_path"
    }
}
