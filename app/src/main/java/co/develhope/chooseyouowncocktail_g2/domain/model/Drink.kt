package co.develhope.chooseyouowncocktail_g2.model.domainmodel.drinks

import android.graphics.drawable.Drawable

data class Drink(
    val id: Int,
    val name: String?,
    val description: String?,
    val shortDescription: String?,
    val category: String?,
    val ingredients: List<String?>,
    val img: String?,
    val favourite: Boolean
)