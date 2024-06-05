package com.example.zquran.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.domain.Profile
import com.example.presentation.helper.Helper
import com.example.presentation.helper.applyLanguage
import com.example.zquran.R
import com.example.zquran.databinding.ActivityProfileBinding
import com.example.zquran.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    companion object {
        fun provideIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ProfileActivity::class.java))
        }
    }

    private lateinit var binding: ActivityProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handleNavBottom()
        observeProfile()

        profileViewModel.getProfile()

        binding.profileButtonEdit.setOnClickListener { EditProfileActivity.startActivity(this) }
        binding.profileButtonLogout.setOnClickListener { profileViewModel.logout() }
    }

    private fun observeProfile() {
        profileViewModel.error.observe(this, ::handleError)
        profileViewModel.dataLoading.observe(this, ::handleLoading)
        profileViewModel.profileData.observe(this, ::handleProfileData)
        profileViewModel.logoutLoading.observe(this, ::handleLogout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                profileViewModel.logout()
                return true
            }
            R.id.menu_language_id -> {
                applyLanguage("id", ProfileActivity.provideIntent(this))
            }
            R.id.menu_language_en -> {
                applyLanguage("en", ProfileActivity.provideIntent(this))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun handleNavBottom() {
        binding.bottomNav.selectedItemId = R.id.nav_menu_profile
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_menu_home -> {
                    HomeActivity.startActivity(this)
                    true
                }
                R.id.nav_menu_profile -> true
                else -> false
            }
        }
    }

    private fun handleError(error: String) {
        Helper.showToast(this, this, error, isSuccess = false)
    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {
            binding.profileLayout.loadSkeleton()
        } else {
            Handler().postDelayed({
                binding.profileLayout.hideSkeleton()
            }, 2000)
        }
    }

    private fun handleLogout(loading: Boolean) {
        binding.profileLoadingLayout.bringToFront()
        if (loading) {
            binding.profileLoadingLayout.visibility = View.VISIBLE
        } else {
            binding.profileLoadingLayout.visibility = View.GONE
            Helper.showToast(this, this, getString(R.string.message_logout), isSuccess = true)
            GetStartedActivity.startActivity(this)
            this.finish()
        }
    }

    private fun handleProfileData(user: Profile) {
        binding.profileUserName.text = user.name
        binding.profileUserUsername.text = user.username
        binding.profileUserEmail.text = user.email
        binding.profileUserImage.load(user.image)
    }
}