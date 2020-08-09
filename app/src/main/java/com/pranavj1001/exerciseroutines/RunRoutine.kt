package com.pranavj1001.exerciseroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson

class RunRoutine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_routine)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(R.string.app_intent_key.toString())
        runRoutine(message)
    }

    // disable animation which is triggered when we switch to another activity
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    /**
     * Runs the Routine
     */
    private fun runRoutine(routineName: String) {
        val routineData = getRoutine(routineName)
    }

    private fun getRoutine(routineName: String): RoutineBody {
        val routineData: String = applicationContext.openFileInput(routineName).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
        return Gson().fromJson(routineData, RoutineBody::class.java)
    }
}
