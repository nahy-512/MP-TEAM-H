package com.nahyun.mz.ui.discussion

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahyun.mz.databinding.ItemDiscussionPostBinding
import com.nahyun.mz.domain.model.Discussion

class DiscussionPostAdapter: RecyclerView.Adapter<DiscussionPostAdapter.ViewHolder>(){

    private var postList = emptyList<Discussion>()
    private lateinit var mItemClickListener: MyItemClickListener

    fun setPostClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addPost(routeList: List<Discussion>) {
        this.postList = routeList
        notifyDataSetChanged()
    }

    interface MyItemClickListener {
        fun onItemClick(routeId: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDiscussionPostBinding = ItemDiscussionPostBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position])
        holder.apply {
            // 아이템 전체 클릭
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(postList[position].id.toInt())
            }
        }
    }

    override fun getItemCount(): Int = postList.size

    inner class ViewHolder(val binding: ItemDiscussionPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Discussion) {
            binding.post = post
        }
    }
}