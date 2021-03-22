package com.example.flacsearcher.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlaybackViewService: Service() {
    private var lastSong: String? = null
    private var pref: SharedPreferences? = null
    private var mp: MediaPlayer?=null

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    fun mPlay() {
    pref = getSharedPreferences("Table", Context.MODE_PRIVATE)
    lastSong = pref?.getString("last", null)
Toast.makeText(this, "good", Toast.LENGTH_SHORT).show()
        mp = MediaPlayer()
        mp!!.setDataSource(lastSong)
        mp!!.prepareAsync()
       // mp!!.prepare()
        mp!!.setOnPreparedListener {
            mp!!.start()}
        /*if (!mp!!.isPlaying) {
            mp?.start()
          //  mp?.seekTo(time!!)
          //  initializeSeekBar()
           // view.songmax.text = toMandS(mp!!.duration.toLong())
           // view.play.setImageResource(R.drawable.pause)
        } else {
            mp?.pause()
           // view.play.setImageResource(R.drawable.play)
        }*/
}
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}