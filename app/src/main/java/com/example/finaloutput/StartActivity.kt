package com.example.finaloutput

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class StartActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)
        auth = Firebase.auth
        database = Firebase.database.reference

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            database.child("users").child(uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val role = snapshot.child("userType").value?.toString()?.lowercase()

                    val intent = when (role) {
                        "patient" -> Intent(this@StartActivity, p_dashboard::class.java)
                        "doctor" -> Intent(this@StartActivity, DoctorDashboard::class.java)
                        "nurse" -> Intent(this@StartActivity, NurseDashboard::class.java)
                        "admin" -> Intent(this@StartActivity, AdminDashboard::class.java)
                        else -> Intent(this@StartActivity, login::class.java) // fallback
                    }

                    startActivity(intent)
                    finish()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@StartActivity, "Error loading user data", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@StartActivity, login::class.java))
                    finish()
                }
            })
        } else {
            startActivity(Intent(this, login::class.java))
            finish()
        }
    }
}

