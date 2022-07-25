package co.develhope.chooseyouowncocktail_g2

import android.graphics.drawable.Drawable
import android.net.Uri

data class User(
    val profilePic: String,
    val name: String,
    val birth: String,
    val gender: Boolean,
    val tel: String,
    val email: String
)