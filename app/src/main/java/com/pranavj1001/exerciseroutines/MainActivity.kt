package com.pranavj1001.exerciseroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun addExerciseRoutine(view: View) {
        val searchText = findViewById<EditText>(R.id.searchText)
        val message = searchText.text.toString()
        val intent = Intent(this, AddExerciseRoutine::class.java)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

}
