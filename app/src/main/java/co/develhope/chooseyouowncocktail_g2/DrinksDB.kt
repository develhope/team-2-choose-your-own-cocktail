package co.develhope.chooseyouowncocktail_g2

import DrinkResult
import DrinkResultModel
import co.develhope.chooseyouowncocktail_g2.mapper.DrinkMapper
import co.develhope.chooseyouowncocktail_g2.network.DrinkDao
import co.develhope.chooseyouowncocktail_g2.network.RestClient
import java.lang.Exception
import java.util.*

enum class Alchool {
    Alcoholic, Non_Alcoholic
}

object DrinksDB {

    private var drinks = listOf<DrinkResultModel>()

    fun drinkList(): List<DrinkResultModel> {
        return drinks
    }

    fun List<DrinkResultModel>.setList() {
        drinks = this
    }


    suspend fun filterByAlcoholic(alchool: Alchool): List<DrinkResultModel>? {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkDao::class.java)
                    .getDrinksByAlcoholic(alchool.toString())
            )
        )
    }

    suspend fun filterByCategory(category: String): List<DrinkResultModel>? {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkDao::class.java)
                    .getDrinkByCategory(category)
            )
        )
    }

    suspend fun searchByFirstLetter(letter: Char): List<DrinkResultModel>? {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkDao::class.java)
                    .getDrinkByFirstLetter(letter)
            )
        )
    }

    suspend fun searchByName(name: String): List<DrinkResultModel>? {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkDao::class.java)
                    .getDrinkByName(name)
            )
        )
    }

    suspend fun searchByIngredient(ingredient: String): List<DrinkResultModel>? {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkDao::class.java)
                    .getDrinkByIngredients(ingredient)
            )
        )
    }

    suspend fun getByID(id: Int): List<DrinkResultModel>? {
        return DrinkMapper.listToDomainModel(
            RestClient.getClient().create(DrinkDao::class.java)
                .getDrinkByID(id)
        )
    }

    suspend fun getRandom(): List<DrinkResultModel>? {
        return DrinkMapper.listToDomainModel(
            getByIDList(
                RestClient.getClient().create(DrinkDao::class.java)
                    .getDrinkRandom()
            )
        )
    }

    suspend fun getByIDList(idList: DrinkResult): DrinkResult {
        val genList = DrinkResult(mutableListOf())
        idList.drinks.forEach {
            val result = RestClient.getClient().create(DrinkDao::class.java)
                .getDrinkByID(Integer.parseInt(it.idDrink))
            genList.drinks.add(result.drinks[0])
        }
        return genList
    }

    suspend fun getListAlphabetically(): List<DrinkResultModel> {
        val genList = mutableListOf<DrinkResultModel>()
        var char = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
        char.forEach {
            try {
                val letterList = searchByFirstLetter(it)
                if (!letterList.isNullOrEmpty()) {
                    letterList.forEach {
                        genList.add(it)
                        println(it.name)
                    }
                }
            } catch (e: Exception) {
                println("error getListAlphabetically $e")
            }
        }
        return genList
    }

    fun setFavorite(Drink: DrinkResultModel, bool: Boolean) {
        Collections.replaceAll(
            drinks,
            Drink,
            Drink.copy(favourite = bool)
        )
    }

}