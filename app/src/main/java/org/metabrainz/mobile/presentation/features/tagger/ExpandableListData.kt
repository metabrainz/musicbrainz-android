package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentTaggerBinding

class ExpandableListData {

    val data: HashMap<String,ArrayList<Pair<String,String>>>?
        get() {
            val expandableListDetail = HashMap<String,ArrayList<Pair<String,String>>>()
            var TaglibFetched = ArrayList<Pair<String,String>>()
            TaglibFetched.add(Pair("Title",""))
            TaglibFetched.add(Pair("Track",""))
            TaglibFetched.add(Pair("Duration",""))
            TaglibFetched.add(Pair("Artist",""))
            TaglibFetched.add(Pair("Album",""))
            TaglibFetched.add(Pair("Year",""))
            TaglibFetched.add(Pair("Disc",""))
            TaglibFetched.add(Pair("MimeType",""))
            TaglibFetched.add(Pair("Size",""))
            expandableListDetail["What Your Audio Has"] = TaglibFetched

            var ServerFetched = ArrayList<Pair<String,String>>()
            ServerFetched.add(Pair("Title",""))
            ServerFetched.add(Pair("Track",""))
            ServerFetched.add(Pair("Duration",""))
            ServerFetched.add(Pair("Artist",""))
            ServerFetched.add(Pair("Album",""))
            ServerFetched.add(Pair("Year",""))
            ServerFetched.add(Pair("Disc",""))
            ServerFetched.add(Pair("Mimetype",""))
            ServerFetched.add(Pair("Size",""))
            expandableListDetail["What we got for you"] = ServerFetched
            return expandableListDetail
        }


}