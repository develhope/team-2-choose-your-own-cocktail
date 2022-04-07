package com.example.chooseyouowncocktail_g2

import android.graphics.drawable.Drawable

data class Beer(
    val name: String,
    val description: String,
    val shortDescription: String,
    var img: Drawable?
)

object DrinkList {

    private var beers = mutableListOf(
        Beer(
            "Ichnusa",
            "Una birra a bassa fermentazione con una gradazione alcolica di 4,7% vol." +
                    "Il colore è giallo tenue, quasi dorato, con una leggera effervescenza , limpida senza residui sul fondo." +
                    " Versata nel bicchiere ha una schiuma densa, bianca e consistente.",
            "Una birra a bassa fermentazione con una gradazione alcolica di 4,7% vol.",
            null
        ),
        Beer(
            "Dreher",
            "La Dreher è una birra a bassa fermentazione, quindi Lager." +
                    "Ha un colore giallo dorato, quando la versi nel bicchiere" +
                    "sprigiona una schiuma fine, persistente e compatta allo stesso tempo.Una birra leggera," +
                    "delicata con aromi di luppolo e cereali e note finali di miele.",
            "La Dreher è una birra a bassa fermentazione, quindi Lager.",
            null
        ),
        Beer(
            "Ichnusa",
            "La Nastro Azzurro è una Pilsner con una gradazione di 5.1%." +
                    "Il gusto di luppolo è gradevolmente amara, con una nota agrumata che la rende inconfondibile." +
                    "Versata nel bicchiere appare di un bel giallo paglierino limpido," +
                    "con una schiuma corposa e un deciso aroma di cereali e lieviti.",
            "La Nastro Azzurro è una Pilsner con una gradazione di 5.1%.",
            null
        )
    )

    fun getBeerList(): MutableList<Beer> {
        return beers
    }

    fun getName(id: Int): String {
        return beers[id].name
    }

    fun getDesc(id: Int): String {
        return beers[id].description
    }

    fun getShortDesc(id: Int): String {
        return beers[id].shortDescription
    }

    fun setPreview(id: Int, img: Drawable) {
        beers[id].img = img
    }

}