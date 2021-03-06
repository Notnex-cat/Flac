package com.example.flacsearcher

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flacsearcher.adapters.SongListAdapter
import com.example.flacsearcher.fragments.PlayFragment
import com.example.flacsearcher.fragments.SettingsFragment
import com.example.flacsearcher.fragments.WebSearchFragment
import com.example.flacsearcher.model.SongModel
import com.example.flacsearcher.service.PlayMusicService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_scrolling.*
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {


    private var songModelData: ArrayList<SongModel> = ArrayList()
    private var songListAdapter: SongListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager2 = findViewById(R.id.fl_wrapper)

        val fragments: Array<Fragment> = arrayOf(PlayFragment(), WebSearchFragment(), SettingsFragment())

        val adapter = ViewPagerAdapter(fragments, this)
        viewPager.adapter = adapter

        val playFragment = PlayFragment()
        val webSearchFragment = WebSearchFragment()
        val settingsFragment = SettingsFragment()

        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", true)

        songListAdapter = SongListAdapter(songModelData, applicationContext)
        val layoutManager = LinearLayoutManager(applicationContext)
        //recycle_view.layoutManager = layoutManager
        //recycle_view.adapter = songListAdapter
        val songListAdapter = Intent(this, SongListAdapter::class.java)
        this.startService(songListAdapter)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions,0)
        }
        //makeCurrentFragment(playFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                //R.id.player -> makeCurrentFragment(playFragment)
               // R.id.websearch -> makeCurrentFragment(webSearchFragment)
                //R.id.settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isNightModeOn) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode", true)
                sharedPrefsEdit.apply()
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode", false)
                sharedPrefsEdit.apply()
            }
        }
    }
       // private fun makeCurrentFragment(fragment: Fragment) =
       //         supportFragmentManager.beginTransaction().apply {
       //             replace(R.id.fl_wrapper, fragment)
      //              commit()
      //          }

        private var doubleBackToExitPressedOnce = false
        override fun onBackPressed() {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "???????????? ?????? ??????, ???????? ????????????", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 1000)
        }
    class MyApp : Application() {
        override fun onCreate() {
            super.onCreate()
            startService(Intent(this, PlayMusicService::class.java))
        }
    }
}