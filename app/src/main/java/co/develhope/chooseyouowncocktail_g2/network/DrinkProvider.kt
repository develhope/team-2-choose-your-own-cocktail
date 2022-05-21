package co.develhope.chooseyouowncocktail_g2

import co.develhope.chooseyouowncocktail_g2.domain.DrinkMapper
import co.develhope.chooseyouowncocktail_g2.model.domainmodel.drinks.Drink
import co.develhope.chooseyouowncocktail_g2.network.DrinkService
import co.develhope.chooseyouowncocktail_g2.network.RestClient


object DrinksProvider {

    private val retrofitClient = RestClient.getClient()


    suspend fun searchByFirstLetter(letter: Char): List<Drink> {
        return DrinkMapper.listToDomainModel(
                retrofitClient.create(DrinkService::class.java)
                    .getDrinkByFirstLetter(letter)
        )
    }

}