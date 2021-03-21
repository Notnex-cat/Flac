package com.example.flacsearcher.model

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlaybackViewModel: Service() {
    private var lastSong: String? = null
    private var pref: SharedPreferences? = null
    private var mp: MediaPlayer?=null

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    fun mPlay() {
    pref = getSharedPreferences("Table", Context.MODE_PRIVATE)
    lastSong = pref?.getString("last", null)



    mp = MediaPlayer()
    mp?.setDataSource(lastSong)
    mp?.prepareAsync()

}

    fun startPlaying(){
        mp?.start()
    }

    fun pausePlaying(){
        mp?.pause()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}