package co.develhope.chooseyouowncocktail_g2.network

import co.develhope.chooseyouowncocktail_g2.network.dto.DrinksResult
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinkService {

    @GET("/api/json/v1/1/search.php")
    suspend fun getDrinkByFirstLetter(@Query("f") drinkName: Char): DrinksResult

}