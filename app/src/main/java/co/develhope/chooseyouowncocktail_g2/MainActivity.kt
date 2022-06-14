package co.develhope.chooseyouowncocktail_g2


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.develhope.chooseyouowncocktail_g2.databinding.ActivityMainBinding
import co.develhope.chooseyouowncocktail_g2.domain.DBEvent
import co.develhope.chooseyouowncocktail_g2.domain.DBResult
import co.develhope.chooseyouowncocktail_g2.domain.DBViewModel
import co.develhope.chooseyouowncocktail_g2.network.DrinksProvider
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val fragManager = supportFragmentManager


    private val mainViewModelFactory = MainViewModel(DrinksProvider)

    private val viewModel =
        mainViewModelFactory.create(DBViewModel::class.java)

    private val currentLetter = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    private var letterIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        retrieveFromDB()

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
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_SELECTED

    }

    fun retrieveFromDB() {
        viewModel.send(DBEvent.RetrieveDrinksByFirstLetter(currentLetter[letterIndex]))
        observer()
        if (letterIndex < currentLetter.size) {
            letterIndex++
            println("++")
        }
        println(currentLetter[letterIndex])
    }


    private fun observer() {

        viewModel.result.observe(this) {
            when (it) {
                is DBResult.Result -> {

                    DrinkList.addToDrinkList(it.db)
                    //  println(DrinkList.drinkList().size)
                    DrinkList.drinkList().forEach {
                        //   println(it.name)
                    }

                }
                is DBResult.Error -> Snackbar.make(
                    binding.root,
                    "Error retrieving Drinks: $it",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Retry") {
                        viewModel.send(
                            DBEvent.RetrieveDrinksByFirstLetter(
                                currentLetter[letterIndex]
                            )
                        )
                    }
                    .show()
            }
        }
    }

    fun goToFragment(
        fragment: Fragment,
        tag: String
    ) {
        fragment.add(tag)
        //se l'utente cambia fragment dalla bottom navigation bar, chiude il fragment
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (fragment.isVisible) {
                //SOLUZIONE TEMPORANEA!!!!!!
                Handler(Looper.getMainLooper()).postDelayed({
                    remove(fragment)
                }, 200)
                ////////////////////////////
            }
        }
    }

    fun remove(fragment: Fragment) {
        fragManager.beginTransaction()
            .remove(fragment)
            .commit()
    }

    private fun Fragment.add(tag: String) {
        fragManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.container, this, tag).commit()
    }


}