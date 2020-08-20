package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentDirectoryPickerBinding

class DirectoryPicker : Fragment() {

//    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        Log.e("MainActivity", "Coroutine failed: ${throwable.localizedMessage}")
//    }
//
//    private val scope = CoroutineScope(Dispatchers.Main + exceptionHandler)
//
//    private lateinit var documentAdapter: DocumentAdapter
//
//    private val tagLib = KTagLib()

    private lateinit var binding: FragmentDirectoryPickerBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentDirectoryPickerBinding.inflate(inflater)
        binding.chooseDirectoryButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_directoryPicker_to_taggerFragment)
        }
        return binding.root
    }
}