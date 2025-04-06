
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finaloutput.R
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_appointment)

        val acceptButton = findViewById<Button>(R.id.btnAccept)
        val rescheduleButton = findViewById<Button>(R.id.btnReschedule)

        acceptButton.setOnClickListener {
            Toast.makeText(this, "Appointment Accepted", Toast.LENGTH_SHORT).show()
        }

        rescheduleButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                Toast.makeText(this, "Rescheduled to $selectedDate", Toast.LENGTH_LONG).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
