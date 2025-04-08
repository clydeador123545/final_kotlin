package com.example.finaloutput

import com.example.finaloutput.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.app.Application
import com.google.firebase.FirebaseApp


class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference // Correct type
    private val calendar = Calendar.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

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

            val datePicker = DatePickerDialog(

                this,
                { _, year, month, day ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    updateBirthdateInView()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
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


//        Signup
        val signUpBtn = findViewById<Button>(R.id.signupBtn)
        signUpBtn.setOnClickListener {
            registerUser()
        }

    }

    private fun updateBirthdateInView() {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        findViewById<EditText>(R.id.birthdate_input).setText(dateFormat.format(calendar.time))
    }

    private fun registerUser() {
        val fullName = findViewById<EditText>(R.id.FullName).text.toString().trim()
        val age = findViewById<EditText>(R.id.Age).text.toString().trim()
        val gender = findViewById<Spinner>(R.id.sex_picker).selectedItem.toString()
        val birthdate = findViewById<EditText>(R.id.birthdate_input).text.toString().trim()
        val username = findViewById<EditText>(R.id.Username).text.toString().trim()
        val email = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.Email).text.toString().trim()
        val countryCode = findViewById<Spinner>(R.id.spinnerCountryCode).selectedItem.toString()
        val phoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber).text.toString().trim()
        val password = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.Password).text.toString().trim()

        if (validateInputs(fullName, age, gender, birthdate, username, email, phoneNumber, password)) {
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User created successfully, now store additional info
                        val userId = auth.currentUser?.uid
                        val userMap = hashMapOf(
                            "fullName" to fullName,
                            "age" to age,
                            "gender" to gender,
                            "birthdate" to birthdate,
                            "username" to username,
                            "email" to email,
                            "phoneNumber" to "$countryCode$phoneNumber",
                            "userType" to "patient" // Added user type as per your "(For Patient only)" text
                        )

                        if (userId != null) {
                            database.child("users").child(userId).setValue(userMap)
                                .addOnCompleteListener { dbTask ->
                                    findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

                                    if (dbTask.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "Registration successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(this, login::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Failed to save user data: ${dbTask.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    } else {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Registration failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun validateInputs(
        fullName: String,
        age: String,
        gender: String,
        birthdate: String,
        username: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Boolean {
        if (fullName.isEmpty()) {
            findViewById<EditText>(R.id.FullName).error = "Full name is required"
            return false
        }
        if (age.isEmpty()) {
            findViewById<EditText>(R.id.Age).error = "Age is required"
            return false
        }
        if (gender == "Select Gender") {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
            return false
        }
        if (birthdate.isEmpty()) {
            findViewById<EditText>(R.id.birthdate_input).error = "Birthdate is required"
            return false
        }
        if (username.isEmpty()) {
            findViewById<EditText>(R.id.Username).error = "Username is required"
            return false
        }
        if (email.isEmpty()) {
            findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.Email).error = "Email is required"
            return false
        }
        if (phoneNumber.isEmpty()) {
            findViewById<EditText>(R.id.editTextPhoneNumber).error = "Phone number is required"
            return false
        }
        if (password.length < 6) {
            findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.Password).error = "Password must be at least 6 characters"
            return false
        }
        return true
    }
}