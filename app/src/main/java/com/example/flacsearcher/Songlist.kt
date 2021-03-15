package com.example.flacsearcher

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
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

class Songlist : AppCompatActivity() {

    var songModelData: ArrayList<SongModel> = ArrayList()
    var songListAdapter: SongListAdapter? = null
    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        songListAdapter = SongListAdapter(songModelData, applicationContext)
        var layoutManager = LinearLayoutManager(applicationContext)
        recycle_view.layoutManager = layoutManager
        recycle_view.adapter = songListAdapter

        var songCursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null)
       while (songCursor != null && songCursor.moveToNext()) {
           val songId: Long = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media._ID))
           val songName = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
           val songDuration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
           val songPath = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
           songModelData.add(SongModel(songName, songDuration,songPath, songId))
       }
        val uriSong: Uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 140)

      /*  var mp: MediaPlayer = MediaPlayer.create(this, uriSong)
        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        mp.start()*/

            setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = "Playlist"
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Start playing", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            }

        }
    }