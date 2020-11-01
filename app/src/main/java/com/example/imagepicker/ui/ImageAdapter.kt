package com.example.imagepicker.ui

import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.imagepicker.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_image.view.*
import java.util.*
import java.util.concurrent.TimeUnit

class ImageAdapter(
    private var uriList: List<Uri> = emptyList(),
    private var onClickListener: ((view: View, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val CAMERA_ITEM = 0
        const val IMAGE_ITEM = 1
    }

    private val expandSubject = PublishSubject.create<Boolean>()

    fun submitList(uriList: List<Uri>) {
        this.uriList = uriList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> {
            if (isExpanded) {
                IMAGE_ITEM
            } else {
                CAMERA_ITEM
            }
        }
        1 -> IMAGE_ITEM
        else -> IMAGE_ITEM
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        CAMERA_ITEM -> CameraViewHolder(parent, expandSubject)
        IMAGE_ITEM -> ImageViewHolder(parent, expandSubject)
        else -> ImageViewHolder(parent, expandSubject)
    }

    override fun getItemCount() = uriList.size+ if (isExpanded) 0 else 1

    var isExpanded = false
        private set

    private val itemClickListener: ((view: View, position: Int) -> Unit) =
        { view: View, position: Int ->
            isExpanded = !isExpanded
            val scrollToPosition = position - if (isExpanded) 1 else 0
            onClickListener?.invoke(view, scrollToPosition)
            expandSubject.onNext(isExpanded)
            if (isExpanded) {
                notifyItemRemoved(0)
            } else {
                notifyItemInserted(0)
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!isExpanded) {
            if (holder is CameraViewHolder) {

            } else if (holder is ImageViewHolder) {
                holder.bind(uriList[position - 1], position, itemClickListener)
            }
        } else if (holder is ImageViewHolder){
            holder.bind(uriList[position], position, itemClickListener)
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is ImageViewHolder)
            holder.changeSize(isExpanded)
    }
}

class CameraViewHolder(
    parent: ViewGroup,
    expandObservable: Observable<Boolean>
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_camera, parent, false)
) {

}

class ImageViewHolder(
    parent: ViewGroup,
    expandObservable: Observable<Boolean>
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_image, parent, false)
) {

    private var uri: Uri? = null

    init {
        expandObservable.subscribe { isExpanded ->
            itemView.run {
                if (isAttachedToWindow) {
                    changeSizeSmoother(isExpanded)
                } else {
                    changeSize(isExpanded)
                }
            }
        }
    }

    fun changeSizeSmoother(isExpanded: Boolean) {
        itemView.run {
            if (isExpanded) {
                Animations.increaseSize(this, 600)
            } else {
                Animations.decreaseSize(this, 300)
            }
        }
    }

    fun changeSize(isExpanded: Boolean) {
        itemView.run {
            if (isExpanded) {
                itemView.layoutParams.width = 600
                itemView.requestLayout()
            } else {
                itemView.layoutParams.width = 300
                itemView.requestLayout()
            }
        }
    }

    fun bind(
        uri: Uri, position: Int,
        onClickListener: ((view: View, position: Int) -> Unit)? = null
    ) {
        this.uri = uri
        itemView.run {
            Glide.with(this).load(uri)
                .apply(RequestOptions()
                    .centerCrop()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(600, 900)
                ).into(image_view)
            setOnClickListener {
                onClickListener?.invoke(it, position)
            }
        }
    }
}