package co.develhope.chooseyouowncocktail_g2.dto.ingredients

import com.google.gson.annotations.SerializedName

data class Ingredients (

	@SerializedName("ingredients") val ingredients : List<IngredientResult>
)