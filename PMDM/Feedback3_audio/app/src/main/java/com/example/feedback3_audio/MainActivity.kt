package com.example.feedback3_audio

import android.icu.text.SimpleDateFormat
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    //Declaramos la variable grabación como nula porque aún no existe
    private var grabacion: MediaRecorder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaramos las variables correspondientes a los botones y textview
        val grabar: ImageButton = findViewById(R.id.botonGrabar)
        val parar: ImageButton = findViewById(R.id.botonParar)
        val ultimoAudio: TextView = findViewById(R.id.audioTextView)

        //Declaramos la variable timeStamp para darle un nombre a cada audio grabado, con la fecha y la hora en la que han sido creados
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        //Así evitamos que el mismo archivo se sobreescriba una y otra vez
        val nombreAudio: String = "audio_$timeStamp.3gp"

        //Construimos la ruta completa del archivo de audio concatenando el directorio de almacenamiento externo, un separador ( / ) y el nombre del archivo
        val outputFile: String = Environment.getExternalStorageDirectory().absolutePath + File.separator + nombreAudio

        //Creamos una instancia de MediaRecorder,que se utiliza para grabar audio y vídeo
        grabacion = MediaRecorder()

        //Establecemos la fuente de audio, el formato de salida, el codificador y la ruta  en la que se guardará el archivo
        grabacion?.setAudioSource(MediaRecorder.AudioSource.MIC)
        grabacion?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        grabacion?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        grabacion?.setOutputFile(outputFile)

        //Establecemos un listener para detectar la pulsación del botón grabar
        grabar.setOnClickListener {
            try {//Se prepara el dispositivo y se inicia la grabación
                grabacion?.prepare()
                grabacion?.start()
                //Cambiamos la imagen del botón para indicar al usuario que la grabación está en curso
                grabar.setImageResource(R.drawable.grabando)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }//Manejamos las excepciones para ver los posibles fallos por consola
        }

        //Establecemos un listener para detectar la pulsación del botón parar
        parar.setOnClickListener {
            //Dejamos la imagen original del botón grabar para indicar que ya no se está grabando
            grabar.setImageResource(R.drawable.grabar)
            //Se para la grabación, se liberan recursos y se elimina la referencia al objeto MediaRecorder
            grabacion?.stop()
            grabacion?.release()
            grabacion = null
            //Cuando la grabación ha finalizado, mostramos al usuario el nombre del audio recién creado
            ultimoAudio.setText(nombreAudio)
        }
    }
}