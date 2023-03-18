package ru.shipa.app.extension

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Drawable.tint(context: Context, @ColorRes color: Int): Drawable {
    return DrawableCompat.wrap(this).mutate().apply {
        DrawableCompat.setTint(this, ContextCompat.getColor(context, color))
    }
}

fun Int.tint(context: Context, @ColorRes color: Int): Drawable {
    return requireNotNull(AppCompatResources.getDrawable(context, this)).tint(context, color)
}
