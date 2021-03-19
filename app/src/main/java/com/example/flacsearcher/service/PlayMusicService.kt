@file:Suppress("DEPRECATION")

package com.example.flacsearcher.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import com.example.flacsearcher.adapters.SongListAdapter

class PlayMusicService: Service() {
    var lastSong: String? = null
    private var pref: SharedPreferences? = null
    var currentSong: String? = null
    var currentPos: Int = 0
    var musicDataList: ArrayList<String> = ArrayList()
    private var mp: MediaPlayer? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        loadData()
        musicDataList = intent!!.getStringArrayListExtra(SongListAdapter.MUSICLIST)!!
        currentPos = intent.getIntExtra(SongListAdapter.MUSICITEMPOS, 0)
        currentSong = musicDataList[currentPos]
        lastSong = musicDataList[currentPos]
        pref = getSharedPreferences("Table", Context.MODE_PRIVATE)


        if (mp != null) {
            mp!!.stop()
            mp!!.release()
            mp = null
        }
        mp = MediaPlayer()
        mp!!.setDataSource(musicDataList[currentPos])
        mp!!.prepare()
        mp!!.setOnPreparedListener { 
        mp!!.start()}
        saveData(lastSong.toString())
        return super.onStartCommand(intent, flags, startId)
    }

    private fun loadData() {
        pref = getSharedPreferences("Table", Context.MODE_PRIVATE)
        lastSong = pref?.getString("last", null)
        // Toast.makeText(this, "last song is:" + lastSong, Toast.LENGTH_SHORT).show()
    }

    private fun saveData(res: String) {
        val editor = pref?.edit()
        editor?.putString("last", res)
        editor?.apply()
    }
}