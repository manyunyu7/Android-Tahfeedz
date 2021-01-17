package com.feylabs.tahfidz.View.Teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.feylabs.tahfidz.R
import kotlinx.android.synthetic.main.activity_mentor_container.*

class MentorContainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_container)

        setupViews()
    }

    fun setupViews()
    {
        // Finding the Navigation Controller
        var navController = findNavController(R.id.nav_host_teacher)

        // Setting Navigation Controller with the BottomNavigationView
        mentorBottomNavigation.setupWithNavController(navController)
    }
}