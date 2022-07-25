package co.develhope.chooseyouowncocktail_g2.ui.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken




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


}