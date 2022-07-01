package co.develhope.chooseyouowncocktail_g2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.develhope.chooseyouowncocktail_g2.ui.detail.DetailViewModel
import co.develhope.chooseyouowncocktail_g2.ui.home.HomeViewModel
import co.develhope.chooseyouowncocktail_g2.ui.search.SearchViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        HomeViewModel::class.java -> HomeViewModel() as T
        SearchViewModel::class.java -> SearchViewModel() as T
        DetailViewModel::class.java -> DetailViewModel() as T
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    }
}
