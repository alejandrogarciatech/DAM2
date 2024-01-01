package com.example.feedback4

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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

// Definimos la extensión de propiedad llamada dataStore, que permite acceder al DataStore de preferencias.
// Lo creamos fuera de la clase principal, como elemento externo, para que esté disponible en cualquier parte del proyecto. Accede a un fichero
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    // Declaramos una variable binding para acceder a la vista de interfaz de usuario
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lógica del botón GUARDAR
        binding.botonGuardar.setOnClickListener {

            //Obtenemos los datos desde los EditText
            val nombreDtaStore = binding.editNombreDataStore.text.toString()
            val nombre = binding.editNombre.text.toString()
            val edad = binding.editEdad.text.toString()
            val telefono = binding.editTelefono.text.toString()

            // Verificamos que todos los campos estén completos
            if (nombre.isNotEmpty() && edad.isNotEmpty() && telefono.isNotEmpty() && nombreDtaStore.isNotEmpty()) {

                // Llamamos a la funcion guardarDatos. Se mete en un lifecycleScope, que decide en qué ámbito del ciclo de vida se ejecuta cada cosa
                // Se lanza manualmente al hilo IO (entradas y salidas)
                lifecycleScope.launch(Dispatchers.IO) {
                    guardarDatos(nombre, edad.toInt(), telefono, nombreDtaStore)
                }

                // Vaciamos los EditText después de guardar los datos
                binding.editNombreDataStore.text.clear()
                binding.editNombre.text.clear()
                binding.editEdad.text.clear()
                binding.editTelefono.text.clear()

            } else {
                // En caso de que algún campo esté vacío, mostramos un mensaje de error al usuario
                if (nombre.isEmpty()) {
                    binding.editNombre.error = "El nombre no puede estar vacío"
                }
                if (edad.isEmpty()) {
                    binding.editEdad.error = "La edad no puede estar vacía"
                }
                if (telefono.isEmpty()) {
                    binding.editTelefono.error = "El teléfono no puede estar vacío"
                }
                if (nombreDtaStore.isEmpty()) {
                    binding.editNombreDataStore.error = "El nombre del archivo no puede estar vacío"
                }
            }
        }

        // Lógica del botón CONSULTAR
        binding.botonConsultar.setOnClickListener {
            binding.editNombreDataStore.text.toString()

            // Llamamos a la funcion consultarDatos. Se mete en un lifecycleScope, y se lanza al hilo IO
            lifecycleScope.launch(Dispatchers.IO) {
                consultarDatos()
            }
        }
    }

    // Lógica de la función guardarDatos. Solo puede ser llamada desde una corrutina
    private suspend fun guardarDatos(
        nombre: String,
        edad: Int,
        telefono: String,
        nombreDataStore: String
    ) {
        //Creamos un objeto Usuario con los datos proporcionados
        val usuario = Usuario(nombre, edad, telefono)
        // y lo convertimos a formato JSON para serializar los datos
        val usuarioJson = Json.encodeToString(usuario)

        // Creamos un archivo en el directorio de ficheros de la app y le damos el nombre que ha introducido el usuario
        val archivo = File(filesDir, "$nombreDataStore.txt")

        // Escribimos en el archivo los datos del objeto Usuario en formato String
        archivo.writeText(usuario.toString())

        // Guardamos el objeto JSON en el DataStore. Establecemos el par clave-valor
        dataStore.edit {
            it[stringPreferencesKey("usuario")] = usuarioJson
        }
    }

    // Lógica de la función consultarDatos. Solo puede ser llamada desde una corrutina
    @SuppressLint("SetTextI18n")
    private suspend fun consultarDatos() {

        // Obtenemos el valor asociado con la clave "usuario" del DataStore y lo almacenamos en la variable usuarioJson
        val usuarioJson = dataStore.data.map {
            // Si no hay ningún valor asociado a esta clave, se asigna una cadena vacía
            it[stringPreferencesKey("usuario")] ?: ""
        }.first()

        // Si el JSON del objeto Usuario no está vacío, se decodifica el objeto a su forma original
        if (usuarioJson.isNotEmpty()) {
            val usuario = Json.decodeFromString<Usuario>(usuarioJson)
            // Y se actualiza el TextView con los datos del usuario
            withContext(Dispatchers.Main) {
                binding.textoConsulta.text =
                    "Nombre: ${usuario.nombre}\nEdad: ${usuario.edad}\nTeléfono: ${usuario.telefono}"
            }
        } else { // Se muestra un mensaje en el TextView si no se encuentran datos
            withContext(Dispatchers.Main) {
                binding.textoConsulta.text = "No se encontraron datos de usuario"
            }
        }
    }
}
