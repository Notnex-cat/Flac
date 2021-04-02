package com.example.flacsearcher.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.flacsearcher.R
import com.example.flacsearcher.Songlist
import com.example.flacsearcher.adapters.SongListAdapter
import com.example.flacsearcher.service.PlayMusicService
import kotlinx.android.synthetic.main.fragment_play.*
import kotlinx.android.synthetic.main.fragment_play.view.*
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
open class PlayFragment() : Fragment(), Parcelable {
    private var lastSong: String? = null
    private var pref: SharedPreferences? = null
    private var mp: MediaPlayer?=null
    private var lastTime: Int? = null  // current time save
    private var time: Int? = null  // current time read
    private var timeMax: Int? = null//read
    private var currentPosit: Int = 0
    private var isPng: Int? = null


    constructor(parcel: Parcel) : this() {
        lastSong = parcel.readString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
    val appSettingPrefs: SharedPreferences = requireContext().getSharedPreferences("AppSettingPrefs", 0)
    val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
    val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)
    val view: View = inflater.inflate(R.layout.fragment_play, container, false)
        pref = this.activity?.getSharedPreferences("Table", Context.MODE_PRIVATE)
        lastSong = pref?.getString("last", null)
        time = pref?.getInt("currentTim", 0)
        timeMax = pref?.getInt("timMax", 0)
        isPng = pref?.getInt("isPng", 0)!!
        Log.d("png", isPng.toString())

        requireActivity().startForegroundService(Intent(activity, SongListAdapter::class.java))
        view.currentTime.text = toMandS(time!!.toLong())
        view.songmax.text = toMandS(timeMax!!.toLong())
        view.seekbar.max = timeMax as Int
        view.seekbar.progress = time as Int
        mp = MediaPlayer()
        mp?.setDataSource(lastSong)
        mp?.prepareAsync()

    view.play.setOnLongClickListener {   // stop button
        if (mp!!.isPlaying) {
            mp!!.stop()
            seekbar.progress = 0
            time = 0
            play.setImageResource(R.drawable.play)
        }
        mp?.prepareAsync()
        true
    }




    view.play.setOnClickListener { //play pause
        requireActivity().startService(Intent(activity, PlayMusicService::class.java))

        isPng = 1
        saveIsPng(isPng)
    }

        if (isPng == 1) {
            mp?.seekTo(time!!)
            initializeSeekBar()
            view.songmax.text = toMandS(mp!!.duration.toLong())
            view.play.setImageResource(R.drawable.pause)
            isPng = 1
            saveIsPng(isPng)
            Toast.makeText(activity, "Play", Toast.LENGTH_SHORT).show()
        } else {
            view.play.setImageResource(R.drawable.play)
            Toast.makeText(activity, "Pause", Toast.LENGTH_SHORT).show()
            isPng = 0
            saveIsPng(isPng)
        }



        lastTime = mp!!.currentPosition
        view.loop.setOnClickListener {
            mp?.isLooping = true
            loop.isPressed = true
        }

            if (isNightModeOn){
            view.darklight.setImageResource(R.drawable.light)
        }else{
           view.darklight.setImageResource(R.drawable.dark)
        }

        view.darklight.setOnClickListener{
            if (isNightModeOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
            }
        }
        view.seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed){
                    mp!!.seekTo(pos)
                    curto = pos.toString()
                   // saveCurrentTime(curto.toInt())

                    val musicDataIntent = Intent(activity, PlayMusicService::class.java)
                    musicDataIntent.putExtra(curto, pos)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        view.select.setOnClickListener {
                val intent = Intent(activity, Songlist::class.java)
                startActivity(intent)
        }
            return view
        }

    private fun saveIsPng(res: Int?) {
        val editor = pref?.edit()
        if (res != null) {
            editor?.putInt("isPng", res)
        }
        editor?.apply()
    }

    private fun initializeSeekBar() {
        val handler = Handler()
        handler.postDelayed(object: Runnable{
            override fun run() {
                try {
                    currentPosit = pref?.getInt("currentTim", 0)!!
                seekbar.progress = currentPosit
                    view?.currentTime?.text = toMandS(currentPosit.toLong())
                  //  lastTime = mp!!.currentPosition
                   // saveData(lastTime!!.toInt())
                handler.postDelayed(this,250)
                    timeMax = pref?.getInt("timMax", 0)
                    val max = timeMax
                    songmax.text = toMandS(max!!.toLong())
                    currentTime.text = toMandS(currentPosit.toLong())
            }catch (e: Exception){
            }
            }
        }, 0)
    }
    private fun toMandS(millis: Long): String {
        return String.format(
            "%2d:%2d",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(millis)

            )
        )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lastSong)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<PlayFragment> {
        var curto = "currentpostoseekbar"
        override fun createFromParcel(parcel: Parcel): PlayFragment {
            return PlayFragment(parcel)
        }

        override fun newArray(size: Int): Array<PlayFragment?> {
            return arrayOfNulls(size)
        }
    }

}