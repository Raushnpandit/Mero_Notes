package com.example.meronotes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import com.google.gson.Gson
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var editTextGoal: EditText
    private lateinit var editTextTask: EditText
    private lateinit var textViewDate: TextView
    private lateinit var buttonSave: Button
    private lateinit var goalList: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var habitAdapter: HabitAdapter

    private val goals = mutableListOf<Habit>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        editTextGoal = view.findViewById(R.id.editTextGoal)
        editTextTask = view.findViewById(R.id.editTextTask)
        textViewDate = view.findViewById(R.id.textViewDate)
        buttonSave = view.findViewById(R.id.buttonSave)
        goalList = view.findViewById(R.id.goalList)

        sharedPreferences = requireContext().getSharedPreferences("GOALS", Context.MODE_PRIVATE)

        goalList.layoutManager = LinearLayoutManager(requireContext())
        habitAdapter =
            HabitAdapter(goals, requireContext(), object : HabitAdapter.OnDeleteClickListener {
                override fun onDeleteClick(goal: Habit) {
                    deleteGoal(goal)
                }
            })

        habitAdapter.setOnItemClickListener(object : HabitAdapter.OnItemClickListener {
            override fun onItemClick(goal: Habit) {
                //TODO: Implement the on item click listener here
            }
        })

        goalList.adapter = habitAdapter


        // Set up the date picker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        textViewDate.setOnClickListener {
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener { date ->
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            textViewDate.text = sdf.format(Date(date))
        }

        buttonSave.setOnClickListener {
            val goalText = editTextGoal.text.toString().trim()
            val taskText = editTextTask.text.toString().trim()
            val dateText = textViewDate.text.toString().trim()

            if (goalText.isEmpty() || taskText.isEmpty() || dateText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val goal = Habit(goalText, taskText, dateText)
                goals.add(goal)
                habitAdapter.notifyDataSetChanged()

                // Save the goal to SharedPreferences
                val goalJson = Gson().toJson(goal)
                val editor = sharedPreferences.edit()
                editor.putString(goalText, goalJson)
                editor.apply()

                // Clear the input fields
                editTextGoal.text.clear()
                editTextTask.text.clear()
                textViewDate.text = getString(R.string.select_a_date)

                Toast.makeText(requireContext(), "Note saved", Toast.LENGTH_SHORT).show()
            }
        }

        // Load saved goals from SharedPreferences
        val savedGoals = sharedPreferences.all.values
        savedGoals.forEach {
            val goalJson = it as String
            val goal = Gson().fromJson(goalJson, Habit::class.java)
            goals.add(goal)
        }
        habitAdapter.notifyDataSetChanged()

        return view
    }

    private fun deleteGoal(goal: Habit) {
        goals.remove(goal)
        habitAdapter.notifyDataSetChanged()

        // Delete the goal from SharedPreferences
        val editor = sharedPreferences.edit()
        editor.remove(goal.title)
        editor.apply()

        Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show()
    }
}