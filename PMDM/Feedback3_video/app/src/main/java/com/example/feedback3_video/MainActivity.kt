package com.example.feedback3_video

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaramos la variable que hace referencia al VideoView de la pantalla
        val videoView: VideoView = findViewById(R.id.video)

        //Creamos un MediaController que proporcione controles de reproducción, pausa y avance
        val mediaController = MediaController(this)
        //Se establece la vista anclada, para que se superponga al VideoView
        mediaController.setAnchorView(videoView)

        //Creamos una URI que apunta a la ubicación del recurso de vídeo, en la carpeta raw que hemos creado
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.resurrection)
        //Establecemos la ruta y los controladores en el VideoView
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        //requestFocus prepara el componente para recibir interacciones
        videoView.requestFocus()
        //Se inicia la reproducción del vídeo
        videoView.start()
    }
}