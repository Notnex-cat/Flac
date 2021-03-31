@file:Suppress("DEPRECATION")

package com.example.flacsearcher.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import kotlinx.android.synthetic.main.fragment_play.*
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.example.flacsearcher.R
import com.example.flacsearcher.Songlist
import com.example.flacsearcher.adapters.SongListAdapter
import java.util.*
import kotlin.collections.ArrayList

class PlayMusicService: Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    companion object{
    var poss = "pos"
        var lasts = "lastsong"
}
    private var lastSong: String? = null
    private var pref: SharedPreferences? = null
    private var currentSong: String? = null
    private var currentPos: Int = 0
    private var musicDataList: ArrayList<String> = ArrayList()

    private var mp: MediaPlayer? = null
    //song list
    private var songs: ArrayList<String>? = null

    //binder
    private val musicBind: IBinder = MusicBinder()

    //title of current song
    private var songTitle = ""
    private var lastso: String? = null

    //shuffle flag and random
    private var shuffle = false
    private var rand: Random? = null

    override fun onCreate() {

        //create the service
        super.onCreate()
        //random
        rand = Random()
        //create player
        mp = MediaPlayer()
        //initialize
        // initMusicPlayer()
    pref = getSharedPreferences("Table", Context.MODE_PRIVATE)
    lastSong = pref?.getString("last", null)
        mp = MediaPlayer()
        mp!!.setDataSource(lastSong)
        mp!!.prepare()
      onControl()
    }

    private fun onControl() {
        if (!mp!!.isPlaying){
            onPlay()
        }else{
            pausePlayer()
        }
    }

    fun onPlay() {
            mp!!.setOnPreparedListener {
                mp!!.start()}
            saveData(lastSong.toString())
    }

    fun pausePlayer() {
            Toast.makeText(this,"pause", Toast.LENGTH_SHORT).show()
        mp!!.pause()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        loadData()
    try {
        musicDataList = intent?.getStringArrayListExtra(SongListAdapter.MUSICLIST)!!
        currentPos = intent.getIntExtra(SongListAdapter.MUSICITEMPOS, 0)
        currentSong = musicDataList[currentPos]
        lastSong = musicDataList[currentPos]

        poss = currentPos.toString()
        songs = musicDataList

        if (mp != null) {
            mp!!.stop()
            mp!!.release()
            mp = null
        }
        mp = MediaPlayer()
        mp!!.setDataSource(lastSong)
        mp!!.prepare()
        mp!!.setOnPreparedListener {
        mp!!.start()}
        saveData(lastSong.toString())
        savePos(poss.toInt())
    } catch (e: Exception) {
        Log.e("MUSIC SERVICE", "Error setting data source", e)}
    return super.onStartCommand(intent, flags, startId)
    }

    private fun loadData() {
        pref = getSharedPreferences("Table", Context.MODE_PRIVATE)
        lastSong = pref?.getString("last", null)
    }

    private fun saveData(res: String) {
        val editor = pref?.edit()
        editor?.putString("last", res)
        editor?.apply()
    }
    private fun savePos(pos: Int) {
        val editor = pref?.edit()
        editor?.putInt("pos", pos)
        editor?.apply()
    }
       /* fun initMusicPlayer() {
            //set player properties
            mp!!.setWakeMode(
                ApplicationProvider.getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK
            )
            mp!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            //set listeners
            mp!!.setOnPreparedListener(this)
            mp!!.setOnCompletionListener(this)
            mp!!.setOnErrorListener(this)
        }*/


        //binder
        inner class MusicBinder : Binder() {
            val service: PlayMusicService
                get() = this@PlayMusicService
        }

        //activity will bind to service
        override fun onBind(intent: Intent?): IBinder {
            return musicBind
        }

        //release resources when unbind
        override fun onUnbind(intent: Intent?): Boolean {
            mp!!.stop()
            mp!!.release()
            return false
        }

        //play a song
        private fun playSong() {
    /*  //play
     mp!!.reset()
     //get song
     val playSong: String = songs!![currentPos]
     //get title
    songTitle = playSong.title
     //get i     val currSong: Long = playSong.id
     //set uri
     val trackUri: Uri = ContentUris.withAppendedId(
         MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
         currSong
     )*/
     //set the data source
     try {
         mp!!.setDataSource(musicDataList[currentPos])
     } catch (e: Exception) {
         Log.e("MUSIC SERVICE", "Error setting data source", e)
     }
     mp!!.prepareAsync()
 }

 //set the song
 fun setSong(songIndex: Int) {
     currentPos = songIndex
 }

 override fun onCompletion(mp: MediaPlayer) {
     //check if playback has reached the end of a track
     if (mp.currentPosition > 0) {
         mp.reset()
         playNext()
     }
 }

 override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
     Log.v("MUSIC PLAYER", "Playback Error")
     mp.reset()
     return false
 }

 override fun onPrepared(mp: MediaPlayer) {
     //start playback
     mp.start()
     //notification
     val notIntent = Intent(this, Songlist::class.java)
     notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
     val pendInt = PendingIntent.getActivity(
         this, 0,
         notIntent, PendingIntent.FLAG_UPDATE_CURRENT
     )
     val builder: Notification.Builder = Notification.Builder(this)
     builder.setContentIntent(pendInt)
         .setSmallIcon(R.drawable.play)
         .setTicker(songTitle)
         .setOngoing(true)
         .setContentTitle("Playing")
         .setContentText(songTitle)
     val not: Notification = builder.build()
     //startForeground(NOTIFY_ID, not)
 }

 //playback methods
 val posn: Int
     get() = mp!!.currentPosition

 val dur: Int
     get() = mp!!.duration
 val isPng: Boolean
     get() = mp!!.isPlaying



 fun seek(posn: Int) {
     mp!!.seekTo(posn)
 }

 fun go() {
     mp!!.start()
 }

 //skip to previous track
 fun playPrev() {
     currentPos--
     if (currentPos < 0) currentPos = songs!!.size - 1
     playSong()
 }

 //skip to next
 private fun playNext() {
     if (shuffle) {
         var newSong = currentSong
         while (newSong == currentSong) {
             newSong = rand!!.nextInt(songs!!.size).toString()
         }
         currentSong = newSong
     } else {
         currentPos++
         if (currentPos >= songs!!.size) currentPos = 0
     }
     playSong()
 }

 override fun onDestroy() {
     stopForeground(true)
 }

 //toggle shuffle
 fun setShuffle() {
     shuffle = if (shuffle){
         false
 }else {
         true
     }
 }

}