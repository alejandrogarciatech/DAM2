package com.example.feedback2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.example.feedback2.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.paises.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = binding.root.findViewById<RadioButton>(checkedId)
            val imageId = when (radioButton.text.toString()) {
                "Belize" -> R.drawable.belize_flag
                "Costa Rica" -> R.drawable.costa_rica_flag
                "Cuba" -> R.drawable.cuba_flag
                "El Salvador" -> R.drawable.el_salvador_flag
                "Guatemala" -> R.drawable.guatemala_flag
                "Honduras" -> R.drawable.honduras_flag
                "Jamaica" -> R.drawable.jamaica_flag
                "México" -> R.drawable.mexico_flag
                "Nicaragua" -> R.drawable.nicaragua_flag
                "Panamá" -> R.drawable.panama_flag
                else -> R.drawable.imagen_defecto
            }
            binding.bandera.setImageResource(imageId)
        }
    }
}


