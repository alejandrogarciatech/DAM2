package com.example.feedback2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val nombrePais = intent.getStringExtra("nombrePais")
        binding.nombrePaisTextView.text = "$nombrePais"

        val habitantes = getHabitantes(nombrePais)
        binding.habitantesTextView.text = "Número de habitantes: \n\n $habitantes \n en 2021"
    }

    private fun getHabitantes(nombrePais: String?): String {
        return when (nombrePais) {
            "Belize" -> "400.031"
            "Costa Rica" -> "5,154 millones"
            "Cuba" -> "11,26 millones"
            "El Salvador" -> "6,314 millones"
            "Guatemala" -> "17,11 millones"
            "Honduras" -> "10,28 millones"
            "Jamaica" -> "2,828 millones"
            "México" -> "126,7 millones"
            "Nicaragua" -> "6,851 millones"
            "Panamá" -> "4,351 millones"
            else -> "Desconocido"
        }
    }
}


