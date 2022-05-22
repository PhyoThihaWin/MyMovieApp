package com.pthw.mymovieapp.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.pthw.mymovieapp.R
import com.pthw.mymovieapp.databinding.ActivityMainBinding
import com.pthw.mymovieapp.utils.toGone
import com.pthw.mymovieapp.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val graph = navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph)

        navHostFragment.navController.graph = graph


        binding.btnNavHome.setupWithNavController(navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.homeFragment, R.id.browseFragment, R.id.profileFragment -> binding.btnNavHome.toVisible()
                else -> binding.btnNavHome.toGone()
            }
        }



    }

}