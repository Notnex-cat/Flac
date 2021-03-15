package com.example.flacsearcher.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flacsearcher.R
import kotlinx.android.synthetic.main.fragment_play.view.*

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val uri: String = "/storage/emulated/0/Download/mp3rington_club_id_9436.mp3"
        var mediaPlayer: MediaPlayer? = null
        mediaPlayer?.setDataSource(uri)
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        view.play.setOnClickListener() {
                mediaPlayer?.start();
            }
        return view
    }
}