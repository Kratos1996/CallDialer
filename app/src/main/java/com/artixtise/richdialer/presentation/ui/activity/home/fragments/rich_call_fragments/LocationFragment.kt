package com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.application.ErrorMessage.LOCATION_SERVICE_PERMISSION
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.R
import com.artixtise.richdialer.data.call.model.RichCallData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.FragmentLocationBinding
import com.artixtise.richdialer.presentation.ui.activity.preview.SelectScreenActivity
import com.artixtise.richdialer.presentation.ui.activity.home.viewmodel.HomeViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*


class LocationFragment : BaseFragment(R.layout.fragment_location), OnMapReadyCallback {
    lateinit var binding: FragmentLocationBinding

    private var mMap: GoogleMap? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat = 0.0
    private var lng = 0.0
    var REQUEST_CHECK_SETTINGS = 101

    companion object {

        var Instance: LocationFragment? = null
        var viewModel: HomeViewModel? = null
        var parent: SelectScreenActivity? = null
        var contactList: ContactList? = null
        fun newInstance(
            viewmodel: HomeViewModel,
            from: SelectScreenActivity,
            list: ContactList
        ): LocationFragment? {
            contactList = list
            viewModel = viewmodel
            if (Instance == null) {
                parent = from
                Instance = LocationFragment()
            }
            return Instance
        }
    }

    override fun WorkStation() {
        binding.btnSend.setOnClickListener {
            Dexter.withContext(requireActivity())
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            if (isGpeEnabled()) {
                                getLastLocation()

                                saveSenderData()

                            } else {
                                turnOnGPS()
                            }
                        } else if (report.isAnyPermissionPermanentlyDenied) {
                            Toast.makeText(
                                requireContext(),
                                LOCATION_SERVICE_PERMISSION,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }).withErrorListener {
                    showCustomAlert(it.name, requireView())
                }.check()
        }
    }

    private fun saveSenderData() {
       /* val richData = RichCallData(
            contactList!!.name,
            contactList!!.email,
            contactList!!.phoneNumber,
            contactList!!.phoneNumber,
            0,
            "",
            "",
            "",
            "",
            "LOCATION"
        )
        lifecycleScope.launchWhenCreated {
            EmojiFragment.viewModel?.saveSenderData(richData)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
       // ( binding.root.findViewById<FrameLayout>(R.id.map) as SupportMapFragment?)!!.getMapAsync(this)
        WorkStation()
        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                println(it)

                lat = it.latitude
                lng = it.longitude
                onCurrentLocationClicked()
            } else {
                val locationRequest = create().apply {
                    interval = 10000
                    fastestInterval = 5000
                    priority = PRIORITY_HIGH_ACCURACY
                }
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return
                        for (location in locationResult.locations) {
                            if (location != null) {
                                lat = location.latitude
                                lng = location.longitude
                                fusedLocationClient.removeLocationUpdates(locationCallback)
                                onCurrentLocationClicked()
                            }
                        }
                    }
                }
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

            }
        }
    }

    private fun turnOnGPS() {

        val locationRequest = create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: com.google.android.gms.tasks.Task<LocationSettingsResponse>? =
            client.checkLocationSettings(builder.build())
        task?.addOnSuccessListener {
            getLastLocation()
        }
        task?.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun onCurrentLocationClicked() {
        //parent!!.dismiss()
        val addresses: List<Address>
        val geocoder = Geocoder(requireContext(), Locale.getDefault())



        addresses = geocoder.getFromLocation(
            lat,
            lng,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        val city = addresses[0].locality + " , " + addresses[0].adminArea
        showCustomAlert(city, binding.root)

    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    binding.btnSend.callOnClick()

                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    binding.btnSend.callOnClick()
                }
                else -> {

                    // No location access granted.
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun isGpeEnabled(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false
        }
        return true
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        if (lat != null && lat != 0.0) {
            val myLoc = LatLng(lat, lng)
            val addresses: List<Address>
            val geocoder = Geocoder(requireContext(), Locale.getDefault())



            addresses = geocoder.getFromLocation(
                lat,
                lng,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            val city = addresses[0].locality + " , " + addresses[0].adminArea
            mMap!!.addMarker(MarkerOptions().position(myLoc).title(city))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(myLoc))

        }

    }

}