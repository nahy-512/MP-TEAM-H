package com.nahyun.mz.ui.discussion.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahyun.mz.databinding.ItemCommentBinding
import com.nahyun.mz.domain.model.Comment

class DiscussionCommentAdapter(
    val postAuthorId: Int
): RecyclerView.Adapter<DiscussionCommentAdapter.ViewHolder>(){

    private var commentList = emptyList<Comment>()
    private lateinit var mItemClickListener: MyItemClickListener

    fun setCommentClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addComment(commentList: List<Comment>) {
        this.commentList = commentList
        notifyDataSetChanged()
    }

    interface MyItemClickListener {
        fun onLikeClick(routeId: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommentBinding = ItemCommentBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commentList[position])
        holder.apply {
            binding.itemCommentLikeBtn.setOnClickListener {
                mItemClickListener.onLikeClick(position)
            }
        }
    }

    override fun getItemCount(): Int = commentList.size

    inner class ViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment
            binding.isAuthor = (comment.userId == postAuthorId)
        }
    }
}