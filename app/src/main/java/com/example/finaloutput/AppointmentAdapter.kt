package com.example.finaloutput


import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.finaloutput.model.Appointment


import java.util.*

class AppointmentAdapter(
    private val context: Context,
    private val appointments: List<Appointment>
) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientImage: ImageView = view.findViewById(R.id.imguser1)
        val patientNumber: TextView = view.findViewById(R.id.tvPatientNumber)
        val patientMessage: TextView = view.findViewById(R.id.tvPatientDescription)
        val acceptButton: Button = view.findViewById(R.id.btnAccept)
        val rescheduleButton: Button = view.findViewById(R.id.btnReschedule)

        val patientImage1: ImageView = view.findViewById(R.id.imguser2)
        val patientNumber1: TextView = view.findViewById(R.id.tvPatientNumber1)
        val patientMessage1: TextView = view.findViewById(R.id.tvPatientDescription1)
        val acceptButton1: Button = view.findViewById(R.id.btnAccept1)
        val rescheduleButton1: Button = view.findViewById(R.id.btnReschedule1)

        val patientImage2: ImageView = view.findViewById(R.id.imguser3)
        val patientNumber2: TextView = view.findViewById(R.id.tvPatientNumber2)
        val patientMessage2: TextView = view.findViewById(R.id.tvPatientDescription2)
        val acceptButton2: Button = view.findViewById(R.id.btnAccept2)
        val rescheduleButton2: Button = view.findViewById(R.id.btnReschedule2)

        val patientImage3: ImageView = view.findViewById(R.id.imguser4)
        val patientNumber3: TextView = view.findViewById(R.id.tvPatientNumber3)
        val patientMessage3: TextView = view.findViewById(R.id.tvPatientDescription3)
        val acceptButton3: Button = view.findViewById(R.id.btnAccept3)
        val rescheduleButton3: Button = view.findViewById(R.id.btnReschedule3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_appointment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = appointments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.patientImage.setImageResource(R.drawable.user1)
        holder.patientImage.setImageResource(appointment.imageResId)
        holder.patientNumber.text = "Patient Number: ${appointment.patientNumber}"
        holder.patientMessage.text = appointment.message

        holder.acceptButton.setOnClickListener {
            Toast.makeText(context, "Accepted ${appointment.patientNumber}", Toast.LENGTH_SHORT).show()
        }

        holder.rescheduleButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selected = "$dayOfMonth/${month + 1}/$year"
                    Toast.makeText(context, "Rescheduled to $selected", Toast.LENGTH_SHORT).show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }
}
