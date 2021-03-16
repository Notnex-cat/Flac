package com.example.flacsearcher

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flacsearcher.adapters.SongListAdapter
import com.example.flacsearcher.model.SongModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_scrolling.*

@Suppress("DEPRECATION")
class Songlist : AppCompatActivity() {

    private var songModelData: ArrayList<SongModel> = ArrayList()
    private var songListAdapter: SongListAdapter? = null
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        songListAdapter = SongListAdapter(songModelData, applicationContext)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycle_view.layoutManager = layoutManager
        recycle_view.adapter = songListAdapter

        val songCursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null)
       while (songCursor != null && songCursor.moveToNext()) {
           val songId: Long = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media._ID))
           val songName = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
           val songDuration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
           val songPath = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
           songModelData.add(SongModel(songName, songDuration,songPath, songId))
       }
            setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = "Playlist"
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Start playing", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }