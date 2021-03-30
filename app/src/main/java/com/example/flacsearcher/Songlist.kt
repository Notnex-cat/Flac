package com.example.flacsearcher

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flacsearcher.adapters.SongListAdapter
import com.example.flacsearcher.model.SongModel
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

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)
        pref = getSharedPreferences("Table", Context.MODE_PRIVATE)
        lastSong = pref?.getString("last", null)
       // Toast.makeText(this, "last song is:$lastSong", Toast.LENGTH_SHORT).show()

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
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = "Playlist"
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            if (mp == null) {
                mp = MediaPlayer()
                mp!!.setDataSource(lastSong)
                mp!!.prepareAsync()
                mp!!.setOnPreparedListener {
                    mp!!.start()}
                fab.setImageResource(R.drawable.pause)
                Snackbar.make(view, "Start playing...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                mp?.pause()
                fab.setImageResource(R.drawable.play)
                Snackbar.make(view, "Pause playing...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                mp = null
            }
        }
    }
}