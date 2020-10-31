package com.example.imagepicker.ui

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

object Animations {
    fun toggleArrow(view: View, isExpanded: Boolean): Boolean {
        return if (isExpanded) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

    fun increase(view: View, width: Int, height: Int) {
        val animation = this.increaseSize(view, width, height)
        view.startAnimation(animation)
    }

    fun decrease(view: View, width: Int, height: Int) {
        val animation = this.decreaseSize(view, width, height)
        view.startAnimation(animation)
    }

    private fun decreaseSize(view: View, width: Int, height: Int): Animation {
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                view.layoutParams.height =
                    if (interpolatedTime == 1f) height
                    else height + ((view.height - height) * (1f - interpolatedTime)).toInt()

                view.layoutParams.width =
                    if (interpolatedTime == 1f) width
                    else width + ((view.width - width) * (1f - interpolatedTime)).toInt()
                view.requestLayout()
            }
        }
        animation.duration = 1000
        view.startAnimation(animation)
        return animation
    }

    private fun increaseSize(view: View, width: Int, height: Int): Animation {
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                view.layoutParams.height =
                    if (interpolatedTime == 1f) height
                    else view.height + ((height - view.height) * interpolatedTime).toInt()

                view.layoutParams.width =
                    if (interpolatedTime == 1f) width
                    else view.width + ((width - view.width) * interpolatedTime).toInt()
                view.requestLayout()
            }
        }
        animation.duration = 1000
        view.startAnimation(animation)
        return animation
    }

    private fun changeSizeAction(view: View, width: Int, height: Int): Animation {
        view.measure(width, height)
        val actualheight = view.measuredHeight
        view.layoutParams.height = 0
        view.visibility = View.VISIBLE
        val animation: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                view.layoutParams.height =
                    if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (actualheight * interpolatedTime).toInt()
                view.requestLayout()
            }
        }
        animation.duration = (actualheight / view.context.resources
            .displayMetrics.density).toLong()
        view.startAnimation(animation)
        return animation
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