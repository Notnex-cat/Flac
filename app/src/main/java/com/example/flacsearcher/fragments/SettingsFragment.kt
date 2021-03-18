package com.example.flacsearcher.fragments

import android.content.ContentValues.TAG
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flacsearcher.R
import kotlinx.android.synthetic.main.fragment_play.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {
    private var output: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        output = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"
        Log.e(TAG, "URL:" + output);
        Log.e(TAG, "exists: " + output.toString());

view.start.setOnClickListener {

    val media: MediaPlayer? = null
    //MediaPlayer.create(this, musicDataList[currentPos])
    if (media != null) {
        media.setDataSource(output)
    }
    media!!.start()
}
        return view
    }
}