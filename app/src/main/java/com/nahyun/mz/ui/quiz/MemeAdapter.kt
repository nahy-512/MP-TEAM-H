package com.nahyun.mz.ui.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahyun.mz.databinding.ItemMemeBinding
import com.nahyun.mz.domain.model.Meme

class MemeAdapter(
    val memeList: List<Meme>
): RecyclerView.Adapter<MemeAdapter.ViewHolder>(){

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMemeClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    interface MyItemClickListener {
        fun onItemClick(meme: Meme)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMemeBinding = ItemMemeBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memeList[position])
        holder.apply {
            // 아이템 전체 클릭
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(memeList[position])
            }
        }
    }

    override fun getItemCount(): Int = memeList.size

    inner class ViewHolder(val binding: ItemMemeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meme: Meme) {
            binding.meme = meme
        }
    }
}