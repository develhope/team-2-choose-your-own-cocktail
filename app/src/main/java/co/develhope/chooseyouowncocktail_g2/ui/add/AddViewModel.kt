package co.develhope.chooseyouowncocktail_g2.ui.add

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import com.google.gson.Gson
import java.io.*

const val USER_DRINK_KEY = "user_drinks"

class AddViewModel(val preferences: SharedPreferences, val drinkList: DrinkList) : ViewModel() {

    fun saveLocalDrink(drink: Drink) {
        drinkList.addToUserDrinks(drink)
        drinkList.getUserDrinks().forEach { println(it.name) }
        preferences.edit().putString(
            USER_DRINK_KEY,
            Gson().toJson(drinkList.getLocalUserDrinks())
        ).apply()
    }

    fun saveToInternalStorage(context: Context, bitmapImage: Bitmap, drinkName: String): String? {

        val directory = context.getDir("userDrink", Context.MODE_APPEND)
        println(directory)
        println("user-$drinkName.jpg")
        // Create imageDir
        val mypath = File(directory, "user-$drinkName.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }


}