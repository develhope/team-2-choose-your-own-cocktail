package co.develhope.chooseyouowncocktail_g2

import android.widget.ImageView
import com.squareup.picasso.Picasso


fun ImageView.setImageByUrl(url: String?, width: Int, height: Int) {
    Picasso.get()
        .load(url)
        .resize(width,height)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .into(this)

}