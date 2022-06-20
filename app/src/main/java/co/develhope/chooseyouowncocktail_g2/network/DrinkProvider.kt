package co.develhope.chooseyouowncocktail_g2.network

import co.develhope.chooseyouowncocktail_g2.domain.DrinkMapper
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink


class DrinksProvider {

    private val retrofitClient = RestClient.getClient()


    suspend fun searchByFirstLetter(letter: Char): List<Drink> {
        return DrinkMapper.listToDomainModel(
                retrofitClient.create(DrinkService::class.java)
                    .getDrinkByFirstLetter(letter)
        )
    }

}