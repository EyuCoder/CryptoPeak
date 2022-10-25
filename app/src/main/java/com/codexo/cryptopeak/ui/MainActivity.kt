package com.codexo.cryptopeak.ui

import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.data.Repository
import com.codexo.cryptopeak.data.database.CoinDatabase
import com.codexo.cryptopeak.databinding.ActivityMainBinding
import com.codexo.cryptopeak.utils.NetworkUtil
import com.codexo.cryptopeak.viewmodels.MainViewModel
import com.codexo.cryptopeak.viewmodels.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(database, repository)
    }
    private lateinit var binding: ActivityMainBinding

    private lateinit var database: CoinDatabase
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = CoinDatabase.getDatabase(applicationContext)
        repository = Repository(database)

        setupBottomNav()
        checkInternet()
    }

    private fun setupBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.visibility =
                if (destination.id == R.id.detailFragment) View.GONE
                else View.VISIBLE
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.coinFragment,
                R.id.favoriteFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun checkInternet() {
        val networkListener = NetworkUtil(
            ContextCompat.getSystemService(this, ConnectivityManager::class.java)!!
        )

        networkListener.observe(this, {
            if (it) {
                Snackbar.make(binding.navHostFragment, "Connected!", Snackbar.LENGTH_LONG)
                    .setTextColor(Color.GREEN)
                    .show()
            } else {
                Snackbar.make(binding.navHostFragment, "Disconnected!", Snackbar.LENGTH_LONG)
                    .setTextColor(Color.RED)
                    .show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)

        val searchItem = menu.findItem(R.id.search)!!
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = getString(R.string.search_hint)
        searchView.setBackgroundResource(R.drawable.bg_search_view)

        searchView.setOnCloseListener {
            searchItem.collapseActionView()
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.setSearchFilter(newText)
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                searchView.isIconified = false
                searchView.requestFocus()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                searchView.clearFocus()
                viewModel.clearSearchFilter()
                return true
            }
        })

        return true
    }
}
