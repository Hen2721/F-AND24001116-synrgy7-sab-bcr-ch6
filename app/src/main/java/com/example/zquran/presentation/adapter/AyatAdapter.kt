package com.example.zquran.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.Ayat
import com.example.zquran.R
import com.example.zquran.presentation.diffutil.AyatDiffUtil
import com.example.zquran.presentation.viewholder.AyatViewHolder

class AyatAdapter(): ListAdapter<Ayat, AyatViewHolder>(
    AyatDiffUtil()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyatViewHolder {
        return AyatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_ayat,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AyatViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }
}