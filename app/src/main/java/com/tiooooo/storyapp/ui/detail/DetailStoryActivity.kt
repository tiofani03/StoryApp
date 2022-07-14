package com.tiooooo.storyapp.ui.detail

import android.location.Address
import android.location.Geocoder
import coil.load
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.ui.base.BaseActivity
import com.tiooooo.core.utils.extensions.toDate
import com.tiooooo.core.utils.extensions.toDateString
import com.tiooooo.storyapp.R
import com.tiooooo.storyapp.databinding.ActivityDetailStoryBinding
import timber.log.Timber
import java.util.Locale


class DetailStoryActivity : BaseActivity<ActivityDetailStoryBinding>(), OnMapReadyCallback {
    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    private lateinit var mMap: GoogleMap
    private var story: StoryViewParam? = null

    override fun initView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        story = intent.getParcelableExtra(EXTRA_STORY)
        with(binding) {
            isUsingToolbar(toolbar, true)
            story?.let {
                tvName.text = it.name
                tvDate.text = getString(R.string.added_at, it.createdAt.toDate().toDateString())
                tvContent.text = it.description

                ivStories.load(it.photoUrl) {
                    placeholder(com.tiooooo.core.R.drawable.ic_image_load)
                }
            }
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            mMap.addMarker(
                MarkerOptions().title(addresses[0].adminArea).position(currentLatLong)
                    .snippet(addresses[0].subAdminArea)
            )
        } catch (err: Exception) {
            Timber.e(err)
        }
    }

    override fun getViewBinding() = ActivityDetailStoryBinding.inflate(layoutInflater)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.apply {
            isScrollGesturesEnabled = false
            isZoomControlsEnabled = false
            isIndoorLevelPickerEnabled = false
            isCompassEnabled = false
            isMapToolbarEnabled = false
            isZoomGesturesEnabled = false
        }
        story?.let {
            setUpLocation(it.lat, it.lon)
        }
    }
}