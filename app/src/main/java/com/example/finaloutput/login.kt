package com.example.finaloutput

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.example.finaloutput.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        auth = Firebase.auth
        database = Firebase.database.reference

        if (auth.currentUser != null) {
            // Get role from SharedPreferences or database
            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val role = prefs.getString("user_role", "patient") ?: "patient"
            redirectToDashboard(role)
            return  // Important: Exit onCreate after redirect
        }

        // Setup role spinner
        val roles = listOf("Select Role", "Patient", "Doctor", "Nurse", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRole.adapter = adapter

        binding.loginBtn.setOnClickListener {
            val username = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val role = binding.spinnerRole.selectedItem.toString()

            if (validateInputs(username, password, role)) {
                binding.progressBar.visibility = View.VISIBLE
                binding.loginBtn.isEnabled = false
                loginUser(username, password, role)
            }
        }

//        auth.currentUser?.let { user ->
//            // User is logged in, redirect to appropriate dashboard
//            redirectToDashboard(getUserRole(user.uid))
//        } ?: run {
//            // No user logged in, show login screen
//            startActivity(Intent(this, login::class.java))
//            finish()
//        }




        binding.signUpLink.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }

    private fun getUserRole(uid: String): String {
        // Implement your logic to get user role from database
        // This is just a placeholder
        return "patient"
    }

    private fun validateInputs(username: String, password: String, role: String): Boolean {
        var isValid = true
        binding.username.error = null
        binding.password.error = null

        if (username.isEmpty()) {
            binding.username.error = "Username required"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.password.error = "Password required"
            isValid = false
        } else if (password.length < 6) {
            binding.password.error = "Password too short"
            isValid = false
        }

        if (role == "Select Role") {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun loginUser(username: String, password: String, role: String) {
        // Your existing login implementation
        database.child("users")
            .orderByChild("username")
            .equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userEmail = userSnapshot.child("email").value.toString()
                            val userRole = userSnapshot.child("userType").value.toString()

                            if (userRole.equals(role, ignoreCase = true)) {
                                auth.signInWithEmailAndPassword(userEmail, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Save user role to SharedPreferences
                                            saveUserRole(userRole)
                                            redirectToDashboard(userRole)
                                        } else {
                                            handleLoginError(task.exception)
                                        }
                                    }
                                return
                            }
                        }
                    }
                    handleLoginError(Exception("Invalid credentials"))
                }

                override fun onCancelled(error: DatabaseError) {
                    handleLoginError(Exception("Database error"))
                }
            })
    }

    private fun saveUserRole(role: String) {
        getSharedPreferences("user_prefs", MODE_PRIVATE).edit {
            putString("user_role", role)
            apply()
        }
    }

    private fun handleLoginResult(task: Task<AuthResult>, role: String) {
        binding.progressBar.visibility = View.GONE
        binding.loginBtn.isEnabled = true

        if (task.isSuccessful) {
            redirectToDashboard(role)
        } else {
            handleLoginError(task.exception)
        }
    }

    private fun handleLoginError(exception: Exception?) {
        binding.progressBar.visibility = View.GONE
        binding.loginBtn.isEnabled = true
        Toast.makeText(this, "Login failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
    }

    private fun redirectToDashboard(role: String) {
        val intent = when (role.lowercase()) {
            "patient" -> Intent(this, p_dashboard::class.java)
            "doctor" -> Intent(this, DoctorDashboard::class.java)
            "nurse" -> Intent(this, NurseDashboard::class.java)
            "admin" -> Intent(this, AdminDashboard::class.java)
            else -> Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}