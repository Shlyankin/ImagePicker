package com.example.imagepicker.ui

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagepicker.BaseFragment
import com.example.imagepicker.R
import kotlinx.android.synthetic.main.f_file_chooser.*


class FileChooserFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            FileChooserFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val adapter: ImageAdapter by lazy {
        ImageAdapter(
            getAllShownImagesPath()
        ) { view, position ->
            if (adapter.isExpanded) {
                Animations.increaseSize(rv_images, height = 900)
            } else {
                Animations.decreaseSize(rv_images, height = 300)
            }
            rv_images.requestLayout()
            (rv_images.layoutManager as? SmoothLinearManager)?.scrollToPositionWithOffset(position, 50)
            if (adapter.isExpanded) {
                Animations.collapse(file_button)
            } else {
                Animations.encollapse(file_button, 150)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        haтdleViews()
    }

    private fun initRecycler() {
        rv_images.adapter = adapter
        rv_images.layoutManager = SmoothLinearManager(requireContext())
    }

    private fun haтdleViews() {
        cancel_button.setOnClickListener {
            handleCancel()
        }
    }

    private fun handleCancel() {
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    private fun getAllShownImagesPath(): List<Uri> {
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val listOfAllImages = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        requireContext().contentResolver.query(uriExternal, projection, null, null, null)
            ?.use { cursor ->
                val columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    val uriImage =
                        Uri.withAppendedPath(uriExternal, "" + cursor.getLong(columnIndexID))
                    listOfAllImages.add(uriImage)
                }
                cursor.close()
            }
        return listOfAllImages
    }

    override val layoutId = R.layout.f_file_chooser
}