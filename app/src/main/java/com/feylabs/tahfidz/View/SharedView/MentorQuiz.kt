package com.feylabs.tahfidz.View.SharedView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import kotlinx.android.synthetic.main.activity_mentor_quiz.*

class MentorQuiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_quiz)
        setupViews()

        //Hide Add Quiz Menu if student login
        if (Preference(this).getPrefString("login_type").toString()=="student"){
            quizBottomNavigation.menu.removeItem(R.id.mentorAddQuizFragment)
        }
    }

    fun setupViews()
    {
        // Finding the Navigation Controller
        var navController = findNavController(R.id.quizFragmentHost)

        // Setting Navigation Controller with the BottomNavigationView
        quizBottomNavigation.setupWithNavController(navController)

        // Setting Up ActionBar with Navigation Controller
//        setupActionBarWithNavController(navController)
    }
}