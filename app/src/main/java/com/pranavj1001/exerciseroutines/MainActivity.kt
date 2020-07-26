package com.pranavj1001.exerciseroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var routines: Array<RoutineBody>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadRoutines()

        viewManager = LinearLayoutManager(this)
        viewAdapter = RoutinesRecyclerViewAdapter(routines)

        recyclerView = findViewById<RecyclerView>(R.id.exerciseRoutineList).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    /**
     * Launches a new activity which creates/edits a routine
     */
    fun addExerciseRoutine(view: View) {
        val intent = Intent(this, AddExerciseRoutine::class.java)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    /**
     * Fetches all routines from internal storage
     */
    private fun loadRoutines() {
        val routineNames = applicationContext.fileList()
        routines = emptyArray();
        if (routineNames.isEmpty()) {
            var routineObject = RoutineBody()
            routineObject.name = getString(R.string.empty_list_message)
            routines = routines.plusElement(routineObject)
        } else {
            for (routineName in routineNames) {
                val routineData: String = applicationContext.openFileInput(routineName).bufferedReader().useLines { lines ->
                    lines.fold("") { some, text ->
                        "$some\n$text"
                    }
                }
                val routineObject = Gson().fromJson(routineData, RoutineBody::class.java)
                routines = routines.plusElement(routineObject)
            }
        }
    }

}
