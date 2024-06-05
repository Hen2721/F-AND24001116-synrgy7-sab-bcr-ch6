package com.example.zquran.presentation.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.Surah

class SurahDiffUtil: DiffUtil.ItemCallback<Surah>() {
    override fun areItemsTheSame(
        oldItem: Surah,
        newItem: Surah
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Surah,
        newItem: Surah
    ): Boolean {
        return oldItem.number == newItem.number
    }
}