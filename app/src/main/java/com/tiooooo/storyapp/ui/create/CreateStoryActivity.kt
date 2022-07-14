package com.tiooooo.storyapp.ui.create

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
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
import timber.log.Timber
import java.io.File
import java.util.Locale

class CreateStoryActivity : BaseActivity<ActivityAddStoryBinding>(), OnMapReadyCallback {
    override fun getViewBinding() = ActivityAddStoryBinding.inflate(layoutInflater)
    private val viewModel: CreateStoryViewModel by viewModel()
    private var getFile: File? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun initView() {
        isUsingToolbar(binding.toolbar, true)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
            binding.tvTextPhoto.text = getString(R.string.text_edit_photo)
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
            binding.tvTextPhoto.text = getString(R.string.text_edit_photo)
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

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()
        getMyLocation()
        mMap.uiSettings.apply {
            isScrollGesturesEnabled = false
            isZoomControlsEnabled = false
            isIndoorLevelPickerEnabled = false
            isCompassEnabled = false
            isMapToolbarEnabled = false
            isZoomGesturesEnabled = false
        }

    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map))
            if (!success) {
                Timber.e("Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Timber.e("Can't find style. Error: $exception")
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            } else snackBar(getString(R.string.permission_denied), InfoEnum.DANGER)
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener {
                it?.let {
                    val position = LatLng(it.latitude, it.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
                    mMap.addMarker(MarkerOptions().position(position))
                    setUpLocation(it.latitude, it.longitude)

                    viewModel.latitude = it.latitude
                    viewModel.longitude = it.longitude
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setUpLocation(latitude: Double, longitude: Double) {
        try {
            val currentLatLong =
                LatLng(latitude, longitude)
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(currentLatLong.latitude, currentLatLong.longitude, 1)

            binding.tvLocation.text = addresses[0].getAddressLine(0)
        } catch (err: Exception) {
            Timber.e(err)
        }
    }
}