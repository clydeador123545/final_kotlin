package com.example.finaloutput

import com.example.finaloutput.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar


class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val birthdateInput = findViewById<EditText>(R.id.birthdate_input)
        birthdateInput.setOnClickListener { v: View? ->
            val calendar: Calendar = Calendar.getInstance()
            val year: Int = calendar.get(Calendar.YEAR)
            val month: Int = calendar.get(Calendar.MONTH)
            val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view: DatePicker?, year1: Int, month1: Int, dayOfMonth: Int ->
                    val birthdate = (month1 + 1).toString() + "/" + dayOfMonth + "/" + year1
                    birthdateInput.setText(birthdate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }


        val spinnerCountryCode = findViewById<Spinner>(R.id.spinnerCountryCode)
        val editTextPhoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber)

        // Country codes (you can add more)
        val countryCodes = arrayOf("+63 (PH)", "+1 (US)", "+44 (UK)", "+91 (IN)")

        // Adapter for Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countryCodes)
        spinnerCountryCode.adapter = adapter

        // Get selected country code
        spinnerCountryCode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCode = countryCodes[position].split(" ")[0]
                Toast.makeText(this@SignUp, "Selected: $selectedCode", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        val logInLink = findViewById<TextView>(R.id.log_in_link)

        logInLink.setOnClickListener {
            // Open Log In page
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }

    }
}