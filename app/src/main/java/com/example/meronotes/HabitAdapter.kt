package com.example.meronotes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HabitAdapter(
    private val goalList: MutableList<Habit>,
    private val context: Context,
    private val onDeleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(goal: Habit)
    }

    interface OnItemClickListener {
        fun onItemClick(goal: Habit)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        onItemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewGoalTitle: TextView = view.findViewById(R.id.textViewGoalTitle)
        val textViewGoalDesc: TextView = view.findViewById(R.id.textViewGoalDesc)
        val textViewGoalDate: TextView = view.findViewById(R.id.textViewGoalDate)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.goal_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal = goalList[position]

        holder.textViewGoalTitle.text = goal.title
        holder.textViewGoalDesc.text = goal.description

        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val dateString = goal.date
        try {
            val date = dateFormat.parse(dateString)
            holder.textViewGoalDate.text = dateFormat.format(date)
        } catch (e: ParseException) {
            // handle invalid date string
            holder.textViewGoalDate.text = ""
        }

        holder.buttonDelete.setOnClickListener {
            onDeleteClickListener.onDeleteClick(goal)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(goal)
        }
    }


    override fun getItemCount(): Int {
        return goalList.size
    }
}
