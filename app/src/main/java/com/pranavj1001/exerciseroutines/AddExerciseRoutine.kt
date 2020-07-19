package com.pranavj1001.exerciseroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AddExerciseRoutine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise_routine)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }
}
