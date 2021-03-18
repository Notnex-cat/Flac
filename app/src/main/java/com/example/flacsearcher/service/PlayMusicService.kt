@file:Suppress("DEPRECATION")

package com.example.flacsearcher.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.flacsearcher.adapters.SongListAdapter

class PlayMusicService: Service() {
    var currentSong: String? = null
    var currentPos:Int = 0
    var musicDataList:ArrayList<String> = ArrayList()
    private var mp:MediaPlayer?=null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        musicDataList = intent!!.getStringArrayListExtra(SongListAdapter.MUSICLIST)!!
        currentPos = intent.getIntExtra(SongListAdapter.MUSICITEMPOS, 0)
         currentSong = musicDataList[currentPos]

        if (mp!=null){
            mp!!.stop()
            mp!!.release()
            mp = null
        }
        mp = MediaPlayer()
        mp!!.setDataSource(musicDataList[currentPos])
        mp!!.prepare()
        mp!!.start()

        return super.onStartCommand(intent, flags, startId)
    }
}







