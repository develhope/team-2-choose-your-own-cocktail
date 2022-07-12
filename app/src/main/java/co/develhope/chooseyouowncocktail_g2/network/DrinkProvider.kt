package co.develhope.chooseyouowncocktail_g2.network

import co.develhope.chooseyouowncocktail_g2.network.dto.DrinksResult
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val thecocktaildb= "https://www.thecocktaildb.com/api.php/"

class DrinksProvider {

    private val gsonBuilder = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit =
        Retrofit.Builder().baseUrl(thecocktaildb).addConverterFactory(
            GsonConverterFactory.create(gsonBuilder)
        ).build()

    suspend fun searchByFirstLetter(letter: Char): DrinksResult {
        return retrofit.create(DrinkService::class.java)
            .getDrinkByFirstLetter(letter)

    }

}