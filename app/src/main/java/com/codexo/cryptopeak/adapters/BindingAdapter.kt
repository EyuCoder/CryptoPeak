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

        @BindingAdapter("open_detail")
        @JvmStatic
        fun openDetail(view: ConstraintLayout, currentItem: CoinData) {
            view.setOnClickListener {
                val action = CoinFragmentDirections.actionCoinFragmentToDetailFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }
    }
}