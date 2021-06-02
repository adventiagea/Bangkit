package com.dicoding.picodiploma.libas

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.libas.databinding.ActivityPredictionBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dateText = binding.waktuPrediksi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.now()
            val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            dateText.text = dateFormat.format(date)
        } else {
            fun Date.toString(format : String, locale: Locale = Locale.getDefault()): String {
                val dateFormat = SimpleDateFormat(format, locale)
                return dateFormat.format(this)
            }
            fun getCurrentDateTime(): Date {
                return Calendar.getInstance().time
            }

            val date = getCurrentDateTime()
            val dateInString = date.toString("dd-MM-yyyy")

            dateText.text = dateInString
        }

    }
}