package com.example.storesmkn1gending.ui.dashboard

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storesmkn1gending.databinding.ItemProductBinding
import com.example.storesmkn1gending.model.ResponseProductItem
import com.example.storesmkn1gending.network.ImagesAsynctask
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale


class ProductAdapter(private val onItemClick: (ResponseProductItem) -> Unit) :
    ListAdapter<ResponseProductItem, ProductAdapter.ViewHolder>(ProductDiffCallback()) {
    private var filteredList: List<ResponseProductItem> = emptyList()

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cvItemProduct.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val productItem = getItem(position)
                    onItemClick.invoke(productItem)
                }
            }
        }
        fun bind(product: ResponseProductItem) {
            binding.productTv.text = product.name
            binding.discTv.text = product.description
            val images = product.imageUrl
            val imageLoaderTask = ImagesAsynctask(binding.productImg)
            imageLoaderTask.execute(images)
            Log.d("TAG", "cek: " + images)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productItem = getItem(position)
        holder.bind(productItem)

    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getItem(position: Int): ResponseProductItem {
        return filteredList[position]
    }

    fun setFilteredList(filteredList: List<ResponseProductItem>) {
        this.filteredList = filteredList
        notifyDataSetChanged()
    }
}

private class ProductDiffCallback : DiffUtil.ItemCallback<ResponseProductItem>() {
    override fun areItemsTheSame(
        oldItem: ResponseProductItem,
        newItem: ResponseProductItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ResponseProductItem,
        newItem: ResponseProductItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

}
