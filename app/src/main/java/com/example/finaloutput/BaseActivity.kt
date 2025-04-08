package com.example.finaloutput

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

open class BaseActivity : AppCompatActivity() {
    protected open val auth = Firebase.auth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onStart() {
        super.onStart()
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                redirectToLogin()
            }
        }
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    protected fun redirectToLogin() {
        startActivity(Intent(this, login::class.java))
        finish()
    }
}