@file:Suppress("DEPRECATION")

package com.example.flacsearcher.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import com.example.flacsearcher.adapters.SongListAdapter


class PlayMusicService: Service() { 
    
   //var mp: MediaPlayer? = null
   private var currentPos:Int = 0
    private var musicDataList:ArrayList<String> = ArrayList()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        musicDataList = intent!!.getStringArrayListExtra(SongListAdapter.MUSICLIST)!!
        currentPos = intent.getIntExtra(SongListAdapter.MUSICITEMPOS, 0)
        Toast.makeText(this, "" + musicDataList[currentPos], Toast.LENGTH_SHORT).show()

        val path: String = Environment.getExternalStorageDirectory().toString() + ""




      //val uriSong: String = ContentUris.withAppendedId(musicDataList[currentPos], 140) as String

     //  val mediaPlayer: MediaPlayer = MediaPlayer().apply {
  //  setAudioStreamType(AudioManager.STREAM_MUSIC) //to send the object to the initialized state
  //  setDataSource(uriSong) //to set media source and send the object to the initialized state
  //  prepare() //to send the object to the prepared state, this may take time for fetching and decoding
  //  start() }//to start the music and send the object to started state


        /* var mp: MediaPlayer = MediaPlayer.create(this, path)
        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        mp.start()*/

       /* val uri: Uri = Uri.parse(musicDataList[currentPos])

        try {
            mp?.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp?.setDataSource( "/storage/emulated/0/Download/mp3rington_club_id_9436.mp3");
            //mp?.prepare();
            mp?.start();
        }finally {

        }*/


      //  var uri: Uri = Uri.fromFile(File())

         val media: MediaPlayer? = null
                 //MediaPlayer.create(this, musicDataList[currentPos])
         media?.setDataSource("/storage/emulated/0/02.flac")
        //  media?.prepare()
      // media?.setOnPreparedListener {

           media?.start()

      // }

        return super.onStartCommand(intent, flags, startId)
    }

}







