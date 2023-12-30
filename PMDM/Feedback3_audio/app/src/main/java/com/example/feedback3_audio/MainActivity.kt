package com.example.feedback3_audio

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
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
    private var grabacion: MediaRecorder? = null
    private var outputFile: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val grabar: ImageButton = findViewById(R.id.botonGrabar)
        val parar: ImageButton = findViewById(R.id.botonParar)
        val reproducir: ImageButton = findViewById(R.id.botonReproducir)
        val ultimoAudio: TextView = findViewById(R.id.audioTextView)

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val nombreAudio: String = "audio_$timeStamp.3gp"

        val outputFile: String = Environment.getExternalStorageDirectory().absolutePath + File.separator + nombreAudio
        val rutaUltimoAudio: String = "audio_$timeStamp.3gp"

        grabacion = MediaRecorder()
        grabacion?.setAudioSource(MediaRecorder.AudioSource.MIC)
        grabacion?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        grabacion?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        grabacion?.setOutputFile(outputFile)

        grabar.setOnClickListener {
            try {
                grabacion?.prepare()
                grabacion?.start()
                grabar.setImageResource(R.drawable.grabando)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        parar.setOnClickListener {
            grabar.setImageResource(R.drawable.grabar)
            grabacion?.stop()
            grabacion?.release()
            grabacion = null
            ultimoAudio.setText(nombreAudio)
        }

        reproducir.setOnClickListener {
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(rutaUltimoAudio)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}