package com.pranavj1001.exerciseroutines

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.gson.Gson

class AddExerciseRoutine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise_routine)
    }

    // disable animation which is triggered when we switch to another activity
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    /**
     * Saves data in json format in phones internal storage
     */
    fun saveRoutine(view: View) {

        // prepare file contents
        val routineObject = RoutineBody()
        routineObject.name = findViewById<EditText>(R.id.routineNameText).text.toString()
        val fileContents = Gson().toJson(routineObject)

        if (validateRoutineObject(routineObject)) {
            applicationContext.openFileOutput(routineObject.name, Context.MODE_PRIVATE).use {
                it.write(fileContents.toByteArray())
            }

        } else {
            // TODO: Show an Error Alert and revert action
        }
    }

    /**
     * Validates routineObject which is to be saved
     */
    private fun validateRoutineObject(routineObject: RoutineBody): Boolean {
        if (routineObject.name == "") {
            return false
        }
        return true
    }

}
