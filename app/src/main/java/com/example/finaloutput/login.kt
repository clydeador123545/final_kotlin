package com.example.finaloutput

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerRole = findViewById<Spinner>(R.id.spinnerRole)

        // List of roles for the dropdown
        val roles = arrayOf("Patient", "Doctor", "Nurse", "Admin")

        // Adapter to display items in the spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        spinnerRole.adapter = adapter

        // Set an item selected listener to handle the selected item
        spinnerRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedRole = parentView?.getItemAtPosition(position).toString()
                Toast.makeText(this@login, "Selected Role: $selectedRole", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle no selection if necessary
            }
        }


        val signUpLink = findViewById<TextView>(R.id.sign_up_link)

        signUpLink.setOnClickListener {
            // Open Log In page
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }



        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener{
            val intent = Intent(this, p_dashboard::class.java)
            startActivity(intent)
        }


    }
}