package com.example.zquran.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.domain.User
import com.example.presentation.helper.Helper
import com.example.zquran.databinding.ActivityRegisterBinding
import com.example.zquran.presentation.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }
    }

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observeRegister()

        binding.registerButton.setOnClickListener { handleRegister() }

        binding.registerLoginText2.setOnClickListener { LoginActivity.startActivity(this) }
    }

    private fun observeRegister() {
        registerViewModel.error.observe(this, ::handleError)
        registerViewModel.loading.observe(this, ::handleLoading)
        registerViewModel.register.observe(this, ::handleSuccess)
    }

    private fun handleError(error: String) {
        Helper.showToast(this, this, error, isSuccess = false)
    }

    private fun handleLoading(loading: Boolean) {
        val visibility = if (loading) View.VISIBLE else View.GONE
        binding.registerLoadingLayout.visibility = visibility
    }

    private fun handleSuccess(message: String) {
        Helper.showToast(this, this, message, isSuccess = true)
        LoginActivity.startActivity(this)
    }

    private fun handleRegister() {
        val name = binding.registerTiName.text.toString()
        val username = binding.registerTiUsername.text.toString()
        val email = binding.registerTiEmail.text.toString()
        val phone = binding.registerTiPhone.text.toString()
        val password = binding.registerTiPassword.text.toString()

        registerViewModel.register(
            User(
                name = name,
                username = username,
                email = email,
                phone = phone,
                password = password
            )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item);
    }
}