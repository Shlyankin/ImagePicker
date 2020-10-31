package com.example.imagepicker.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible


object Animations {

    fun hideView(view: View) {
        view.animate()
            .translationY(0.0f)
            .alpha(0.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    view.isVisible = false
                }
            })
    }

    fun slide(view: View, height: Int, toVisible: Boolean) {
        if (toVisible) {
            slideUp(view, height)
        } else {
            slideDown(view, height)
        }
    }

    fun slideUp(view: View, height: Int) {
        view.isVisible = true
        val animate = TranslateAnimation(
            0.0f,  // fromXDelta
            0.0f,  // toXDelta
            height.toFloat(),  // fromYDelta
            0.0f
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    fun slideDown(view: View, height: Int) {
        val animate = TranslateAnimation(0.0f, 0.0f, 0.0f, height.toFloat())
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    fun showView(view: View) {
        view.animate()
            .translationY(0.0f)
            .alpha(0.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    view.isVisible = false
                }
            })
    }

    fun decreaseSize(view: View, width: Int? = null, height: Int? = null): Animation {
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                height?.let {
                    view.layoutParams.height =
                        if (interpolatedTime == 1f) height
                        else height + ((view.height - height) * (1f - interpolatedTime)).toInt()
                }
                width?.let {
                    view.layoutParams.width =
                        if (interpolatedTime == 1f) width
                        else width + ((view.width - width) * (1f - interpolatedTime)).toInt()
                }
                view.requestLayout()
            }
        }
        animation.duration = 500
        view.startAnimation(animation)
        return animation
    }

    fun increaseSize(view: View, width: Int? = null, height: Int? = null): Animation {
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                height?.let {
                    view.layoutParams.height =
                        if (interpolatedTime == 1f) height
                        else view.height + ((height - view.height) * interpolatedTime).toInt()

                }
                width?.let {
                    view.layoutParams.width =
                        if (interpolatedTime == 1f) width
                        else view.width + ((width - view.width) * interpolatedTime).toInt()
                }
                view.requestLayout()
            }
        }
        animation.duration = 500
        view.startAnimation(animation)
        return animation
    }

    fun encollapse(view: View, height: Int) {
        view.visibility = View.VISIBLE
        val actualHeight = view.measuredHeight
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                if (interpolatedTime == 1f) {
                    view.layoutParams.height = height
                } else {
                    view.layoutParams.height = actualHeight + (height * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }
        }
        animation.duration = (height / view.context.resources.displayMetrics.density).toLong()
        view.startAnimation(animation)
    }

    fun collapse(view: View) {
        val actualHeight = view.measuredHeight
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                if (interpolatedTime == 1f) {
                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height =
                        actualHeight - (actualHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }
        }
        animation.duration = (actualHeight / view.context.resources
            .displayMetrics.density).toLong()
        view.startAnimation(animation)
    }
}