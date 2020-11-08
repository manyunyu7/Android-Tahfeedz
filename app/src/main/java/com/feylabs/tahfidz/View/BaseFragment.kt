package com.feylabs.tahfidz.View

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    public fun String.showToast(){
        Toast.makeText(requireContext(),this, Toast.LENGTH_LONG).show()
    }
}