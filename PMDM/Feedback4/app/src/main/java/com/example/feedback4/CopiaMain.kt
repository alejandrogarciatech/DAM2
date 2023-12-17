/*package com.example.feedback4

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/*Hay dos hilos de ejecución:
MAIN -> Hilo principal. Se encarga de del renderizado y los listener. No se debe sobrecargar con más funciones
IO -> Hilo de entradas y salidas, transacción de datos (leer y escribir en ficheros, acceder a bbdd, capturar elementos de un JSON)
 */


//Crear la variable Data fuera de la clase principal, como elemento externo, para que esté disponible en cualquier parte del proyecto. Accede a un fichero
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
//Asignador
//Patrón singleton
//Delegado: elemento que permite gestionar de forma única distintos tipos de variable


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombreArchivo = binding.editArchivo.text.toString()

        binding.textoConsulta.setText("")

        binding.botonGuardar.setOnClickListener {
            val nombre = binding.editNombre.text.toString()
            val edadText = binding.editEdad.text.toString()
            val telefono = binding.editTelefono.text.toString()

            if (nombre.isNotEmpty() && edadText.isNotEmpty() && telefono.isNotEmpty()) {
                val edad = edadText.toInt()

                // Resto del código para guardar los datos
                lifecycleScope.launch(Dispatchers.IO) {
                    guardarDatos(nombre, edad, telefono)
                }

                // Vaciar los EditText después de guardar los datos
                binding.editNombre.text.clear()
                binding.editEdad.text.clear()
                binding.editTelefono.text.clear()
            } else {
                // Manejar el caso en el que algún campo esté vacío
                // Por ejemplo, mostrar un mensaje de error al usuario
                if (nombre.isEmpty()) {
                    binding.editNombre.error = "El nombre no puede estar vacío"
                }
                if (edadText.isEmpty()) {
                    binding.editEdad.error = "La edad no puede estar vacía"
                }
                if (telefono.isEmpty()) {
                    binding.editTelefono.error = "El teléfono no puede estar vacío"
                }
            }

            //Al pulsar en el bot ón Guardar se almacena el nombre, edad y telefono en las data
            /*lifecycleScope.launch(Dispatchers.IO) {
                //Esta función se mete en un lifecycleScope, decide en qué ámbito del ciclo de vida se ejecuta cada cosa. Se lanza manualmente al hilo IO
                guardarDatos(
                    binding.editNombre.text.toString(),
                    binding.editEdad.text.toString().toInt(),
                    binding.editTelefono.text.toString()
                )
            }*/
        }

        binding.botonConsultar.setOnClickListener {
            //Se guardan los datos en la variable dataFlow
            val dataFlow: Flow<String> = dataStore.data.map {
                it[stringPreferencesKey("usuario")] ?: "Nombre no encontrado"
            }

            //Devuelve el elemento que ha encontrado previamente. También hay que meterlo en una corrutina
            lifecycleScope.launch(Dispatchers.IO) {
                consultarDatos()
                //Log.v("data_store", dataFlow.first())
            }
        }
    }

    //Función para guardar los datos
    //Las funciones de tipo suspend solo pueden ser llamadas desde una corrutina
    suspend fun guardarDatos(dataStore: String, nombre: String, edad: Int, telefono: String) {
        val usuario = Usuario(nombre, edad, telefono)
        val usuarioJson = Json.encodeToString(usuario)
        dataStore.edit {
            //par clave-valor
            it[stringPreferencesKey("usuario")] = usuarioJson

            /*
            it[stringPreferencesKey("claveNombre")] = nombre
            //clave asociada a String, stringPreferenceKey
            it[intPreferencesKey("claveEdad")] =edad
            //clave asociada a entero, intPreferenceKey
            it[stringPreferencesKey("claveTelefono")] = telefono

            Si quisiera guardar objetos en dataStore:
            Usuario (nombre, edad) -> String -> JSON -> String
             */
        }
    }

    suspend fun consultarDatos() {
        val dataFlow: kotlinx.coroutines.flow.Flow<String> = dataStore.data.map {
            it[stringPreferencesKey("usuario")] ?: ""
        }

        dataFlow.collect { usuarioJson ->
            if (usuarioJson.isNotEmpty()) {
                val usuario = Json.decodeFromString<Usuario>(usuarioJson)
                withContext(Dispatchers.Main){
                    binding.textoConsulta.text =
                        "Nombre: ${usuario.nombre}, Edad: ${usuario.edad}, Teléfono: ${usuario.telefono}"
                }
            } else {
                withContext(Dispatchers.Main){
                    binding.textoConsulta.text = "No se encontraron datos de usuario"
                }
            }
        }
    }
}

/*La función runOnUiThread es un método que pertenece a la clase Activity en Android. Permite ejecutar un bloque de código en el hilo de la interfaz de usuario (UI thread) de la actividad. En Android, todas las actualizaciones de la interfaz de usuario deben realizarse en el hilo de la interfaz de usuario para evitar problemas de concurrencia y asegurar que las actualizaciones se reflejen correctamente en la pantalla.
Cuando se llama a runOnUiThread desde un contexto que no es el hilo de la interfaz de usuario, el bloque de código que se pasa como argumento se ejecuta en el hilo de la interfaz de usuario. Esto es útil cuando se está trabajando con hilos secundarios (como el hilo de fondo que se utiliza en las corrutinas) y se necesita actualizar la interfaz de usuario con los resultados obtenidos en el hilo secundario.
En el código que te proporcioné anteriormente, runOnUiThread se utiliza para actualizar el TextView "Consultar" con los datos del usuario obtenidos en el hilo de fondo (usando corrutinas) una vez que se han recuperado del DataStore. Esto garantiza que la actualización de la interfaz de usuario se realice de forma segura en el hilo de la interfaz de usuario.
*/