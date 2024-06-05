package com.example.zquran.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.domain.Surah
import com.example.zquran.R
import com.example.zquran.presentation.adapter.SurahAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class SurahViewHolder(
    private val surahItemView: View,
    private val viewType: Int
): RecyclerView.ViewHolder(surahItemView) {
    private var name: MaterialTextView? = null
    private var ayat: MaterialTextView? = null
    private var image: ShapeableImageView? = null

    fun bindData(data: Surah) {
        if (viewType == SurahAdapter.ViewType.LINEAR.ordinal) {
            name = surahItemView?.findViewById(R.id.item_surah_name)
            ayat = surahItemView?.findViewById(R.id.item_surah_ayat)
            image = surahItemView?.findViewById(R.id.item_surah_image)
        } else {
            name = surahItemView?.findViewById(R.id.item_grid_surah_name)
            ayat = surahItemView?.findViewById(R.id.item_grid_surah_ayat)
            image = surahItemView?.findViewById(R.id.item_grid_surah_image)
        }

        name?.text = data.englishName
        ayat?.text = "${data.numberOfAyahs.toString()} Ayat"
        image?.load("https://img.freepik.com/free-photo/top-view-islamic-new-year-concept-with-copy-space_23-2148611768.jpg?w=1060&t=st=1699471045~exp=1699471645~hmac=cad300ee4407fc88ce95044858ba5741ba252d3933547e95d9c00c1b6fcad2b0")
    }
}