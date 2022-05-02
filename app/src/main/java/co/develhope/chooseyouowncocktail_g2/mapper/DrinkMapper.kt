package co.develhope.chooseyouowncocktail_g2.mapper

import DrinkDto
import DrinkResult
import DrinkResultModel
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
        return DrinkResultModel(
            Integer.parseInt(idDrink),
            strDrink,
            setByCurrentLanguage(),
            strInstructions,
            strCategory,
            strDrinkThumb,
            false
        )
    }

    fun listToDomainModel(dtoList: DrinkResult): List<DrinkResultModel> {
        val domainList = mutableListOf<DrinkResultModel>()
        dtoList.drinks.forEach {
            domainList.add(it.mapToDomainModel())
        }
        return domainList
    }

}