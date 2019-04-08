package com.example.calories

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        this.setTitle("ADD FOOD")

        val name: Editable = editText1.text
        val calories: Editable = editText2.text

        button.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(name.isBlank() || calories.isBlank()) {
                    Toast.makeText(this@SecondActivity, "Your input is empty", Toast.LENGTH_SHORT).show()
                }
                else if(calories.length > 5) {
                    Toast.makeText(this@SecondActivity, "Too many calories! That's impossible.", Toast.LENGTH_SHORT).show()
                }
                else {
                    val intent = Intent()
                    intent.putExtra("name", name.toString().trim())
                    intent.putExtra("calories", calories.toString().toInt())

                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        })
    }
}
