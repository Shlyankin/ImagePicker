package com.example.imagepicker.ui

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
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
            file_button.isVisible = adapter.isExpanded
            (rv_images.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(position, 0);
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_images.adapter = adapter
        hadleViews()
    }

    fun hadleViews() {
        cancel_button.setOnClickListener {
            handleCancel()
        }
    }

    fun handleCancel() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun getAllShownImagesPath(): List<Uri> {
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