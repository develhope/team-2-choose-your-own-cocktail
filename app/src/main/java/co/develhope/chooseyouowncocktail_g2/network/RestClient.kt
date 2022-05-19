package co.develhope.chooseyouowncocktail_g2.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val thecocktaildb= "https://www.thecocktaildb.com/api.php/"

object RestClient {

    private val gsonBuilder = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit =
        Retrofit.Builder().baseUrl(thecocktaildb).addConverterFactory(
            GsonConverterFactory.create(gsonBuilder)
        ).build()

    fun getClient(): Retrofit {
        return retrofit
    }

}