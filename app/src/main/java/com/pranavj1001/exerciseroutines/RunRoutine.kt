package com.pranavj1001.exerciseroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.gson.Gson

class RunRoutine : AppCompatActivity() {

    private lateinit var routineObject: RoutineBody
    private lateinit var currentExerciseTimeObject: Time
    private var currentExerciseIndex: Number = 0
    private var isPlayMode = true
    private var isPrevButtonDisabled = false
    private var isPlayPauseButtonDisabled = false
    private var isNextButtonDisabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_routine)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(R.string.app_intent_key.toString())
        loadRoutine(message)
    }

    // disable animation which is triggered when we switch to another activity
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    fun goToPreviousExercise(view: View) {

    }

    fun goToNextExercise(view: View) {

    }

    fun togglePlayAndPause(view: View) {

    }

    /**
     * Runs the Routine
     */
    private fun loadRoutine(routineName: String) {
        routineObject = getRoutine(routineName)
        currentExerciseTimeObject = Time()
        setMetaData(routineObject)
        if (routineObject.exercises.isNotEmpty()) {
            currentExerciseIndex = 0
            loadTimer(routineObject.exercises[0].time.toInt())
        } else {
            // TODO: Disable Action Buttons
        }
    }

    /**
     * Loads the on screen timer as per the given time in seconds
     */
    private fun loadTimer(timeInSeconds: Int) {
        updateTime(timeInSeconds)

        val hoursTextView = findViewById<TextView>(R.id.hoursValue)
        val minutesTextView = findViewById<TextView>(R.id.minutesValue)
        val secondsTextView = findViewById<TextView>(R.id.secondsValue)

        hoursTextView.setText(currentExerciseTimeObject.hours).toString()
        minutesTextView.setText(currentExerciseTimeObject.minutes).toString()
        secondsTextView.setText(currentExerciseTimeObject.seconds).toString()
    }

    /**
     * Update the timeObject
     */
    private fun updateTime(timeInSeconds: Int) {
        currentExerciseTimeObject.hours = String.format("%02d", (timeInSeconds / 3600));
        currentExerciseTimeObject.minutes = String.format("%02d", ((timeInSeconds % 3600) / 60));
        currentExerciseTimeObject.seconds = String.format("%02d", (timeInSeconds % 60));
    }

    /**
     * Sets MetaData of the current Routine
     */
    private fun setMetaData(routineData: RoutineBody) {
        val routineNameTextView = findViewById<TextView>(R.id.routineName)
        routineNameTextView.setText("Current Routine: " + routineData.name).toString()
        if (routineData.exercises.isEmpty()) {
            val currentExerciseTextView = findViewById<TextView>(R.id.currentExercise)
            currentExerciseTextView.setText("No Exercises Found. Please add some Exercises").toString()
        } else {
            val currentExerciseTextView = findViewById<TextView>(R.id.currentExercise)
            currentExerciseTextView.setText("Current Exercise: " + routineData.exercises[0].name).toString()
        }
    }

    /**
     * Gets Current Routine data
     */
    private fun getRoutine(routineName: String): RoutineBody {
        val routineData: String = applicationContext.openFileInput(routineName).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
        return Gson().fromJson(routineData, RoutineBody::class.java)
    }
}
