package co.develhope.chooseyouowncocktail_g2.domain

import co.develhope.chooseyouowncocktail_g2.network.dto.DrinkDto
import co.develhope.chooseyouowncocktail_g2.network.dto.DrinksResult
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import java.util.*

object DrinkMapper {

    var sortPos: Int = 0
    private fun DrinkDto.setByCurrentLanguage(): String {
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

    private fun DrinkDto.mapToDomainModel(): Drink {

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

        return Drink(
            Integer.parseInt(idDrink),
            strDrink,
            setByCurrentLanguage(),
            strInstructions,
            strCategory,
            ingrList,
            strDrinkThumb,
            false,
            incSortId()
        )
    }

    fun listToDomainModel(dtoList: DrinksResult): List<Drink> {
        return dtoList.drinks.map { it.mapToDomainModel() }
    }

    fun incSortId():Int {
        val id = sortPos
        sortPos += 1
        return id

    }
}