package com.oneseven.codetest.viewmodel

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.oneseven.codetest.R

@BindingAdapter("android:imageUrl")
fun ImageView.setImageUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.account_circle)
        .error(R.drawable.account_circle)
        .into(this)
}
