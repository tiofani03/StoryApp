package com.tiooooo.storyapp.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.activity.result.contract.ActivityResultContracts
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
import com.tiooooo.core.utils.constant.InfoEnum
import com.tiooooo.core.utils.extensions.snackBar
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.ActivityMapBinding
import com.tiooooo.storyapp.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MapActivity : BaseActivity<ActivityMapBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val viewModel: MainViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun initView() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        isUsingToolbar(binding.toolbar, true)
        viewModel.getStoriesWithLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()
        getMyLocation()
        mMap.apply {
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isMapToolbarEnabled = true
        }

        viewModel.listStoriesMap.observe(this) {
            it.forEach { story ->
                val latLng = LatLng(story.lat, story.lon)
                latLng.let {
                    mMap.addMarker(MarkerOptions().position(latLng).title(story.name))
                }
            }
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

    override fun getViewBinding() = ActivityMapBinding.inflate(layoutInflater)

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
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 5f))
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}