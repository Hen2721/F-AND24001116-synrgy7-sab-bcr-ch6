package com.example.zquran.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.Surah
import com.example.zquran.R
import com.example.zquran.presentation.diffutil.SurahDiffUtil
import com.example.zquran.presentation.viewholder.SurahViewHolder

class SurahAdapter(
    private val layoutManager: GridLayoutManager,
    private val clickListener: (Surah) -> Unit
): ListAdapter<Surah, SurahViewHolder>(
    SurahDiffUtil()
) {
    enum class ViewType {
        GRID,
        LINEAR
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        return SurahViewHolder(
            LayoutInflater.from(parent.context).inflate(
                if (viewType == ViewType.LINEAR.ordinal) R.layout.item_surah else R.layout.item_grid_surah,
                parent,
                false
            ),
            viewType
        )
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        holder.bindData(getItem(position))

        holder.itemView.setOnClickListener {
            clickListener(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager.spanCount == 2) ViewType.GRID.ordinal else ViewType.LINEAR.ordinal
    }
}