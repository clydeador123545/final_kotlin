package com.example.finaloutput

import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class p_dashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val hamburgerIcon: View = findViewById(R.id.hamburger_icon)

        // Open drawer when hamburger is clicked
        hamburgerIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Close drawer when tapping outside
        drawerLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Handle menu item clicks
        navView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {

                }
                R.id.nav_profile -> {
                    showProfileBottomSheet()
                }
                R.id.nav_logout -> {
                    logoutUser()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START) // Close after click
            true
        }

        var sheet:View = findViewById(R.id.sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(sheet)

        bottomSheetBehavior.apply{
            peekHeight=500
            this.state=BottomSheetBehavior.STATE_COLLAPSED
        }







    }


    private fun showProfileBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.profile, null)
        bottomSheetDialog.setContentView(view)



        val closeButton = view.findViewById<ImageView>(R.id.btn_close)
        closeButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }


//        val textView = findViewById<TextView>(R.id.birthdate_picker)
//        textView.background = ContextCompat.getDrawable(this, R.drawable.text_view_border_bottom)

        // Find the Spinner
        val spinner: Spinner = view.findViewById(R.id.spinner_sex)

        // Define dropdown items (Male, Female)
        val genderOptions = arrayOf("Male", "Female")

        // Set up adapter for dropdown list
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderOptions)
        spinner.adapter = adapter

        // Handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedGender = genderOptions[position]
                Toast.makeText(this@p_dashboard, "Selected: $selectedGender", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        bottomSheetDialog.show()
    }

    private fun logoutUser() {
        auth.signOut()
        startActivity(Intent(this, login::class.java))
        finish()
        Toast.makeText(this@p_dashboard, "Logout Successful", Toast.LENGTH_SHORT).show()

    }

    private fun clearUserData() {
        getSharedPreferences("user_prefs", MODE_PRIVATE).edit {
            clear()
            apply()
        }
    }


}