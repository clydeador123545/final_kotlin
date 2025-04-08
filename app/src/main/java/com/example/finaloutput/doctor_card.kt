package com.example.finaloutput

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DoctorListActivity : AppCompatActivity() {

    private lateinit var doctorContainer: LinearLayout

    // List of doctors to display
    private val allDoctors = listOf(
        Doctor("Dr. John Smith", "Cardiologist", "johnsmith@medmail.com"),
        Doctor("Dr. Sarah Brown", "Pulmonologist", "sarahbrown@medmail.com"),
        Doctor("Dr. Alex Taylor", "Cardiologist", "alextaylor@medmail.com"),
        Doctor("Dr. Emily White", "Pulmonologist", "emilywhite@medmail.com")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)

        doctorContainer = findViewById(R.id.doctorContainer)

        // Filter buttons
        val btnAll = findViewById<Button>(R.id.btnAll)
        val btnCardiologist = findViewById<Button>(R.id.btnCardiologist)
        val btnPulmonologist = findViewById<Button>(R.id.btnPulmonologist)

        // Show all doctors by default
        showDoctors(allDoctors)

        btnAll.setOnClickListener {
            showDoctors(allDoctors)
        }

        btnCardiologist.setOnClickListener {
            showDoctors(allDoctors.filter { it.specialty == "Cardiologist" })
        }

        btnPulmonologist.setOnClickListener {
            showDoctors(allDoctors.filter { it.specialty == "Pulmonologist" })
        }
    }

    private fun showDoctors(doctors: List<Doctor>) {
        // Clear any existing doctor cards
        doctorContainer.removeAllViews()

        // Add a card for each doctor
        for (doctor in doctors) {
            // Inflate the doctor card layout
            val view = LayoutInflater.from(this).inflate(R.layout.activity_doctor_card, doctorContainer, false)

            // Set doctor details to the respective views
            val name = view.findViewById<TextView>(R.id.tvDoctorName)
            val specialty = view.findViewById<TextView>(R.id.tvSpeciality)
            val email = view.findViewById<TextView>(R.id.tvEmail)
            val bookBtn = view.findViewById<Button>(R.id.btnBook)

            name.text = doctor.name
            specialty.text = doctor.specialty
            email.text = doctor.email

            // Set the "Book" button action
            bookBtn.setOnClickListener {
                Toast.makeText(this, "Booked with ${doctor.name}", Toast.LENGTH_SHORT).show()
            }

            // Add the doctor card to the container
            doctorContainer.addView(view)
        }
    }
}
