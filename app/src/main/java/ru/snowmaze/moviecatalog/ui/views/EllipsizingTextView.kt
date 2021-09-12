package ru.snowmaze.moviecatalog.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.text.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpanned
import ru.snowmaze.moviecatalog.R

// кастомная TextView, чтобы текст не обрезался, а ставились 3 точки в конце
// источник - https://stackoverflow.com/questions/2160619/android-ellipsize-multiline-textview
class EllipsizingTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    var isEllipsized = false
        private set
    private var isStale = false
    private var programmaticChange = false
    private var fullText: Spanned? = null
    private var maxLines = 0
    private var lineAdditionalVerticalPadding = 0.0f

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        this.maxLines = maxLines
        isStale = true
    }

    override fun getMaxLines(): Int {
        return maxLines
    }

    fun ellipsizingLastFullyVisibleLine(): Boolean {
        return maxLines == Int.MAX_VALUE
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        lineAdditionalVerticalPadding = add
        super.setLineSpacing(add, mult)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (!programmaticChange) {
            fullText = text?.toSpanned()
            isStale = true
        }
        super.setText(text, type)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (ellipsizingLastFullyVisibleLine()) {
            isStale = true
        }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        if (ellipsizingLastFullyVisibleLine()) {
            isStale = true
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (isStale) {
            resetText()
        }
        super.onDraw(canvas)
    }

    private fun resetText() {
        var workingText = fullText ?: ("".toSpanned())
        var ellipsized = false
        val layout: Layout = createWorkingLayout(workingText)
        val linesCount = linesCount
        if (layout.lineCount > linesCount) {
            // We have more lines of text than we are allowed to display.
            workingText = workingText.subSequence(0, layout.getLineEnd(linesCount - 1)) as Spanned
            while (createWorkingLayout(
                    TextUtils.concat(
                        workingText.trimEnd(),
                        ELLIPSIS
                    ) as Spanned
                ).lineCount > linesCount
            ) {
                val lastSpace: Int = workingText.toString().lastIndexOf(' ')
                if (lastSpace == -1) {
                    break
                }
                workingText = workingText.subSequence(0, lastSpace) as Spanned
            }
            workingText = TextUtils.concat(workingText.trimEnd(), ELLIPSIS) as Spanned
            ellipsized = true
        }
        if (workingText != text) {
            programmaticChange = true
            try {
                text = workingText
            } finally {
                programmaticChange = false
            }
        }
        isStale = false
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized
        }
    }

    /**
     * Get how many lines of text we are allowed to display.
     */
    private val linesCount: Int
        get() = if (ellipsizingLastFullyVisibleLine()) {
            val fullyVisibleLinesCount = fullyVisibleLinesCount
            if (fullyVisibleLinesCount == -1) {
                1
            } else {
                fullyVisibleLinesCount
            }
        } else {
            maxLines
        }

    /**
     * Get how many lines of text we can display so their full height is visible.
     */
    private val fullyVisibleLinesCount: Int
        get() {
            val layout: Layout = createWorkingLayout(SpannedString(""))
            val height: Int = height - paddingTop - paddingBottom
            val lineHeight: Int = layout.getLineBottom(0)
            return height / lineHeight
        }

    private fun createWorkingLayout(workingText: Spanned?): Layout {
        val text = workingText ?: ("".toSpanned())
        return StaticLayout.Builder.obtain(
            text,
            0,
            text.length,
            paint,
            width - paddingLeft - paddingRight
        ).build()
    }

    override fun setEllipsize(where: TextUtils.TruncateAt?) {
        // Ellipsize settings are not respected
    }

    companion object {
        private val ELLIPSIS: Spanned = SpannedString("…")
    }

    init {
        super.setEllipsize(null)
        val a: TypedArray = context.obtainStyledAttributes(attrs, intArrayOf(R.attr.maxLines))
        setMaxLines(a.getInt(0, Int.MAX_VALUE))
        a.recycle()
    }
}