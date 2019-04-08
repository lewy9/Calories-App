package com.example.calories

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.calories.model.Food
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

// Request Code for navigating to the second activity
private const val ADD_FOOD_REQUEST = 1

class MainActivity : AppCompatActivity() {

    private var foodList = ArrayList<Food>()
    lateinit var defaultCal: String
    private val listView by lazy { this.findViewById<ListView>(R.id.listView) }
    private val adapter by lazy { FoodAdapter(this, R.layout.listview_layout, foodList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Call the dialog function to ask user input their initiate calories at start
        submitCal()

        // Click on "add food" button and navigate to the second activity
        button1.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivityForResult(intent, ADD_FOOD_REQUEST)
            }
        })

        // Delete single food in the listView and update remain calories and total calories
        listView.setOnItemClickListener { parent, view, position, id ->
            val food: Food = adapter.getItem(position)
            val builder = AlertDialog.Builder(this)
            builder
                .setCancelable(false)
                .setMessage("Are you sure to delete this food: ${food.name}?")
                .setTitle("Delete item")
                .setPositiveButton("Yes") {dialog, which ->
                    // Delete
                    adapter.remove(food)
                    adapter.notifyDataSetChanged()
                    // Update remain calories and color
                    var remainCal = textView2.text.toString().toInt()
                    remainCal += food.calorie
                    if (remainCal >= 0)
                        textView2.setTextColor(Color.GRAY)
                    textView2.text = remainCal.toString()
                    // Update total calories
                    var totalCal = textView4.text.toString().toInt()
                    totalCal -= food.calorie
                    textView4.text = totalCal.toString()
                }
                .setNegativeButton("No") { dialog, which -> }
                .show()
        }

        // Clear all the items in the food adapter
        button3.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder
                    .setCancelable(false)
                    .setMessage("Are you sure to clear all the food?")
                    .setTitle("Clear All")
                    .setPositiveButton("Yes") {dialog, which ->
                        // Clear
                        adapter.clear()
                        adapter.notifyDataSetChanged()
                        // Update remain calories and color
                        textView2.setTextColor(Color.GRAY)
                        textView2.text = defaultCal
                        // Update total calories
                        textView4.text = 0.toString()
                    }
                    .setNegativeButton("No") { dialog, which -> }
                    .show()
            }
        })

        // Reset all, requires user to re-input the default calories
        button2.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder
                    .setCancelable(false)
                    .setMessage("Are you sure to reset?")
                    .setTitle("Reset All")
                    .setPositiveButton("Yes") {dialog, which ->
                        // Rest
                        adapter.clear()
                        adapter.notifyDataSetChanged()
                        // Update remain calories and color
                        textView2.setTextColor(Color.GRAY)
                        textView2.text = 0.toString()
                        // Update total calories
                        textView4.text = 0.toString()
                        submitCal()
                    }
                    .setNegativeButton("No") { dialog, which -> }
                    .show()
            }
        })

        listView.adapter = adapter
    }
    // Receive data from the second activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == ADD_FOOD_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                val name = data?.getStringExtra("name")
                val calories = data?.getIntExtra("calories", 0)
                foodList.add(Food(name.toString(), calories!!.toInt()))
                adapter.notifyDataSetChanged()
                // modify total calories
                var totalCal = textView4.text.toString().toInt()
                totalCal += calories.toInt()
                textView4.text = totalCal.toString()
                // modify remaining calories and color
                var remainCal = textView2.text.toString().toInt()
                remainCal -= calories.toInt()
                if(remainCal < 0)
                    textView2.setTextColor(Color.RED)
                textView2.text = remainCal.toString()
            }
        }
    }

    // Create an AlertDialog to ask user to set default calories when start up
    private fun submitCal() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.set_calories, null)
        val input: EditText = dialogView.findViewById(R.id.editText)
        builder.setView(dialogView)
        builder
            .setCancelable(false)
            .setPositiveButton("Submit", null)
        // create a new dialog to avoid original dialog disappeared by default when click on any button
        val alertDialog = builder.create()
        alertDialog.show()
        val btnSubmit = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btnSubmit.setOnClickListener{
            val calories = input.text.toString().trim()
            defaultCal = calories
            if(calories.isBlank()) {
                Toast.makeText(this@MainActivity, "Your input is empty", Toast.LENGTH_SHORT).show()
            }
            else if(calories.length > 5) {
                Toast.makeText(this@MainActivity, "Too many calories! That's impossible.", Toast.LENGTH_SHORT).show()
            }
            else {
                textView2.text = calories.toInt().toString()  // Avoid the case when the first digit is 0
                textView4.text = 0.toString()
                alertDialog.dismiss()
            }
        }
    }
}
