package com.codexo.cryptopeak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.codexo.cryptopeak.repository.Repository
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNav()
    }

    private fun setupBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.coinFragment,
                R.id.favoriteFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottom_nav.setupWithNavController(navController)
    }
}