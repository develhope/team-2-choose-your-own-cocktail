package co.develhope.chooseyouowncocktail_g2.network

import co.develhope.chooseyouowncocktail_g2.network.dto.DrinksResult
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinkService {

    @GET("/api/json/v1/1/lookup.php")
    suspend fun getDrinkByID(@Query("i") drinkID: Int): DrinksResult

    @GET("/api/json/v1/1/lookup.php")
    suspend fun getDrinkByIngredients(@Query("iid") ingredient: String): DrinksResult

    @GET("/api/json/v1/1/search.php")
    suspend fun getDrinkByName(@Query("s") drinkName: String): DrinksResult

    @GET("/api/json/v1/1/search.php")
    suspend fun getDrinkByFirstLetter(@Query("f") drinkName: Char): DrinksResult

    @GET("/api/json/v1/1/filter.php")
    suspend fun getDrinksByAlcoholic(@Query("a") alcoholic: String): DrinksResult

    @GET("/api/json/v1/1/filter.php")
    suspend fun getDrinkByCategory(@Query("c") category: String): DrinksResult

    @GET("/api/json/v1/1/random.php")
    suspend fun getDrinkRandom(): DrinksResult


}