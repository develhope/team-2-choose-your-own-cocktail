package co.develhope.chooseyouowncocktail_g2

import android.app.Activity
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.ActivityMainBinding
import co.develhope.chooseyouowncocktail_g2.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onBackPressed() {
        val previus = findNavController(R.id.nav_host_fragment_activity_main).previousBackStackEntry?.destination?.id
        if (previus != null) {
            findNavController(R.id.nav_host_fragment_activity_main).navigate(previus)
        } else{
            super.onBackPressed()
        }
    }
}