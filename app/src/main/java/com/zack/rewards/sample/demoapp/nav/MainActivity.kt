package com.zack.rewards.sample.demoapp.nav

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.zack.rewards.sample.demoapp.R

class MainActivity : AppCompatActivity() {
    companion object {
        private const val ACTION_OPEN_WEB = "com.zack.android.demo.OPEN_WEB"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
                ?: return

        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setUpActionBar(navController)

        checkAction(navController, intent.action)
    }

    private fun checkAction(navController: NavController, action: String?) {
        action?.let {
            if (it == ACTION_OPEN_WEB) {
                Log.d("DemoApp", "Shortcut to open web")
                navController.navigate(MainFragmentDirections.goToShortcutFragment())
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