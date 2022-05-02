package co.develhope.chooseyouowncocktail_g2.network

import DrinkDto
import DrinkResult
import DrinkResultModel
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinkDao {

    @GET("https://www.thecocktaildb.com/api/json/v1/1/lookup.php")
    suspend fun getDrinkByID(@Query("i") drinkID: Int): DrinkResult

    @GET("https://www.thecocktaildb.com/api/json/v1/1/lookup.php")
    suspend fun getDrinkByIngredients(@Query("iid") ingredient: String): DrinkResult

    @GET("https://www.thecocktaildb.com/api/json/v1/1/search.php")
    suspend fun getDrinkByName(@Query("s") drinkName: String): DrinkResult

    @GET("https://www.thecocktaildb.com/api/json/v1/1/search.php")
    suspend fun getDrinkByFirstLetter(@Query("f") drinkName: Char): DrinkResult

    @GET("https://www.thecocktaildb.com/api/json/v1/1/filter.php")
    suspend fun getDrinksByAlcoholic(@Query("a") alcoholic: String): DrinkResult

    @GET("https://www.thecocktaildb.com/api/json/v1/1/filter.php")
    suspend fun getDrinkByCategory(@Query("c") category: String): DrinkResult

    @GET("https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Ordinary_Drink")
    suspend fun getDrinkRandom(): DrinkResult


}