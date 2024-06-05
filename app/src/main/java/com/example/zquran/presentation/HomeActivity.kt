package com.example.zquran.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.Surah
import com.example.presentation.helper.Helper
import com.example.presentation.helper.applyLanguage
import com.example.zquran.R
import com.example.zquran.databinding.ActivityHomeBinding
import com.example.zquran.presentation.adapter.SurahAdapter
import com.example.zquran.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), OnClickListener {
    companion object {
        const val NO_SURAH_KEY = "noSurah"

        fun provideIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    private lateinit var binding: ActivityHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private var layoutManager: GridLayoutManager = GridLayoutManager(this, 1)

    private var surahAdapter = SurahAdapter(layoutManager) { surahData: Surah ->
        handleNavigateToSurahDetail(surahData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.surahRecycler.layoutManager = layoutManager

        homeViewModel.fetchAllSurahData()

        observeSurahData()
        handleNavBottom()

        binding.bottomNav.bringToFront()
        binding.surahGridIcon.bringToFront()
        binding.surahGridIcon.setOnClickListener(this)
    }

    private fun observeSurahData() {
        homeViewModel.error.observe(this, ::handleError)
        homeViewModel.loading.observe(this, ::handleLoading)
        homeViewModel.surahData.observe(this, ::handleGetSurahData)
        homeViewModel.logoutLoading.observe(this, ::handleLogout)
    }

    private fun handleNavigateToSurahDetail(surahData: Surah) {
        val bundle = Bundle()
        bundle.putInt(NO_SURAH_KEY, surahData.number)
        SurahDetailActivity.startActivity(this, bundle)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.surah_grid_icon -> {
                handleSwitchLayout()
                handleSwitchIcon()
            }
        }
    }

    private fun handleSwitchLayout() {
        layoutManager.spanCount = if (layoutManager.spanCount == 1) 2 else 1
        surahAdapter.notifyItemRangeChanged(0, surahAdapter?.itemCount ?: 0)
    }

    private fun handleSwitchIcon() {
        val icon = if (layoutManager.spanCount == 1) R.drawable.ic_grid_on else R.drawable.ic_grid_off
        binding.surahGridIcon.setImageResource(icon)
    }

    private fun handleError(error: String) {
        Helper.showToast(this, this, error, isSuccess = false)
    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {
            binding.homeLayout.loadSkeleton()
        } else {
            Handler().postDelayed({
                binding.homeLayout.hideSkeleton()
            }, 2000)
        }
    }

    private fun handleGetSurahData(surahData: List<Surah>) {
        this.surahAdapter.submitList(surahData)
        binding.surahRecycler.adapter = this.surahAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                homeViewModel.logout()
                return true
            }
            R.id.menu_language_id -> {
                applyLanguage("id", provideIntent(this))
            }
            R.id.menu_language_en -> {
                applyLanguage("en", provideIntent(this))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun handleLogout(loading: Boolean) {
        binding.homeLoadingLayout.bringToFront()
        if (loading) {
            binding.homeLoadingLayout.visibility = View.VISIBLE
        } else {
            binding.homeLoadingLayout.visibility = View.GONE
            Helper.showToast(this, this, getString(R.string.message_logout), isSuccess = true)
            GetStartedActivity.startActivity(this)
            this.finish()
        }
    }

    private fun handleNavBottom() {
        binding.bottomNav.selectedItemId = R.id.nav_menu_home
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_menu_home -> true
                R.id.nav_menu_profile -> {
                    ProfileActivity.startActivity(this)
                    true
                }
                else -> false
            }
        }
    }
}