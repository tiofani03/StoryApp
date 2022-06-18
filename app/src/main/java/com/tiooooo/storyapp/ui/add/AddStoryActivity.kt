package com.tiooooo.storyapp.ui.add

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tiooooo.core.ui.base.BaseActivity
import com.tiooooo.core.ui.camera.CameraActivity
import com.tiooooo.core.utils.extensions.reduceFileImage
import com.tiooooo.core.utils.extensions.rotateBitmap
import com.tiooooo.core.utils.extensions.uriToFile
import com.tiooooo.storyapp.databinding.ActivityAddStoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class AddStoryActivity : BaseActivity<ActivityAddStoryBinding>() {
    override fun getViewBinding() = ActivityAddStoryBinding.inflate(layoutInflater)
    private val viewModel: CreateStoryViewModel by viewModel()
    private var getFile: File? = null

    override fun initView() {
        isUsingToolbar(binding.toolbar, true)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun initListener() {
        binding.btnAddPhoto.setOnClickListener {
            showImagePickerOptions(this, object : PickerOptionListener {
                override fun onTakeCameraSelected() = startCameraX()
                override fun onChooseGallerySelected() = startGallery()
            })
        }

        binding.btnAddStory.setOnClickListener {
            getFile?.let { file ->
                val compressFile = reduceFileImage(file)
                Timber.d("compressFile: $compressFile")
                viewModel.createStories(
                    binding.etSmall.text.toString(),
                    compressFile
                )
            } ?: run {
                Toast.makeText(this, "Please add photo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setSubscribeToLiveData() {
        with(viewModel) {
            createStories.observe(this@AddStoryActivity) {
                if (it == false) {
                    Toast.makeText(this@AddStoryActivity, "Story created", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(this@AddStoryActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapatkan permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherCameraIntentCameraX.launch(intent)
    }

    private val launcherCameraIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            getFile = myFile

            binding.ivStoriesPreview.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.ivStoriesPreview.setImageURI(selectedImg)
        }
    }


    interface PickerOptionListener {
        fun onTakeCameraSelected()
        fun onChooseGallerySelected()
    }

    private fun showImagePickerOptions(context: Context, listener: PickerOptionListener) {
        // setup the alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Pilih gambar")

        // add a list
        val animals = arrayOf(
            "Pilih dari kamera",
            "Pilih dari galeri"
        )
        builder.setItems(animals) { _, which ->
            when (which) {
                0 -> listener.onTakeCameraSelected()
                1 -> listener.onChooseGallerySelected()
            }
        }

        // create and show the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}