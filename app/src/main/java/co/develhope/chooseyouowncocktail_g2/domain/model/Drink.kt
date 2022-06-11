package co.develhope.chooseyouowncocktail_g2.domain.model

data class Drink(
    val id: Int,
    val name: String?,
    val description: String?,
    val shortDescription: String?,
    val category: String?,
    val ingredients: List<String?>,
    val img: String?,
    var favourite: Boolean,
    val sortingPosition: Int
)