package com.pranavj1001.exerciseroutines

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class AddExerciseRoutine : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ExercisesRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var exercises: Array<ExerciseBody>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise_routine)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(R.string.app_intent_key.toString())
        loadExercises(message)

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
        val routineObject = prepareRoutineObject()
        val fileContents = Gson().toJson(routineObject)
        val validationString = validateRoutineObject(routineObject)

        if (validationString.isEmpty()) {
            applicationContext.openFileOutput(routineObject.name, Context.MODE_PRIVATE).use {
                it.write(fileContents.toByteArray())
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)

        } else {
            val builder = AlertDialog.Builder(this)
                .setTitle(getString(R.string.err_title_invalid_routine))
                .setMessage(validationString)
            builder.setCancelable(true)
            builder.setPositiveButton(R.string.alert_positive_button_text)
                { dialog, id -> dialog.cancel() }

            val alert = builder.create()
            alert.show()
        }
    }

    /**
     * Adds a new exercise in the current routine
     */
    fun addExercise(view: View) {
        exercises = exercises.plusElement(ExerciseBody())
        viewAdapter.setExercises(exercises)
        viewAdapter.notifyDataSetChanged()
    }


    /**
     * Prepares the object of a Routine
     */
    private fun prepareRoutineObject(): RoutineBody {
        val routineObject = RoutineBody()
        routineObject.name = findViewById<EditText>(R.id.routineNameText).text.toString()
        routineObject.exercises = viewAdapter.getExercises()
        return routineObject
    }

    /**
     * Validates routineObject which is to be saved
     */
    private fun validateRoutineObject(routineObject: RoutineBody): String {
        var validationString: String = ""
        if (routineObject.name == "") {
            validationString += "\nRoutine name cannot be Empty"
        }
        if (routineObject.exercises.isNotEmpty()) {
            var nameIndexes = emptyArray<Number>()
            var timeIndexes = emptyArray<Number>()
            var pos = 1
            for (exercise in routineObject.exercises) {
                if (exercise.name.isEmpty()) {
                    nameIndexes = nameIndexes.plusElement(pos);
                }
                if (exercise.time.isEmpty()) {
                    timeIndexes = timeIndexes.plusElement(pos);
                }
                pos++;
            }
            if (nameIndexes.isNotEmpty()) {
                validationString += "\nPlease add names for exercise number: " + nameIndexes.joinToString(", ")
            }
            if (timeIndexes.isNotEmpty()) {
                validationString += "\nPlease add durations for exercise number: " + timeIndexes.joinToString(", ")
            }
        }
        return validationString
    }

    /**
     * Loads Exercises from routine if present
     */
    private fun loadExercises(routineName: String) {
        if (routineName.isNotEmpty()) {
            val routineData: String = applicationContext.openFileInput(routineName).bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    "$some\n$text"
                }
            }
            val routineObject = Gson().fromJson(routineData, RoutineBody::class.java)
            exercises = routineObject.exercises
        } else {
            exercises = emptyArray()
        }
    }

}
