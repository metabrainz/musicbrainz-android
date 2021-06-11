package org.metabrainz.mobile.presentation.features.tagger

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.simplecityapps.ktaglib.KTagLib
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.metabrainz.mobile.App
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentDirectoryPickerBinding

@AndroidEntryPoint
class DirectoryPickerFragment : Fragment(), OnItemCLickListener, SearchView.OnQueryTextListener {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("DirectoryPicker", "Coroutine failed: ${throwable.localizedMessage}")
    }

    private val Fragment.contentResolver get() = activity?.contentResolver

    private val scope = CoroutineScope(Dispatchers.Main + exceptionHandler)

    private lateinit var documentAdapter: DocumentAdapter
    private var dataFiles: MutableList<Pair<AudioFile, Document>> = mutableListOf()
    val copyDataFiles : MutableList<Pair<AudioFile, Document>> = mutableListOf()

    private val viewmodel: TaggerViewModel by activityViewModels()
    private lateinit var binding: FragmentDirectoryPickerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDirectoryPickerBinding.inflate(inflater)
        documentAdapter = DocumentAdapter(dataFiles,this)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = documentAdapter
        recyclerView.addItemDecoration(SpacesItemDecoration(8, true))
        binding.instructionForTagFix.visibility = View.INVISIBLE

        binding.chooseDirectoryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            try {
                startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT)
            }
            catch (ex: Exception){
                Toast.makeText(context, "Something went wrong! We could not select the directory", Toast.LENGTH_SHORT).show()
            }
        }
        setupSearchView()

        if(copyDataFiles.isNotEmpty()){
           hideInstructions()
        }
        return binding.root
    }

    override fun onItemClicked(audioFile: AudioFile?,uri: Uri?) {
        viewmodel.setTaglibFetchedMetadata(audioFile)
        uri?.let { viewmodel.setURI(it) }
        if(App.context!!.isOnline)
            findNavController().navigate(R.id.action_directoryPicker_to_taggerFragment2)
        else{
            Toast.makeText(requireContext(),"Connect to Internet and Try Again",Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                intent.data?.let { uri ->
                    contentResolver?.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    scope.launch {
                        documentAdapter.clear()
                        val documents = parseUri(uri)
                        if(documents.isNotEmpty()){
                            hideInstructions()
                        }
                        else{
                            showInstructions()
                            Toast.makeText(context,"This folder does not have any supported media files!",Toast.LENGTH_LONG).show()
                        }
                        getTags(documents).collect { pair ->
                            dataFiles.add(pair as Pair<AudioFile, Document>)
                            documentAdapter.notifyItemChanged(dataFiles.size-1)
                        }
                        copyDataFiles.addAll(dataFiles)
                    }
                } ?: Log.e(TAG, "Intent uri null")
            } ?: Log.e(TAG, "onActivityResult failed to handle result: Intent data null")
        }
    }

    private fun showInstructions(){
        binding.instructionForTagFix.visibility = View.GONE
        binding.instruction.visibility = View.VISIBLE
        binding.loadingAnimation.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.searchView.visibility = View.GONE
    }

    private fun hideInstructions(){
        binding.instructionForTagFix.visibility = View.VISIBLE
        binding.instruction.visibility = View.GONE
        binding.loadingAnimation.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.searchView.visibility = View.VISIBLE
    }

    private fun setupSearchView() {
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        binding.searchView.isSubmitButtonEnabled = true
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        startSearch(newText)
        return false
    }

    private fun startSearch(query: String) {
        dataFiles.clear()
        documentAdapter.notifyDataSetChanged()
        if (query.isNotEmpty()) {
            for((audioFile, document) in copyDataFiles) {
                if(document.displayName.contains(query,ignoreCase = true)){
                    dataFiles.add(Pair(audioFile,document))
                    documentAdapter.notifyItemChanged(dataFiles.size-1)
                }
                else if(audioFile.album!=null){
                    if(audioFile.album.contains(query,ignoreCase = true)){
                        dataFiles.add(Pair(audioFile,document))
                        documentAdapter.notifyItemChanged(dataFiles.size-1)
                    }
                    else if(audioFile.artist!=null){
                        if(audioFile.artist.contains(query,ignoreCase = true)){
                            dataFiles.add(Pair(audioFile,document))
                            documentAdapter.notifyItemChanged(dataFiles.size-1)
                        }
                        else if(audioFile.albumArtist!=null){
                            if(audioFile.albumArtist.contains(query,ignoreCase = true)){
                                dataFiles.add(Pair(audioFile,document))
                                documentAdapter.notifyItemChanged(dataFiles.size-1)
                            }
                        }
                    }
                }
            }
        }
        else{
            dataFiles.addAll(copyDataFiles)
            documentAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private suspend fun parseUri(uri: Uri): List<Document> {
        return withContext(Dispatchers.IO) {
            val childDocumentsUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri))
            traverse(uri, childDocumentsUri, mutableListOf())
        }
    }

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
                        val metadata = KTagLib.getMetadata(pfd.detachFd())
                        emit(Pair(AudioFile.getAudioFileFromHashMap(
                                document.uri.toString(), metadata), document))
                    } catch (e: IllegalStateException) {
                        Log.e(TAG, "Failed to get audio file: ", e)
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        const val TAG = "MainActivity"
        const val REQUEST_CODE_OPEN_DOCUMENT = 100
    }
}
