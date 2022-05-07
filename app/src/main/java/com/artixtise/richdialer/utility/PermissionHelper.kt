package com.artixtise.richdialer.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment

class PermissionHelper {

    companion object {

        val PERMISSION_CODE = 0
        private lateinit var locationManager: LocationManager
        val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        )

        @JvmStatic
        fun hasPermission(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(activity, permission)
                            != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            } else {
                return true
            }

            return true
        }

        private val storagePermissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
        )

        @JvmStatic
        fun hasStoragePermission(activity: Activity): Boolean {
            for (permission in storagePermissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission)
                        != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }


        private val locationPermission = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )

        @JvmStatic
        fun hasLocationPermission(activity: Activity): Boolean {
            for (permission in locationPermission) {
                if (ActivityCompat.checkSelfPermission(activity, permission)
                        != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }

        @JvmStatic
        fun requestLocationPermission(activity: Activity) {
            ActivityCompat.requestPermissions(activity, locationPermission, PERMISSION_CODE)
        }

        @JvmStatic
        fun requestPermission(activity: Activity) {
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_CODE)
        }

        @JvmStatic
        fun requestPermission(fragment: Fragment) {
            fragment.requestPermissions(permissions, PERMISSION_CODE)
        }

        @JvmStatic
        fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
            for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    return true
                }
            }
            return false
        }
    }
}