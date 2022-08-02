package co.develhope.chooseyouowncocktail_g2.ui.profile

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*


const val USER_KEY = "local_user"

class ProfileViewModel(val preferences: SharedPreferences) : ViewModel() {

    lateinit var user: User

    init {
        if (preferences.contains(USER_KEY)) {
            user = getLocalUser()
        }
    }

    private fun getLocalUser(): User {
        return Gson().fromJson(
            preferences.getString(USER_KEY, null),
            object : TypeToken<User>() {}.type
        )
    }

    fun isUserInitialized(): Boolean {
        return ::user.isInitialized
    }

    fun saveLocalUser(user: User?) {
        preferences.edit().putString(
            USER_KEY,
            Gson().toJson(user)
        ).apply()
    }

    fun loadImageFromStorage(context: Context): Bitmap? {
        return try {
            val directory =  context.getDir("user", Context.MODE_PRIVATE)
            val f = File(directory, "profile.jpg")
            BitmapFactory.decodeStream(FileInputStream(f))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            println("null")
            null
        }
    }

    fun saveToInternalStorage(context: Context,bitmapImage: Bitmap): String? {

        val directory =  context.getDir("user", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profile.jpg")
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