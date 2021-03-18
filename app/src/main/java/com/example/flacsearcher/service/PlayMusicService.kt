@file:Suppress("DEPRECATION")

package com.example.flacsearcher.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import androidx.media2.exoplayer.external.ExoPlayer
import com.example.flacsearcher.adapters.SongListAdapter

class PlayMusicService: Service() {
   private var currentPos:Int = 0
    private var musicDataList:ArrayList<String> = ArrayList()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        musicDataList = intent!!.getStringArrayListExtra(SongListAdapter.MUSICLIST)!!
        currentPos = intent.getIntExtra(SongListAdapter.MUSICITEMPOS, 0)
        Toast.makeText(this, "" + musicDataList[currentPos], Toast.LENGTH_SHORT).show()
        var mp = MediaPlayer()

      /*  if (mp!=null){
            mp.stop()
            mp.release()
           // mp = null
        }*/

        mp.setDataSource(musicDataList[currentPos])
        mp.prepare()
        mp.start()


        return super.onStartCommand(intent, flags, startId)
    }
}







