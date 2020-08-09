package com.pranavj1001.exerciseroutines

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
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

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(applicationContext, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    Toast.makeText(applicationContext, "clicked!", Toast.LENGTH_SHORT).show()
                    runRoutine(position)
                }

                override fun onLongItemClick(view: View?, position: Int) {
                    Toast.makeText(applicationContext, "long clicked!", Toast.LENGTH_SHORT).show()
                    editExerciseRoutine(position)
                }
            })
        )
    }

    /**
     * Launches a new activity which creates a routine
     */
    fun addExerciseRoutine(view: View) {
        val intent = Intent(this, AddExerciseRoutine::class.java)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    /**
     * Launches a new activity which edits a routine
     */
    fun editExerciseRoutine(position: Int) {
        val intent = Intent(this, AddExerciseRoutine::class.java).apply {
            putExtra(R.string.app_intent_key.toString(), routines[position].name)
        }
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    /**
     * Launches a new activity which deals with launching the new activity
     */
    private fun runRoutine(position: Int) {
        val intent = Intent(this, RunRoutine::class.java).apply {
            putExtra(R.string.app_intent_key.toString(), routines[position].name)
        }
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
