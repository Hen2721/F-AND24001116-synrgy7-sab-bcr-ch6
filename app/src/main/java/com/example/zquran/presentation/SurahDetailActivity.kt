package com.example.zquran.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.SurahDetail
import com.example.presentation.helper.Helper
import com.example.zquran.databinding.ActivitySurahDetailBinding
import com.example.zquran.presentation.adapter.AyatAdapter
import com.example.zquran.presentation.viewmodel.SurahDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

@AndroidEntryPoint
class SurahDetailActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context, bundle: Bundle) {
            val intent = Intent(context, SurahDetailActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivitySurahDetailBinding

    private val surahDetailViewModel: SurahDetailViewModel by viewModels()

    private var ayatAdapter = AyatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurahDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.ayatRecycler.layoutManager = LinearLayoutManager(this)

        val bundle = intent.extras
        val noSurah = bundle!!.getInt(HomeActivity.NO_SURAH_KEY)

        surahDetailViewModel.fetchSurahDataDetail(noSurah)

        observeSurahDetailData()
    }

    private fun observeSurahDetailData() {
        surahDetailViewModel.error.observe(this, ::handleError)
        surahDetailViewModel.loading.observe(this, ::handleLoading)
        surahDetailViewModel.surahDetailData.observe(this, ::handleGetSurahDetailData)
    }

    private fun handleError(error: String) {
        Helper.showToast(this, this, error, isSuccess = false)
    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {
            binding.surahDetailLayout.loadSkeleton()
        } else {
            Handler().postDelayed({
                binding.surahDetailLayout.hideSkeleton()
            }, 2000)
        }
    }

    private fun handleGetSurahDetailData(surahDetailData: SurahDetail) {
        this.ayatAdapter.submitList(surahDetailData.ayahs)
        binding.ayatRecycler.adapter = this.ayatAdapter

        binding.surahDetailTitle.text = surahDetailData.englishName
        binding.surahDetailName.text = surahDetailData.englishNameTranslation
        binding.surahDetailAyat.text = "${surahDetailData.numberOfAyahs} Ayat"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item);
    }
}