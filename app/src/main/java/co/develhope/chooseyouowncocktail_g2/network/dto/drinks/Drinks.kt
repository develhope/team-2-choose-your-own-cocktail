package co.develhope.chooseyouowncocktail_g2.dto.drinks

import com.google.gson.annotations.SerializedName



data class Drinks (

	@SerializedName("drinks") val drinks : List<DrinkResult>
)