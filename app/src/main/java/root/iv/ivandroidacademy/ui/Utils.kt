package root.iv.ivandroidacademy.ui

import android.graphics.drawable.Drawable
import android.widget.ImageView
import coil.ImageLoader
import coil.imageLoader
import coil.load
import coil.request.Disposable
import coil.request.ImageRequest
import coil.target.Target

inline fun ImageView.loadBackground(
    data: Any?,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = this.load(data, imageLoader) {
    target(object : Target {
            override fun onError(error: Drawable?) {
                this@loadBackground.background = null
            }

            override fun onStart(placeholder: Drawable?) {
                this@loadBackground.background = placeholder
            }

            override fun onSuccess(result: Drawable) {
                this@loadBackground.background = result
            }
        })
    this.apply(builder)
}