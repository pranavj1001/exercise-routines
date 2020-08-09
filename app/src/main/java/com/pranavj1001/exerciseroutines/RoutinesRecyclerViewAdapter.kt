package com.pranavj1001.exerciseroutines

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RoutinesRecyclerViewAdapter(private val routines: Array<RoutineBody>) :
    RecyclerView.Adapter<RoutinesRecyclerViewAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // each data item is just a string in this case
        var listItemName: TextView = view.findViewById(R.id.listItemName)
        var listItemDuration: TextView = view.findViewById(R.id.listItemDuration)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RoutinesRecyclerViewAdapter.ViewHolder {
        // create a new view
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_routine_list, parent, false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.listItemName.text = routines[position].name
        if (routines[position].time.isEmpty()) {
            holder.listItemDuration.text = "No time related information found."
        } else {
            holder.listItemDuration.text = routines[position].time
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = routines.size
}

class ExercisesRecyclerViewAdapter(private var exercises: Array<ExerciseBody>) :
    RecyclerView.Adapter<ExercisesRecyclerViewAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // each data item is just a string in this case
        var listItemName: TextView = view.findViewById(R.id.exerciseName)
        var listItemDuration: TextView = view.findViewById(R.id.exerciseDuration)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ExercisesRecyclerViewAdapter.ViewHolder {
        // create a new view
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_exercise_list, parent, false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.listItemName.text = exercises[position].name
        holder.listItemDuration.text = exercises[position].time

        holder.listItemName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                exercises[position].name = holder.listItemName.text.toString()
            }
        })

        holder.listItemDuration.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                exercises[position].time = holder.listItemDuration.text.toString()
            }
        })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = exercises.size

    fun setExercises(exercisesData: Array<ExerciseBody>) {
        exercises = exercisesData
    }

    fun getExercises() = exercises
}

class RecyclerItemClickListener(
    context: Context?,
    recyclerView: RecyclerView,
    private val mListener: OnItemClickListener?
) :
    RecyclerView.OnItemTouchListener {

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onLongItemClick(view: View?, position: Int)
    }

    var mGestureDetector: GestureDetector
    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    init {
        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && mListener != null) {
                    mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }
}