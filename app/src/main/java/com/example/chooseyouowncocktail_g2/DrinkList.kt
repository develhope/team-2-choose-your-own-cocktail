package com.example.chooseyouowncocktail_g2

import com.example.chooseyouowncocktail_g2.model.Beer
import java.util.*


object DrinkList {

    private var beers = listOf(
        Beer(
            "Ichnusa",
            "Una birra a bassa fermentazione con una gradazione alcolica di 4,7% vol." +
                    "Il colore è giallo tenue, quasi dorato, con una leggera effervescenza , limpida senza residui sul fondo." +
                    " Versata nel bicchiere ha una schiuma densa, bianca e consistente.",
            "Una birra a bassa fermentazione con una gradazione alcolica di 4,7% vol.",
            66,
            R.drawable.ichnusaprev,
            false
        ),
        Beer(
            "Dreher",
            "La Dreher è una birra a bassa fermentazione, quindi Lager." +
                    "Ha un colore giallo dorato, quando la versi nel bicchiere" +
                    "sprigiona una schiuma fine, persistente e compatta allo stesso tempo.Una birra leggera," +
                    "delicata con aromi di luppolo e cereali e note finali di miele.",
            "La Dreher è una birra a bassa fermentazione, quindi Lager.",
            33,
            R.drawable.dreherprev,
            false
        ),
        Beer(
            "Nastro Azzurro",
            "La Nastro Azzurro è una Pilsner con una gradazione di 5.1%." +
                    "Il gusto di luppolo è gradevolmente amara, con una nota agrumata che la rende inconfondibile." +
                    "Versata nel bicchiere appare di un bel giallo paglierino limpido," +
                    "con una schiuma corposa e un deciso aroma di cereali e lieviti.",
            "La Nastro Azzurro è una Pilsner con una gradazione di 5.1%.",
            33,
            R.drawable.nastroazzurroprev,
            false
        )
    )

    fun beerList(): List<Beer> {
        return beers
    }

    fun setFavorite(beer: Beer, bool: Boolean) {
        Collections.replaceAll(
            beers,
            beer,
            beer.copy(favourite = bool)
        )
    }

}