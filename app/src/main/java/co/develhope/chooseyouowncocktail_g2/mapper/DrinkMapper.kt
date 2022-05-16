package co.develhope.chooseyouowncocktail_g2.mapper

import DrinkDto
import DrinkResult
import DrinkResultModel
import android.util.Log
import co.develhope.chooseyouowncocktail_g2.DrinksDB
import co.develhope.chooseyouowncocktail_g2.DrinksDB.setList
import java.util.*

object DrinkMapper {

    fun DrinkDto.setByCurrentLanguage(): String {
        return when (Locale.getDefault().displayLanguage) {
            "English" -> this.strInstructions
            "Italian" -> this.strInstructionsIT
            "Deutsche" -> this.strInstructionsDE
            "French" -> this.strInstructionsFR
            else -> {
                this.strInstructions
            }
        }
    }

    fun DrinkDto.mapToDomainModel(): DrinkResultModel {

        val ingrList = listOf(
            this.strIngredient1,
            this.strIngredient2,
            this.strIngredient3,
            this.strIngredient4,
            this.strIngredient5,
            this.strIngredient6,
            this.strIngredient7,
            this.strIngredient8,
            this.strIngredient9,
            this.strIngredient10,
            this.strIngredient11,
            this.strIngredient12,
            this.strIngredient13,
            this.strIngredient14,
            this.strIngredient15,
        )

        return DrinkResultModel(
            Integer.parseInt(idDrink),
            strDrink,
            setByCurrentLanguage(),
            strInstructions,
            strCategory,
            ingrList,
            strDrinkThumb,
            false
        )
    }

    fun listToDomainModel(dtoList: DrinkResult): List<DrinkResultModel> {
        return dtoList.drinks.map { it.mapToDomainModel() }
    }

}