package com.example.cacheimage.presentation.image_listing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cacheimage.R
import com.example.cacheimage.databinding.RowItemImageBinding
import com.example.cacheimage.domain.model.Image
import com.shaya.cacheImage.CacheImageBuilder

class ImageListAdapter(
    private var items: ArrayList<Image>
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: RowItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, item: Image) {
            CacheImageBuilder
                .with(context)
                .url(item.url)
                .callback(object : CacheImageBuilder.Callback {
                    override fun onLoading() {
                        binding.progressCircular.visibility = View.VISIBLE
                    }
                    override fun onLoaded() {
                        binding.progressCircular.visibility = View.GONE
                    }
                })
                .into(binding.ivImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<RowItemImageBinding>(
                layoutInflater,
                R.layout.row_item_image,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(holder.itemView.context, item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}