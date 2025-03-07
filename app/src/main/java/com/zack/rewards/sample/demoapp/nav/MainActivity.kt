package com.zack.rewards.sample.demoapp.nav

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.zack.rewards.sample.demoapp.R
import com.zack.rewards.sample.demoapp.shortcut.ShortcutFragment.Companion.ACTION_OPEN_WEB
import com.zack.rewards.sample.demoapp.util.Pref
import com.zack.rewards.sample.demoapp.util.restartLockScreenService


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        fun getCloseIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra("CLOSE_APP", true)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DemoApp", "MainActivity onCreate")
        super.onCreate(savedInstanceState)
        Pref.init(this)

        val closeApp = intent.getBooleanExtra("CLOSE_APP", false)
        if (closeApp) {
            restartLockScreenService("MainActivity#onCreate")
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
                ?: return

        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setUpActionBar(navController)
        checkAction(navController, intent.action, intent.data)
    }

    override fun onStart() {
        Log.d("DemoApp", "MainActivity onStart")
        super.onStart()
        restartLockScreenService("MainActivity#onStart")
    }

    private fun checkAction(navController: NavController, action: String?, uri: Uri?) {
        action?.let {
            if (it == ACTION_OPEN_WEB) {
                Log.d("DemoApp", "Shortcut to open web")
                navController.navigate(MainFragmentDirections.goToShortcutFragment(uri?.toString()))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    private fun setUpActionBar(navController: NavController) {
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}