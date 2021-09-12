package ru.snowmaze.moviecatalog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Drawable?.createRippleDrawable(touchColor: Int = Color.GRAY): Drawable {
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_pressed)
        ),
        intArrayOf(
            touchColor,
            touchColor
        )
    )
    return RippleDrawable(colorStateList, this, null)
}

fun Context.colorAttr(
    @AttrRes attrColor: Int,
    resolveRefs: Boolean = true
) = TypedValue().apply {
    theme.resolveAttribute(attrColor, this, resolveRefs)
}.data