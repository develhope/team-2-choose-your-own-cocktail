package co.develhope.chooseyouowncocktail_g2.model

import android.graphics.drawable.Drawable

data class Beer(
    val id: Int,
    val name: String,
    val description: String,
    val shortDescription: String,
    val sizeInCl: Int,
    val img: Int,
    val favourite: Boolean
)