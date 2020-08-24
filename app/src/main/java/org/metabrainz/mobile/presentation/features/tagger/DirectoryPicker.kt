package org.metabrainz.mobile.presentation.features.tagger

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.simplecityapps.ktaglib.AudioFile
import com.simplecityapps.ktaglib.KTagLib
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentDirectoryPickerBinding


class DirectoryPicker : Fragment(),OnItemCLickListener{


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("MainActivity", "Coroutine failed: ${throwable.localizedMessage}")
    }

    val Fragment.packageManager get() = activity?.packageManager
    val Fragment.contentResolver get() = activity?.contentResolver

    private val scope = CoroutineScope(Dispatchers.Main + exceptionHandler)

    private val kTagLib = KTagLib()
    private lateinit var documentAdapter: DocumentAdapter

    private lateinit var viewmodel:KotlinTaggerViewModel
    private lateinit var binding: FragmentDirectoryPickerBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentDirectoryPickerBinding.inflate(inflater)
        viewmodel = activity?.run{
            ViewModelProvider(this).get(KotlinTaggerViewModel::class.java)
        }!!
        documentAdapter = DocumentAdapter(this)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = documentAdapter
        recyclerView.addItemDecoration(SpacesItemDecoration(8, true))
        binding.instructionForTagFix.visibility = View.INVISIBLE

        binding.chooseDirectoryButton.setOnClickListener {
            binding.instructionForTagFix.visibility = View.VISIBLE
            binding.instruction.visibility = View.GONE
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            if (packageManager?.let { it1 -> intent.resolveActivity(it1) } != null) {
                startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT)
            } else {
                Toast.makeText(context,"Dcument provider not found",Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onItemClicked(metadata: AudioFile?) {
        viewmodel.setTaglibFetchedMetadata(metadata)
        findNavController().navigate(R.id.action_directoryPicker_to_taggerFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                intent.data?.let { uri ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        contentResolver?.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    scope.launch {
                        documentAdapter.clear()
                        val documents = parseUri(uri)
                        getTags(documents).collect { pair ->
                            documentAdapter.addItem(pair as Pair<AudioFile, Document>)
                        }
                    }
                } ?: Log.e(TAG, "Intent uri null")
            } ?: Log.e(TAG, "onActivityResult failed to handle result: Intent data null")
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }


    // Private

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private suspend fun parseUri(uri: Uri): List<Document> {
        return withContext(Dispatchers.IO) {
            val childDocumentsUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri))
            traverse(uri, childDocumentsUri, mutableListOf())
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun traverse(treeUri: Uri, documentUri: Uri, documents: MutableList<Document> = mutableListOf()): List<Document> {
        contentResolver?.query(
                documentUri,
                arrayOf(
                        DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                        DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                        DocumentsContract.Document.COLUMN_MIME_TYPE
                ),
                null,
                null,
                null
        ).use { cursor ->
            cursor?.let {
                while (cursor.moveToNext()) {
                    val documentId = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
                    val displayName = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                    val mimeType = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))
                    val childDocumentUri = DocumentsContract.buildDocumentUriUsingTree(documentUri, documentId)
                    if (mimeType == DocumentsContract.Document.MIME_TYPE_DIR) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            traverse(treeUri, DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, documentId), documents)
                        }
                    } else {
                        if (mimeType.startsWith("audio") ||
                                arrayOf("mp3", "3gp", "m4a", "m4b", "aac", "ts", "flac", "mid", "xmf", "mxmf", "midi", "rtttl", "rtx", "ota", "imy", "ogg", "mkv", "wav", "opus")
                                        .contains(displayName.substringAfterLast('.'))
                        ) {
                            documents.add(Document(childDocumentUri, documentId, displayName, mimeType))
                        }
                    }
                }
            }
        } ?: Log.e(TAG, "Failed to iterate cursor (null)")

        return documents
    }

    private fun getTags(documents: List<Document>): Flow<Pair<AudioFile?, Document>> {
        return flow {
            documents.forEach { document ->
                contentResolver?.openFileDescriptor(document.uri, "r")?.use { pfd ->
                    try {
                        emit(Pair(kTagLib.getAudioFile(pfd.fd, document.uri.toString(), document.displayName.substringBeforeLast(".")), document))
                    } catch (e: IllegalStateException) {
                        Log.e("MainActivity", "Failed to get audio file: ", e)
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun setBitmapForAlbumArt(byteArray: ByteArray):Bitmap{
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return bmp
    }


    // Static

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_CODE_OPEN_DOCUMENT = 100
    }
}
