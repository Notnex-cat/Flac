package com.example.flacsearcher

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.flacsearcher.fragments.PlayFragment
import com.example.flacsearcher.fragments.SettingsFragment
import com.example.flacsearcher.fragments.WebSearchFragment
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val playFragment = PlayFragment()
        val webSearchFragment = WebSearchFragment()
        val settingsFragment = SettingsFragment()
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)




        makeCurrentFragment(playFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.player -> makeCurrentFragment(playFragment)
                R.id.websearch -> makeCurrentFragment(webSearchFragment)
                R.id.settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }

        fun showFragmentSetup() {
            val setupFragment = PlayFragment()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fl_wrapper, setupFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
        if(isNightModeOn){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPrefsEdit.putBoolean("NightMode", true)
            sharedPrefsEdit.apply()
        }else{
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPrefsEdit.putBoolean("NightMode", false)
            sharedPrefsEdit.apply()
        }
    }
        private fun makeCurrentFragment(fragment: Fragment) =
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_wrapper, fragment)
                    commit()
                }

        private var doubleBackToExitPressedOnce = false
        override fun onBackPressed() {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Жмакни ещё раз, если смелый", Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 1000)
        }


}