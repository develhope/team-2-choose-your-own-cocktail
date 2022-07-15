package co.develhope.chooseyouowncocktail_g2.di

import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.network.DrinksProvider
import co.develhope.chooseyouowncocktail_g2.ui.detail.DetailViewModel
import co.develhope.chooseyouowncocktail_g2.ui.home.HomeViewModel
import co.develhope.chooseyouowncocktail_g2.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { DrinksProvider() }
    single { DrinkList() }
}

val viewModels = module {
    viewModel {
        HomeViewModel(drinkList = get())
    }
    viewModel {
        SearchViewModel(drinkList = get())
    }
    viewModel {
        DetailViewModel(drinkList = get())
    }

}