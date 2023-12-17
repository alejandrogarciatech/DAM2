package com.example.listasjson

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.listasjson.adapter.UsuariosAdapter
import com.example.listasjson.databinding.ActivityMainBinding
import com.example.listasjson.model.User
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import org.json.JSONObject
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {
    //URL
    //HTTP Connection
    //BufferedReader
    //String -> JSON
    //Consultas

    private lateinit var binding: ActivityMainBinding
    private lateinit var listaUsuarios: ArrayList<User>
    private lateinit var adaptadorUsuariosAdapter: UsuariosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaUsuarios = ArrayList()
        adaptadorUsuariosAdapter = UsuariosAdapter(listaUsuarios, this)

        binding.recyclerUsuarios.adapter = adaptadorUsuariosAdapter
        binding.recyclerUsuarios.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.spinnerGenero.onItemSelectedListener = object : OnItemSelectedListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Crear la petición
                val genero = binding.spinnerGenero.selectedItem.toString()
                val url = "https://randomuser.me/api/?results=50&gender=${genero}"
                val peticion: JsonObjectRequest = JsonObjectRequest(url,{
                    getContacts(it)
                },{
                    Log.v("conexión", "conexión incorrecta")
                })

                //Otra forma
                //val peticion2 = JsonObjectRequest(Method.PUBLIC, url, null, {},{})

                //Se añade a la lista de peticiones
                Volley.newRequestQueue(applicationContext).add(peticion)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    fun getContacts(response: JSONObject) {
        listaUsuarios.clear()
        val results = response.getJSONArray("results")
        for (i in (0..results.length()-1)){
            val user = results.getJSONObject(i)
            val name = user.getJSONObject("name").getString("first")
            val last = user.getJSONObject("name").getString("last")
            val email = user.getString("email")
            val phone = user.getString("phone")
            val picture = user.getJSONObject("picture").getString("medium")
            listaUsuarios.add(User(name, last, email, phone, picture))
            adaptadorUsuariosAdapter.notifyDataSetChanged()
        }
        Log.v("conexión", listaUsuarios.size.toString())
    }
}