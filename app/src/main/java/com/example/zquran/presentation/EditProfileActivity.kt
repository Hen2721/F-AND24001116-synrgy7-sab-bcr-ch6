package com.example.zquran.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkInfo
import coil.load
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerLauncher
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.example.domain.Profile
import com.example.presentation.helper.Helper
import com.example.presentation.helper.worker.KEY_IMAGE_URI
import com.example.zquran.R
import com.example.zquran.databinding.ActivityEditProfileBinding
import com.example.zquran.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    companion object {
        private const val MIME_TYPE_IMAGE = "image/*"
        private const val REQUEST_CODE_PERMISSION = 0

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, EditProfileActivity::class.java))
        }
    }

    private lateinit var binding: ActivityEditProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var imagePickerConfig: ImagePickerConfig
    private lateinit var imagePickerLauncher: ImagePickerLauncher

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionsAndroid13 = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_MEDIA_IMAGES,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        profileViewModel.getProfile()

        observeProfile()
        handleUpdateProfile()

        imagePickerConfig = ImagePickerConfig {
            mode = ImagePickerMode.SINGLE
            theme = R.style.ImagePickerTheme
            isFolderMode = true
        }
        imagePickerLauncher = registerImagePicker { result: List<Image> ->
            result.forEach {
                profileViewModel.applyBlurImage(it.uri)
                handleUploadProfileImage(it)
            }
        }

        binding.imageButtonEdit.setOnClickListener { checkPermissions() }
    }

    private fun observeProfile() {
        profileViewModel.error.observe(this, ::handleError)
        profileViewModel.loading.observe(this, ::handleLoading)
        profileViewModel.dataLoading.observe(this, ::handleDataLoading)
        profileViewModel.profileData.observe(this, ::handleGetProfile)
        profileViewModel.message.observe(this, ::handleSuccess)
        profileViewModel.getOutputWorkerInfo().observe(this, ::handleWorkerInfos)
        profileViewModel.profileImage.observe(this, ::handleBlurImage)
        profileViewModel.profileImageLoading.observe(this, ::handleProfileImageLoading)
        profileViewModel.profileImageMessage.observe(this, ::handleProfileImageSuccess)
    }

    private fun handleError(error: String) {
        Helper.showToast(this, this, error, isSuccess = false)
    }

    private fun handleSuccess(message: String) {
        Helper.showToast(this, this, message, isSuccess = true)
        ProfileActivity.startActivity(this)
        this.finish()
    }

    private fun handleLoading(loading: Boolean) {
        val visibility = if (loading) View.VISIBLE else View.GONE
        binding.profileLoadingLayout.visibility = visibility
    }

    private fun handleDataLoading(loading: Boolean) {
        if (loading) {
            binding.editProfileLayout.loadSkeleton()
        } else {
            Handler().postDelayed({
                binding.editProfileLayout.hideSkeleton()
            }, 2000)
        }
    }

    private fun handleGetProfile(user: Profile) {
        binding.profileTiName.setText(user.name)
        binding.profileTiUsername.setText(user.username)
        binding.profileTiEmail.setText(user.email)
        binding.profileTiPhone.setText(user.phone)
        binding.profileUserImage.load(user.image)
    }

    private fun handleUpdateProfile() {
        binding.profileButtonEdit.setOnClickListener {
            val name = binding.profileTiName.text.toString()
            val username = binding.profileTiUsername.text.toString()
            val email = binding.profileTiEmail.text.toString()
            val phone = binding.profileTiPhone.text.toString()

            profileViewModel.updateProfile(Profile(
                name = name,
                username = username,
                email = email,
                phone = phone,
                image = null
            ))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item);
    }

    private fun checkPermissions() {
        Helper.requestPermissions(
            this,
            permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionsAndroid13
            } else {
                permissions
            },
            requestCode = REQUEST_CODE_PERMISSION,
            doIfGranted = ::showGallery,
        )
    }

    private fun showGallery() {
        imagePickerLauncher.launch(imagePickerConfig)
    }

    private fun handleUploadProfileImage(image: Image) {
        val imagePart = MultipartBody.Part.createFormData(
            "image", image.name, RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                File(image.path).readBytes()
            )
        )
        profileViewModel.updateProfileImage(imagePart)
    }

    private fun handleProfileImageLoading(loading: Boolean) {
        if (!loading) profileViewModel.getProfile()
    }

    private fun handleProfileImageSuccess(message: String) {
        Helper.showToast(this, this, message, isSuccess = true)
    }

    private fun handleBlurImage(image: String) {
        binding.profileUserImage.setImageURI(Uri.parse(image))
    }

    private fun handleWorkerInfos(workerInfos: List<WorkInfo>) {
        if (workerInfos.isEmpty()) {
            return
        }

        val workerInfo = workerInfos.last()
        if (workerInfo.state.isFinished) {
            val outputImageUrl = workerInfo.outputData.getString(KEY_IMAGE_URI)
            if (!outputImageUrl.isNullOrEmpty()) {
                profileViewModel.saveBlurImage(outputImageUrl)
            }
        }
    }
}