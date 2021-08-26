package com.codexo.cryptopeak.adapters

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codexo.cryptopeak.R
import com.codexo.cryptopeak.database.CoinData
import com.codexo.cryptopeak.ui.CoinFragmentDirections
import com.codexo.cryptopeak.ui.FavoriteFragmentDirections
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

        @BindingAdapter(value = ["open_detail", "navigate_from"], requireAll = true)
        @JvmStatic
        fun navigateToDetail(view: ConstraintLayout, currentItem: CoinData, navigateFrom: String) {
            val action = when (navigateFrom){
                "coinFragment" -> CoinFragmentDirections.actionCoinFragmentToDetailFragment(currentItem)
                "favoriteFragment" -> FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(currentItem)
                else -> return
            }
            view.setOnClickListener {view.findNavController().navigate(action) }
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