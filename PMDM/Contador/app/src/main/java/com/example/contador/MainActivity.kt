package com.example.contador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import com.example.contador.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener{

    //private lateinit var botonIncremento: Button;
    //private lateinit var botonDecremento: Button;
    //private var botonPasar: Button? = null;
    //private lateinit var textoContador: TextView;
    //Al declarar binding podemos cargarnos todas las demás variables declaradas

    private lateinit var binding: ActivityMainBinding
    //Esta variable guarda el contenido de la parte gráfica

    private var contador: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        //Se inicializa pantalla principal, con todos los elementos que están dentro

        setContentView(binding.root)
        //Pone como vista lo que hay dentro del fichero, en concreto lo que hay dentro de la parte principal
        //root contiene al resto

        //setContentView(R.layout.activity_main)
        Log.v("ciclo", "ejecutando onCreate")

        contador = savedInstanceState?.getInt("contador") ?:0
        //if else
        //condicion ? true : false
        //ejemplo if ternario:
        //numero%2==0 ? sout("Es par") : sout("Es impar)


        //botonIncremento = findViewById(R.id.boton_incremento)
        //botonDecremento = findViewById(R.id.boton_decremento)
        //botonPasar = findViewById(R.id.boton_Pasar)
        //textoContador = findViewById(R.id.texto_contador)
        //Con binding inicializado, lo de arriba sobra

        binding.textoContador.text = contador.toString()

        binding.botonIncremento.setOnClickListener(this)
        binding.botonDecremento.setOnClickListener(this)
        binding.botonPasar?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //Diferenciar quién ha pulsado
        when(v?.id){
            binding.botonIncremento.id->{
                contador++
            }
            binding.botonDecremento.id->{
                contador--
            }
            binding.botonPasar?.id->{
                val intent = Intent(applicationContext, SecondActivity::class.java)
                intent.putExtra("contador",contador)
                startActivity(intent)
                //intent pasa de un sitio a otro
            }
        }
        binding.textoContador.text = contador.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Par clave - valor
        outState.putInt("contador", contador)
    }

    override fun onStart() {
        super.onStart()
        Log.v("ciclo", "ejecutando onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.v("ciclo", "ejecutando onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.v("ciclo", "ejecutando onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.v("ciclo", "ejecutando onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v("ciclo", "ejecutando onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("ciclo", "ejecutando onDestroy")
    }
}