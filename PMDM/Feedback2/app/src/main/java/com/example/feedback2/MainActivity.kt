package com.example.feedback2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val paises = arrayOf("Belize", "Costa Rica", "Cuba", "El Salvador", "Guatemala",
            "Honduras", "Jamaica", "México", "Nicaragua", "Panamá")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, paises)
        binding.paisesListView.adapter = adapter

        binding.paisesListView.setOnItemLongClickListener { _, _, position, _ ->
            val paisSeleccionado = paises[position]
            binding.paisSeleccionadoTextView.text = paisSeleccionado
            true
        }

        binding.paisesListView.setOnItemClickListener { _, _, position, _ ->
            val paisSeleccionado = paises[position]
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("nombrePais", paisSeleccionado)
            startActivity(intent)
        }

        binding.botonBanderas.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}