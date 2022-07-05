package co.develhope.chooseyouowncocktail_g2.network

import co.develhope.chooseyouowncocktail_g2.network.dto.DrinksResult


class DrinksProvider {

    private val retrofitClient = RestClient.getClient()

    suspend fun searchByFirstLetter(letter: Char): DrinksResult {
        return retrofitClient.create(DrinkService::class.java)
            .getDrinkByFirstLetter(letter)

    }

}