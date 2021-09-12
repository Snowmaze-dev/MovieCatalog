package ru.snowmaze.moviecatalog.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import ru.snowmaze.moviecatalog.R
import ru.snowmaze.moviecatalog.createRippleDrawable
import ru.snowmaze.moviecatalog.databinding.ViewStubImageTextBinding

class MoviesStubView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var imageTextStubBinding: ViewStubImageTextBinding? = null
    private var button: View? = null

    fun showProgress() {
        clearStub()
        addView(ProgressBar(context).apply {
            minimumHeight = resources.getDimensionPixelSize(R.dimen.dp28)
        })
    }

    fun showNothingFound(query: String) {
        val binding = getStubImageTextBinding()
        binding.stubImage.setImageResource(R.drawable.ic_big_search)
        binding.stubText.text =
            resources.getString(R.string.nothing_found, query)
    }

    fun showRequestError(onRetryClick: () -> Unit) {
        val binding = getStubImageTextBinding(showButton = true)
        binding.stubImage.setImageResource(R.drawable.ic_alert_triangle)
        binding.stubText.text = resources.getString(R.string.request_error_retry)
        button = ImageView(context).apply {
            setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.ic_refresh_button)
                    .createRippleDrawable()
            )
            isFocusable = true
            isClickable = true
            setOnClickListener {
                onRetryClick()
            }
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            }
            addView(this)
        }
        bringToFront()
    }

    private fun getStubImageTextBinding(showButton: Boolean = false): ViewStubImageTextBinding {
        if (imageTextStubBinding == null) {
            button = null
            clearStub()
            inflate(context, R.layout.view_stub_image_text, this)
        }
        if (!showButton) {
            button?.let {
                removeView(it)
            }
            button = null
        }
        val binding = imageTextStubBinding
            ?: ViewStubImageTextBinding.bind(findViewById(R.id.stub_main_layout))
        binding.root.updateLayoutParams<LayoutParams> {
            gravity = Gravity.CENTER
        }
        imageTextStubBinding = binding
        return binding
    }

    fun clearStub() {
        imageTextStubBinding = null
        button = null
        if (childCount != 0) {
            removeAllViews()
        }
    }

}