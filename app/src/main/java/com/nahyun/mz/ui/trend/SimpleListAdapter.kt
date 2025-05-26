package com.nahyun.mz.ui.trend

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahyun.mz.databinding.ItemSimpleListBinding

data class ItemInfo(
    val name: String,
    val change: String,
    val total: String,
    val ratio: String,
    val query: String
)

class SimpleListAdapter(private val items: List<ItemInfo>) :
    RecyclerView.Adapter<SimpleListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSimpleListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemInfo) {
            binding.itemText.text = item.name
            binding.itemChange.text = item.change

            // 🔍 클릭 시 구글 검색
            binding.root.setOnClickListener {
                val encodedQuery = Uri.encode(item.query)
                val searchUrl = "https://www.google.com/search?q=$encodedQuery"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSimpleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
