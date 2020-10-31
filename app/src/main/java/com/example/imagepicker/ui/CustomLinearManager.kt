package com.example.imagepicker.ui

import android.content.Context
import android.graphics.PointF
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView


class SmoothLinearManager(
    private val mContext: Context,
    @RecyclerView.Orientation orientation: Int = HORIZONTAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(mContext, orientation, reverseLayout) {
    companion object {
        private const val MILLISECONDS_PER_INCH = 50f
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State, position: Int
    ) {
        val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(mContext) {
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@SmoothLinearManager
                    .computeScrollVectorForPosition(targetPosition)
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

}