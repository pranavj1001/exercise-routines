package com.pranavj1001.exerciseroutines

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class AddExerciseRoutine : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var exercises: Array<ExerciseBody>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise_routine)

        loadExercises()

        viewManager = LinearLayoutManager(this)
        viewAdapter = ExercisesRecyclerViewAdapter(exercises)

        recyclerView = findViewById<RecyclerView>(R.id.excercisesList).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
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

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)

        } else {
            // TODO: Show an Error Alert and revert action
        }
    }

    /**
     * Adds a new exercise in the current routine
     */
    fun addExercise(view: View) {
        exercises = exercises.plusElement(ExerciseBody())
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

    /**
     * Loads Exercises from routine if present
     */
    private fun loadExercises() {
        exercises = emptyArray()

        // TODO: If data is present then make load all exercises
    }

}
