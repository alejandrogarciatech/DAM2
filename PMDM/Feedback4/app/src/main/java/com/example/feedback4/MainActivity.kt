package com.example.feedback4

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.lifecycleScope
import com.example.feedback4.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textoConsulta.setText("")

        binding.botonGuardar.setOnClickListener {
            val nombreDtaStore = binding.editNombreDataStore.text.toString()
            val nombre = binding.editNombre.text.toString()
            val edad = binding.editEdad.text.toString().toInt()
            val telefono = binding.editTelefono.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                guardarDatos(nombre, edad, telefono, nombreDtaStore)
            }
        }

        binding.botonConsultar.setOnClickListener {
            val nombreDataStore = binding.editNombreDataStore.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                consultarDatos(nombreDataStore)
            }
        }
    }

    suspend fun guardarDatos(nombre: String, edad: Int, telefono: String, nombreDataStore: String) {
        val usuario = Usuario(nombre, edad, telefono)
        val usuarioJson = Json.encodeToString(usuario)
        val archivo = applicationContext.preferencesDataStoreFile(name = nombreDataStore)
        val file = File(filesDir, "$nombreDataStore.txt")
        file.writeText(usuario.toString())
        dataStore.edit {
            it[stringPreferencesKey("usuario")] = usuarioJson
        }
    }

    suspend fun consultarDatos(nombreDataStore: String) {
        val archivo = applicationContext.preferencesDataStoreFile(name = nombreDataStore)
        val usuarioJson = dataStore.data.map {
            it[stringPreferencesKey("usuario")] ?: ""
        }.first()

        if (usuarioJson.isNotEmpty()) {
            val usuario = Json.decodeFromString<Usuario>(usuarioJson)
            withContext(Dispatchers.Main) {
                binding.textoConsulta.text =
                    "Archivo: ${nombreDataStore}" + "\n" +
                            "Nombre: ${usuario.nombre}" + "\n"  +
                            "Edad: ${usuario.edad}" + "\n"  +
                            "Teléfono: ${usuario.telefono}"
            }
        } else {
            withContext(Dispatchers.Main) {
                binding.textoConsulta.text = "No se encontraron datos de usuario"
            }
        }
    }
}
