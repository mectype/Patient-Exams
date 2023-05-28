package com.example.odtpatients.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.odtpatients.databinding.ImageExamLayoutBinding
import com.squareup.picasso.Picasso
import java.io.File

class ImagesAdapter(directoryPath : String) : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    private val imagesList : MutableList<File>

    init {
        imagesList = File(directoryPath).listFiles()!!.toMutableList()
        android.util.Log.e("GUYGUY","imagesList size  ${imagesList.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = ImageExamLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false);
        return ImageViewHolder((view))
    }

    override fun getItemCount() = imagesList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        android.util.Log.e("GUYGUY", "onBindViewHolder $position")
        Picasso.get().load(Uri.fromFile(imagesList[position])).into(holder.binding.imageExam)
    }

    fun addNewImage(image: File) {
        imagesList.add(image)
        notifyItemInserted(itemCount)
    }

    class ImageViewHolder(itemView: ImageExamLayoutBinding) : ViewHolder(itemView.root) {
        var binding: ImageExamLayoutBinding = itemView
    }

}