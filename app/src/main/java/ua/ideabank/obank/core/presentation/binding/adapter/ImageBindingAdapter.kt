package ua.ideabank.obank.core.presentation.binding.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object ImageBindingAdapter {
	@SuppressLint("CheckResult")
	@JvmStatic
	@BindingAdapter(
		value = [
			"android:bind_img_uri",
			"android:bind_img_res_id",
			"android:bind_img_placeholder",
			"android:bind_img_fallback",
			"android:bind_img_error",
			"android:bind_img_rounded",
			"android:bind_img_animated_placeholder",
			"android:bind_img_need_cache"
		], requireAll = false
	)
	fun setImage(
		imageView: ImageView,
		uri: String?,
		resId: Int?,
		placeHolder: Drawable?,
		fallback: Drawable?,
		error: Drawable?,
		isImageRounded: Boolean?,
		animatedPlaceHolder: Boolean?,
		needCache: Boolean?
	) {
		var circularProgressDrawable: CircularProgressDrawable? = null
		if (animatedPlaceHolder == true) {
			circularProgressDrawable = CircularProgressDrawable(imageView.context)
			circularProgressDrawable.strokeWidth = 5f
			circularProgressDrawable.centerRadius = 30f
			circularProgressDrawable.start()
		}
		Glide
			.with(imageView.context)
			.load(uri ?: resId)
			.diskCacheStrategy(if (needCache == true) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)
			.thumbnail(0.1f)
			.apply { if (isImageRounded == true) apply(RequestOptions.circleCropTransform()) }
			.placeholder(circularProgressDrawable ?: placeHolder)
			.fallback(if (uri == null) null else fallback)
			.error(if (uri == null) null else error ?: fallback)
			.into(imageView)
	}

	@JvmStatic
	@BindingAdapter("android:bind_src")
	fun setSrcVector(imageView: ImageView, @DrawableRes drawable: Int) {
		imageView.setImageResource(drawable)
	}

	@JvmStatic
	@BindingAdapter("android:bind_im_color")
	fun setImColor(imageView: ImageView, @DrawableRes color: Int) {
		imageView.setColorFilter(color)
	}
}