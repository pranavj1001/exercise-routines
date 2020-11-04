package com.pranavj1001.exerciseroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson

class RunRoutine : AppCompatActivity() {

    private lateinit var routineObject: RoutineBody
    private lateinit var currentExerciseTimeObject: Time
    private var currentExerciseIndex = 0
    private var isTimerRunning = false
    private var currentTimerValue: Long = 0
    private lateinit var currentTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_routine)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(R.string.app_intent_key.toString())

        routineObject = getRoutine(message)
        currentExerciseTimeObject = Time()
        loadRoutine()
    }

    // disable animation which is triggered when we switch to another activity
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    fun goToPreviousExercise(view: View) {
        currentExerciseIndex--
        onExerciseChange()
        loadRoutine()
        showOrHideActionButton()
    }

    fun goToNextExercise(view: View) {
        currentExerciseIndex++
        onExerciseChange()
        loadRoutine()
        showOrHideActionButton()
    }

    fun togglePlayAndPause(view: View) {
        if (isTimerRunning) {
            pauseTimer()
            val playPauseButton = findViewById<Button>(R.id.playPauseButton)
            playPauseButton.setText(R.string.play_button_text)
        } else {
            resumeTimer()
            val playPauseButton = findViewById<Button>(R.id.playPauseButton)
            playPauseButton.setText(R.string.pause_button_text)
        }
        isTimerRunning = !isTimerRunning
    }

    /**
     * Runs the Routine
     */
    private fun loadRoutine() {
        setMetaData(routineObject)
        if (routineObject.exercises.isNotEmpty()) {
            val timeInSeconds = routineObject.exercises[currentExerciseIndex].time.toLong()
            updateTimerValue(timeInSeconds)
            setTimer(timeInSeconds)
        } else {
            showOrHideActionButton(true)
        }
    }

    /**
     * Decides whether to display show or hide
     */
    private fun showOrHideActionButton(disableAll: Boolean = false) {
        val playPauseButton = findViewById<Button>(R.id.playPauseButton)
        val prevButton = findViewById<Button>(R.id.prevButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        if (disableAll) {
            enableOrDisableButton(playPauseButton, false)
            enableOrDisableButton(prevButton, false)
            enableOrDisableButton(nextButton, false)
            return
        }

        if (routineObject.exercises.size == 1) {
            enableOrDisableButton(prevButton, false)
            enableOrDisableButton(nextButton, false)
            return
        }

        if (currentExerciseIndex == 0) {
            enableOrDisableButton(prevButton, false)
        } else {
            enableOrDisableButton(prevButton, true)
        }

        if (currentExerciseIndex == routineObject.exercises.size - 1) {
            enableOrDisableButton(nextButton, false)
        } else {
            enableOrDisableButton(nextButton, true)
        }
    }

    /**
     * Sets isEnabled and isClickable property as the given value of the given button
     */
    private fun enableOrDisableButton(button: Button, value: Boolean) {
        button.isEnabled = value
        button.isClickable = value
    }

    /**
     * Stops the timer and play button whenever exercise is changed
     */
    private fun onExerciseChange() {
        pauseTimer()
        val playPauseButton = findViewById<Button>(R.id.playPauseButton)
        playPauseButton.setText(R.string.play_button_text)
    }

    /**
     * Sets the timer value
     */
    private fun setTimer(timeInSeconds: Long) {
        currentTimer = object : CountDownTimer(timeInSeconds * 1000, 1000) {
            override fun onTick(milliSecsUntilFinished: Long) {
                updateTimerValue(milliSecsUntilFinished / 1000)
            }
            override fun onFinish() {

            }
        }
    }

    /**
     * Loads the on screen timer as per the given time in seconds
     */
    private fun updateTimerValue(timeInSeconds: Long) {
        updateTime(timeInSeconds)
        currentTimerValue = timeInSeconds

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
    private fun updateTime(timeInSeconds: Long) {
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
            currentExerciseTextView.setText("Current Exercise: " + routineData.exercises[currentExerciseIndex].name).toString()
        }
    }

    /**
     * Starts the current set timer
     */
    private fun resumeTimer() {
        currentTimer.start()
    }

    /**
     * Cancels the timer and replaces the current timer with current time value
     */
    private fun pauseTimer() {
        currentTimer.cancel()
        setTimer(currentTimerValue)
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
