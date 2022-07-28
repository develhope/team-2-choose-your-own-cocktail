package co.develhope.chooseyouowncocktail_g2.di

import android.content.Context
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.adapter.HeaderAdapter
import co.develhope.chooseyouowncocktail_g2.network.DrinksProvider
import co.develhope.chooseyouowncocktail_g2.ui.add.AddViewModel
import co.develhope.chooseyouowncocktail_g2.ui.detail.DetailViewModel
import co.develhope.chooseyouowncocktail_g2.ui.home.HomeViewModel
import co.develhope.chooseyouowncocktail_g2.ui.profile.ProfileViewModel
import co.develhope.chooseyouowncocktail_g2.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { DrinksProvider() }
    single { HeaderAdapter() }
    single { DrinkList(preferences = get()) }
    single { androidContext().getSharedPreferences("app", Context.MODE_PRIVATE) }
}

val viewModels = module {
    viewModel {
        HomeViewModel(drinkList = get(), preferences = get(), headerAdapter = get() )
    }
    viewModel {
        SearchViewModel(drinkList = get(), preferences = get())
    }
    viewModel {
        DetailViewModel(drinkList = get())
    }
    viewModel {
        ProfileViewModel(preferences = get())
    }
    viewModel {
        AddViewModel(preferences = get(),drinkList = get())
    }
}