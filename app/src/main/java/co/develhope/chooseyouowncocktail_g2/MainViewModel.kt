package co.develhope.chooseyouowncocktail_g2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.develhope.chooseyouowncocktail_g2.model.DBViewModel


class MainViewModel(private val githubProvider: DrinksDB) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DBViewModel::class.java)) {
            return DBViewModel(githubProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}