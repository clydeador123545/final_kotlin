//package com.example.finaloutput
//
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//
//class booking : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_booking)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        // Setup RecyclerView
//        doctorAdapter = DoctorAdapter(doctorList)
//        doctorsRecyclerView.apply {
//            layoutManager = LinearLayoutManager(this@BookingActivity)
//            adapter = doctorAdapter
//        }
//
//        // Fetch doctors from Firebase
//        fetchDoctors()
//
//        // Setup search functionality
//        searchView.addTextChangedListener { text ->
//            doctorAdapter.filter.filter(text)
//        }
//
//        // Setup filter chips
//        filterChipGroup.setOnCheckedChangeListener { group, checkedId ->
//            val chip = group.findViewById<Chip>(checkedId)
//            chip?.text?.let {
//                doctorAdapter.filterBySpecialty(it.toString())
//            }
//        }
//    }
//
//    private fun fetchDoctors() {
//        val database = FirebaseDatabase.getInstance()
//        val doctorsRef = database.getReference("doctors")
//
//        doctorsRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                doctorList.clear()
//                for (doctorSnapshot in snapshot.children) {
//                    val doctor = doctorSnapshot.getValue(Doctor::class.java)
//                    doctor?.let { doctorList.add(it) }
//                }
//                doctorAdapter.updateData(doctorList)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//            }
//        })
//    }
//}