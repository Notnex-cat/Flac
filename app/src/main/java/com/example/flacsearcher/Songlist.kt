package com.example.flacsearcher

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flacsearcher.adapters.SongListAdapter
import com.example.flacsearcher.model.SongModel
import com.example.flacsearcher.service.PlayMusicService
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_songlist.*
import kotlinx.android.synthetic.main.content_scrolling.*

@Suppress("DEPRECATION")
class Songlist : AppCompatActivity() {
    private var lastSong: String? = null
    private var pref: SharedPreferences? = null
    private var songModelData: ArrayList<SongModel> = ArrayList()
    private var songListAdapter: SongListAdapter? = null
    private var mp: MediaPlayer?=null
    private var isPng: Int? = null

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)
        pref = getSharedPreferences("Table", Context.MODE_PRIVATE)
        lastSong = pref?.getString("last", null)

        songListAdapter = SongListAdapter(songModelData, applicationContext)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycle_view.layoutManager = layoutManager
        recycle_view.adapter = songListAdapter

        val songCursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null, null, null, null
        )
        while (songCursor != null && songCursor.moveToNext()) {
            val songName =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val songDuration =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            val songPath =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            songModelData.add(SongModel(songName, songDuration, songPath))
        }
        // isPng = pref?.getBoolean("isPng", false)!!
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = "Playlist"
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
                startService(Intent(this, PlayMusicService::class.java))
                isPng = 1
                saveIsPng(isPng)
                Snackbar.make(view, "Start playing...", Snackbar.LENGTH_LONG)
                  .setAction("Action", null).show()
        }
if (isPng==1){
    fab.setImageResource(R.drawable.play)
    isPng = 0
    Toast.makeText(this, "play", Toast.LENGTH_SHORT).show()
    saveIsPng(isPng)
}else{
    fab.setImageResource(R.drawable.pause)
    Toast.makeText(this, "pAUSE", Toast.LENGTH_SHORT).show()
    isPng = 1
    saveIsPng(isPng)
}

    }
    private fun saveIsPng(png: Int?) {
        val editor = pref?.edit()
            editor?.putInt("isPng", png!!)

        editor?.apply()
    }
}