package com.feylabs.tahfidz.View.Base

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    public fun String.showToast(){
        Toast.makeText(requireContext(),this, Toast.LENGTH_LONG).show()
    }

    public fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(
              requireContext(),
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                111
            )
    }
}