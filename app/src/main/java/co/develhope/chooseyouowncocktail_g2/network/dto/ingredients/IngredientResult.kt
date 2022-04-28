package co.develhope.chooseyouowncocktail_g2.dto.ingredients

import com.google.gson.annotations.SerializedName

data class IngredientResult (

	@SerializedName("idIngredient") val idIngredient : Int,
	@SerializedName("strIngredient") val strIngredient : String,
	@SerializedName("strDescription") val strDescription : String,
	@SerializedName("strType") val strType : String,
	@SerializedName("strAlcohol") val strAlcohol : String,
	@SerializedName("strABV") val strABV : Int
)