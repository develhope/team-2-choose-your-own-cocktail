package co.develhope.chooseyouowncocktail_g2

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {

    private val gsonBuilder = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit =
        Retrofit.Builder().baseUrl("https://www.thecocktaildb.com/api.php").addConverterFactory(
            GsonConverterFactory.create(gsonBuilder)
        ).build()

    fun getClient(): Retrofit {
        return retrofit
    }

}