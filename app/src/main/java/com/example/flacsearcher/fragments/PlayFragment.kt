package com.example.flacsearcher.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.flacsearcher.R
import com.example.flacsearcher.Songlist
import com.example.flacsearcher.adapters.SongListAdapter
import com.example.flacsearcher.service.PlayMusicService
import kotlinx.android.synthetic.main.fragment_play.*
import kotlinx.android.synthetic.main.fragment_play.view.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class PlayFragment : Fragment() {
    private var lastSong: String? = null
    private var pref: SharedPreferences? = null
    private var mp: MediaPlayer?=null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        Toast.makeText(activity, "Play Fragment started", Toast.LENGTH_SHORT).show()
    val appSettingPrefs: SharedPreferences = requireContext().getSharedPreferences("AppSettingPrefs", 0)
    val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
    val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)
    val view: View = inflater.inflate(R.layout.fragment_play, container, false)
    pref = this.activity?.getSharedPreferences("Table", Context.MODE_PRIVATE)
    lastSong = pref?.getString("last", null)

    //val playingMusic = playMusicService!!::playingMusic

    view.play.setOnLongClickListener {   // stop button
        if (mp != null) {
            mp!!.stop()
            mp = null
            play.setImageResource(R.drawable.play)
        }
        mp!!.prepareAsync()
        true
    }

    view.play.setOnClickListener { //play pause
        if (mp == null) {
            mp = MediaPlayer()
            mp!!.setDataSource(lastSong)
            mp!!.prepare()
            mp!!.start()
            view.songmax.text = toMandS(mp!!.duration.toLong())
            //initializeSeekBar()
            view.play.setImageResource(R.drawable.pause)
        } else {
            mp!!.pause()
            view.play.setImageResource(R.drawable.play)
            }
        }

       /* seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })*/

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
                startActivity(intent)
        }
            return view
        }

    private fun toMandS(millis: Long): String {
        return String.format(
            "%2d:%2d",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(millis)
            )
        )


    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(activity, "PlayFragment died", Toast.LENGTH_SHORT).show()
    }
}

    /* private fun initializeSeekBar(){
         seekbar.max = totalTime!!
         song_name.text = getString((totalTime!!))

         val handler = Handler()
         handler.postDelayed(object: Runnable{
             override fun run() {
                 try {
                 seekbar.progress = mp!!.currentPosition
                 handler.postDelayed(this, 1000)
                 }catch (e: Exception){
                     seekbar.progress = 0
                 }
             }
         },0)*/
