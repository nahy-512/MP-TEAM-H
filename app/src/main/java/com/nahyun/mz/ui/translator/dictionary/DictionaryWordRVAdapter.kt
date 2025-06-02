package com.nahyun.mz.ui.translator.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nahyun.mz.domain.model.Word
import com.nahyun.mz.databinding.ItemWordBinding

class DictionaryWordRVAdapter(private val onFavoriteClick: (Word) -> Unit) :
    ListAdapter<Word, DictionaryWordRVAdapter.DictionaryViewHolder>(WordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val binding = ItemWordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DictionaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DictionaryViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(word: Word) {
            binding.word = word

            // 즐겨찾기
            binding.ivStar.setOnClickListener {
                onFavoriteClick(word)
            }
        }
    }

    class WordDiffCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.word == newItem.word
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }
    }
}