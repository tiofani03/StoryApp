package com.tiooooo.storyapp.ui.create

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
import com.tiooooo.core.utils.constant.InfoEnum
import com.tiooooo.core.utils.extensions.reduceFileImage
import com.tiooooo.core.utils.extensions.rotateBitmap
import com.tiooooo.core.utils.extensions.snackBar
import com.tiooooo.core.utils.extensions.uriToFile
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.ActivityAddStoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class CreateStoryActivity : BaseActivity<ActivityAddStoryBinding>() {
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
                viewModel.createStories(
                    binding.etSmall.text.toString(),
                    compressFile
                )
            } ?: run {
                snackBar(getString(R.string.please_add_photo), InfoEnum.DANGER)
            }
        }
    }

    override fun setSubscribeToLiveData() {
        with(viewModel) {
            createStories.observe(this@CreateStoryActivity) {
                if (it == false) {
                    Toast.makeText(
                        this@CreateStoryActivity,
                        getString(R.string.success_created),
                        Toast.LENGTH_SHORT
                    ).show()
                    successCreateStory()
                } else {
                    snackBar(getString(R.string.failed_to_create_story), InfoEnum.DANGER)
                }
            }

            createStoriesState.observe(this@CreateStoryActivity) {
                populateLoadingDialog(it)
            }

            createStoriesError.observe(this@CreateStoryActivity) {
                snackBar(it, InfoEnum.DANGER)
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
                snackBar(getString(R.string.permission_denied), InfoEnum.DANGER)
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
            val myFile = it.data?.getSerializableExtra(EXTRA_PICTURE) as File
            val isBackCamera = it.data?.getBooleanExtra(EXTRA_IS_BACK_CAMERA, true) as Boolean

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
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.choose_image))

        val list = arrayOf(
            getString(R.string.take_photo),
            getString(R.string.choose_from_gallery)
        )
        builder.setItems(list) { _, which ->
            when (which) {
                0 -> listener.onTakeCameraSelected()
                1 -> listener.onChooseGallerySelected()
            }
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun successCreateStory() {
        val intent = Intent().apply {
            putExtra(ADD_STORY_STATE, true)
        }
        setResult(ADD_STORY_CODE, intent)
        finish()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        const val EXTRA_PICTURE = "picture"
        const val EXTRA_IS_BACK_CAMERA = "isBackCamera"
        const val ADD_STORY_CODE = 100
        const val ADD_STORY_STATE = "state"

        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}