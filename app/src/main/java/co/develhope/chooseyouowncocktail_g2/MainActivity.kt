package co.develhope.chooseyouowncocktail_g2


import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.develhope.chooseyouowncocktail_g2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val fragManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Action.activity = this

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
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

    fun goToFragment(
        fragment: Fragment
    ) {
        fragment.add()
        //se l'utente cambia fragment dalla bottom navigation bar, chiude il fragment
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (fragment.isVisible) {
                fragment.remove()
            }
        }
    }

    private fun Fragment.remove() {
        fragManager.beginTransaction()
            .remove(this)
            .commit()
    }

    private fun Fragment.add() {
        fragManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.container, this, this.tag).commit()
    }


}