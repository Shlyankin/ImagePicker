package com.example.imagepicker.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagepicker.R
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_image.view.*
import java.util.*

class ImageAdapter(
    private var uriList: List<Uri> = emptyList(),
    private var onClickListener: ((view: View, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<ImageViewHolder>() {

    private val expandSubject = PublishSubject.create<Boolean>()

    fun submitList(uriList: List<Uri>) {
        this.uriList = uriList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(parent, expandSubject)

    override fun getItemCount() = uriList.size

    var isExpanded = false
        private set

    private val itemClickListener: ((view: View, position: Int) -> Unit)= { view: View, position: Int ->
        isExpanded = !isExpanded
        onClickListener?.invoke(view, position)
        expandSubject.onNext(isExpanded)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(uriList[position], position, itemClickListener)
    }

    override fun onViewAttachedToWindow(holder: ImageViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.changeSize(isExpanded)
    }
}

class ImageViewHolder(
    parent: ViewGroup,
    expandObservable: Observable<Boolean>
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_image, parent, false)
) {

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
        itemView.run {
            Glide.with(this).load(uri).into(image_view)
            setOnClickListener {
                onClickListener?.invoke(it, position)
            }
        }
    }

}