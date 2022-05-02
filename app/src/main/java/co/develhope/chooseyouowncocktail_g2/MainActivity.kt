package co.develhope.chooseyouowncocktail_g2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.develhope.chooseyouowncocktail_g2.DrinksDB.setList
import co.develhope.chooseyouowncocktail_g2.network.DrinkDao
import co.develhope.chooseyouowncocktail_g2.databinding.ActivityMainBinding
import co.develhope.chooseyouowncocktail_g2.mapper.DrinkMapper
import co.develhope.chooseyouowncocktail_g2.network.RestClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


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
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        lifecycleScope.launchWhenResumed {
            try {
                DrinksDB.getRandom().setList()
                //DrinksDB.getByID(12460).setList()
                //DrinksDB.searchByName("Vodka").setList()
                //DrinksDB.searchByName("Almeria").setList()
                //DrinksDB.searchByFirstLetter('a').setList()
                //DrinksDB.filterByCategory("Ordinary Drink").setList()
                // DrinksDB.filterByAlcoholic(Alchool.Alcoholic).setList()
                println(DrinksDB.drinkList().forEach {
                    println(it.id)
                    println(it.name)
                    println(it.description)
                    println(it.category)
                    println(it.img)
                    println("---------------")
                })
            } catch (e: Exception) {
                Log.e("MainActivity", "error retrieving List: $e")
            }
        }

    }
}