package com.codexo.cryptopeak.adapters

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.utils.ICON_URL
import java.util.*

class BindingAdapter {
    companion object {
        @BindingAdapter("image_url")
        @JvmStatic
        fun bindImage(imgView: ImageView, imgUrl: String?) {
            val url =
                "$ICON_URL${imgUrl?.lowercase(Locale.getDefault())}@2x.png"
            url.let {
                val imgUri = url.toUri().buildUpon().scheme("https").build()
                Glide.with(imgView.context)
                    .load(imgUri)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(imgView)
            }
        }

        @BindingAdapter("android:is_favorite")
        @JvmStatic
        fun isFavorite(img: ImageView, marked: Boolean) {
            if (marked) {
                img.setImageResource(R.drawable.ic_favorite_on)
            } else img.setImageResource(R.drawable.ic_favorite_off)
        }
    }
}