package com.feylabs.tahfidz.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.feylabs.tahfidz.R
import kotlinx.android.synthetic.main.activity_student_host.*

class StudentContainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_host)
        setupViews()
    }

    private fun setupViews()
    {
        // Finding the Navigation Controller
        var navController = findNavController(R.id.nav_host_student)

        // Setting Navigation Controller with the BottomNavigationView
        studentBottomNavigation.setupWithNavController(navController)
    }


}