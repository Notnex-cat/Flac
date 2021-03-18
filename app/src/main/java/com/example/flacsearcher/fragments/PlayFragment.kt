package com.example.flacsearcher.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.flacsearcher.R
import com.example.flacsearcher.Songlist
import com.example.flacsearcher.service.PlayMusicService
import kotlinx.android.synthetic.main.fragment_play.*
import kotlinx.android.synthetic.main.fragment_play.view.*

class PlayFragment : Fragment() {
    private var playMusicService = PlayMusicService()
    private var lastSong: String? = null
    private var mp:MediaPlayer?=null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val appSettingPrefs: SharedPreferences = requireContext().getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)
        val view: View = inflater.inflate(R.layout.fragment_play, container, false)
        val musicDataList = playMusicService.musicDataList
        val currentPos = playMusicService.currentPos
        lastSong = playMusicService.lastSong
        view.song_name.text = lastSong
        Toast.makeText(activity, "last song is:$lastSong", Toast.LENGTH_SHORT).show()

        view.play.setOnLongClickListener {
            if (mp!!.isPlaying) {
                mp!!.stop()
                play.setImageResource(R.drawable.play)
            }
            mp!!.prepareAsync()
            true
        }
            view.play.setOnClickListener {
                if (mp!!.isPlaying) {
                    mp!!.pause()
                    view.play.setImageResource(R.drawable.play)
                } else {
                    mp!!.setDataSource(musicDataList[currentPos])
                    mp!!.prepare()
                    mp!!.start()
                    view.play.setImageResource(R.drawable.pause)
                }
            }
        if (isNightModeOn){
            view.darklight.setImageResource(R.drawable.light)
        }else{
           view.darklight.setImageResource(R.drawable.dark)
        }

        view.darklight.setOnClickListener{
            if (isNightModeOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
            }
        }

        view.select.setOnClickListener {
                val intent = Intent(activity, Songlist::class.java)
                startActivity(intent)}
            return view
        }
}