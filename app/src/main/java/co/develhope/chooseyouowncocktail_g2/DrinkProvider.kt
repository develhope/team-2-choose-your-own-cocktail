package co.develhope.chooseyouowncocktail_g2

import DrinkResult
import co.develhope.chooseyouowncocktail_g2.domain.DrinkMapper
import co.develhope.chooseyouowncocktail_g2.model.domainmodel.drinks.Drink
import co.develhope.chooseyouowncocktail_g2.network.DrinkService
import co.develhope.chooseyouowncocktail_g2.network.RestClient
import java.util.*

enum class Alchool {
    Alcoholic, Non_Alcoholic
}


object DrinksProvider {


    suspend fun filterByAlcoholic(alchool: Alchool): List<Drink> {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkService::class.java)
                    .getDrinksByAlcoholic(alchool.toString())
            )
        )
    }

    suspend fun filterByCategory(category: String): List<Drink> {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkService::class.java)
                    .getDrinkByCategory(category)
            )
        )
    }

    suspend fun searchByFirstLetter(letter: Char): List<Drink> {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkService::class.java)
                    .getDrinkByFirstLetter(letter)
            )
        )
    }

    suspend fun searchByName(name: String): List<Drink> {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkService::class.java)
                    .getDrinkByName(name)
            )
        )
    }

    suspend fun searchByIngredient(ingredient: String): List<Drink> {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkService::class.java)
                    .getDrinkByIngredients(ingredient)
            )
        )
    }

    suspend fun getByID(id: Int): List<Drink> {
        return DrinkMapper.listToDomainModel(
            RestClient.getClient().create(DrinkService::class.java)
                .getDrinkByID(id)
        )
    }

    suspend fun getRandom(): List<Drink> {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkService::class.java)
                    .getDrinkRandom()
            )
        )
    }

    suspend fun getByIDList(idList: DrinkResult): DrinkResult {
        val genList = DrinkResult(mutableListOf())
        idList.drinks.forEach {
            val result = RestClient.getClient().create(DrinkService::class.java)
                .getDrinkByID(Integer.parseInt(it.idDrink))
            genList.drinks.add(result.drinks[0])
        }
        return genList
    }

    suspend fun getListAlphabetically(): List<Drink> {
        val genList = mutableListOf<Drink>()
        val char = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
        char.forEach {
            try {
                val letterList = searchByFirstLetter(it)
                if (!letterList.isNullOrEmpty()) {
                    letterList.forEach {
                        genList.add(it)
                        // println(it.name)
                    }
                }
            } catch (e: Exception) {
                println("error getListAlphabetically $e")
            }
        }
        return genList
    }

}