@file:Suppress("DEPRECATION")

package com.example.flacsearcher.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider
import com.example.flacsearcher.MainActivity
import com.example.flacsearcher.R
import com.example.flacsearcher.adapters.SongListAdapter
import com.mtechviral.mplaylib.MusicFinder
import java.util.*
import kotlin.collections.ArrayList

class PlaybackService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {
        //media player
        private var player: MediaPlayer? = null

        //song list
        private var songs: ArrayList<MusicFinder.Song>? = null

    private var currentPos: Int = 0
    private var musicDataList: ArrayList<String> = ArrayList()
    private var currentSong: String? = null

        //binder
        private val musicBind: IBinder = MusicBinder()

        //title of current song
        private var songTitle = ""

        //shuffle flag and random
        private var shuffle = false
        private var rand: Random? = null
        override fun onCreate() {
            //create the service
            super.onCreate()
            //initialize position
            currentPos = 0
            //random
            rand = Random()
            //create player
            player = MediaPlayer()
            //initialize
            initMusicPlayer()
        }
    
    fun initMusicPlayer() {
            //set player properties
            //
          //  player!!.setWakeMode(
//                ApplicationProvider.getApplicationContext(),
              //  PowerManager.PARTIAL_WAKE_LOCK
           // )
            player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            //set listeners
            player!!.setOnPreparedListener(this)
            player!!.setOnCompletionListener(this)
            player!!.setOnErrorListener(this)
           // Toast.makeText(this, songs.toString(), Toast.LENGTH_SHORT).show()
        }

        //pass song list
        fun setList(theSongs: ArrayList<MusicFinder.Song>?) {
            songs = theSongs
            Log.d("frfrfr", songs.toString())
        }

        //binder
        inner class MusicBinder : Binder() {
            val service: PlaybackService
                get() = this@PlaybackService
        }

        //activity will bind to service
        override fun onBind(intent: Intent?): IBinder {
            return musicBind
        }

        //release resources when unbind
        override fun onUnbind(intent: Intent?): Boolean {
            player!!.stop()
            player!!.release()
            return false
        }

        //play a song
        fun playSong() {
            //play
            player!!.reset()

            //get song
            val playSong: MusicFinder.Song = songs!![currentPos]
            //get title
            songTitle = playSong.title
            //get id
            val currSong: Long = playSong.id
            //set uri
            val trackUri: Uri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong
            )
            //set the data source
            try {
                player!!.setDataSource(ApplicationProvider.getApplicationContext(), trackUri)

                //player!!.setDataSource(ApplicationProvider.getApplicationContext(), trackUri)
            } catch (e: Exception) {
                Log.e("MUSIC SERVICE", "Error setting data source", e)
            }
            player!!.prepareAsync()
        }

        //set the song
        fun setSong(songIndex: Int) {
            currentPos = songIndex
        }

        override fun onCompletion(mp: MediaPlayer) {
            //check if playback has reached the end of a track
            if (player!!.currentPosition > 0) {
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
            Toast.makeText(this, "gdgg"+ songs.toString(), Toast.LENGTH_SHORT).show()
            //notification
            val notIntent = Intent(this, MainActivity::class.java)
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
            startForeground(NOTIFY_ID, not)
        }

        //playback methods
        val posn: Int
            get() = player!!.currentPosition

        val dur: Int
            get() = player!!.duration
        val isPng: Boolean
            get() = player!!.isPlaying

        fun pausePlayer() {
            player!!.pause()
        }

        fun seek(posn: Int) {
            player!!.seekTo(posn)
        }

        fun go() {
            player!!.start()
        }
        //skip to previous track
        fun playPrev() {
            currentPos--
            if (currentPos < 0) currentPos = songs!!.size - 1
            playSong()
        }

        //skip to next
        fun playNext() {
            if (shuffle) {
                var newSong = currentPos
                while (newSong == currentPos) {
                    newSong = rand!!.nextInt(songs!!.size)
                }
                currentPos = newSong
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
            shuffle = !shuffle
        }

        companion object {
            //notification id
            private const val NOTIFY_ID = 1
        }
    }