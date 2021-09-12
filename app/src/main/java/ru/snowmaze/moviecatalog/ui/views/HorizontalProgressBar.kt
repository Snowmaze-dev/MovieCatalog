package ru.snowmaze.moviecatalog.ui.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ProgressBar
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import ru.snowmaze.moviecatalog.R
import ru.snowmaze.moviecatalog.colorAttr

class HorizontalProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ProgressBar(context, attrs, android.R.attr.progressBarStyleHorizontal) {

    private var progressAnimation: Animator? = null
    val isAnimatingProgress get() = progressAnimation != null

    init {
        isIndeterminate = false
        max = 1000

        progressDrawable = LayerDrawable(
            arrayOf(
                ColorDrawable(Color.TRANSPARENT),
                ColorDrawable(Color.TRANSPARENT),
                ScaleDrawable(
                    ColorDrawable(context.colorAttr(R.attr.colorSecondary)),
                    Gravity.START,
                    1f,
                    -1f
                )
            )
        ).apply {
            setId(0, android.R.id.background)
            setId(1, android.R.id.secondaryProgress)
            setId(2, android.R.id.progress)
        }
    }

    fun animateProgressToEnd(duration: Long = 2000, onEnd: () -> Unit = {}) {
        animateProgressTo(max, duration = duration, onEnd = onEnd)
    }

    private fun animateProgressTo(targetProgress: Int, duration: Long, onEnd: () -> Unit) {
        isVisible = true
        progressAnimation?.cancel()
        progressAnimation = ValueAnimator.ofInt(progress, targetProgress).apply {
            addUpdateListener {
                progress = it.animatedValue as Int
            }
            doOnEnd {
                progressAnimation = null
                onEnd()
            }
            setDuration(duration)
            start()
        }
    }
}