package com.example.calculardolares

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.calculardolares.providers.PesosProvider

const val DOLAR_BLUE = 133.0
const val DOLAR_BOLSA = 120.46
const val DOLAR_NACION = 78.25
const val DOLAR_SOLIDARIO = DOLAR_NACION * 1.3

const val SHARED_PREF_PESOS = "PESOS"
const val POSICION = "POSICION"

class MainActivity : AppCompatActivity() {
    private lateinit var spPesos: Spinner
    private lateinit var rgDolares: RadioGroup
    private lateinit var cbAplicarImpuesto: CheckBox
    private lateinit var btnConvertir: Button
    private lateinit var txtResultado: TextView
    private lateinit var  toolbar: Toolbar

    private val  sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(SHARED_PREF_PESOS, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //almacenamiento interno
        setupUI()
    }

    private fun setupUI() {
        setupToolbar()
        spPesos = findViewById(R.id.spPesos)
        rgDolares = findViewById(R.id.rgDolares)
        cbAplicarImpuesto = findViewById(R.id.cbAplicarImpuesto)
        btnConvertir = findViewById(R.id.btnConvertir)
        txtResultado = findViewById(R.id.txtResultado)

        btnConvertir.setOnClickListener { realizarConversion() }

        //escuchamos cuando se selecciona un radio button
        rgDolares.setOnCheckedChangeListener { group, checkedId ->
            onRadioButtonCheckedChange(checkedId)
        }
        setupSpinner()
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Conversión dolares"
    }

    private fun setupSpinner() {

        //OBTNER ULTIMA POSICIÓN POR MEMORIA
        val ultimaPosicion = sharedPreferences.getInt(POSICION, 0)

        val pesos = PesosProvider.providePesos()
        //el adapter es para ir iterando cada vista
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            pesos
        )


        spPesos.adapter = adapter
        spPesos.setSelection(ultimaPosicion)
    }

    private fun onRadioButtonCheckedChange(checkedId: Int) {
        if (checkedId == R.id.rbNacion){
            mostrarCheckboxImpuesto()
        } else  {
            ocultarCheckBoxImpuesto()
        }
    }

    private fun ocultarCheckBoxImpuesto() {
        cbAplicarImpuesto.visibility = View.GONE
    }

    private fun mostrarCheckboxImpuesto() {
        cbAplicarImpuesto.visibility = View.VISIBLE
    }

    private fun realizarConversion() {
        val pesos = spPesos.selectedItem.toString()
        //validamos que no este vacío
        if(pesos.isNotEmpty()){

           val resultado = when (obtenerRadioButtonSeleccionado()) {
               R.id.rbBlue -> convertirABlue(pesos.toDouble())
               R.id.rbBlue -> convertirABolsa(pesos.toDouble())
               R.id.rbBlue -> convertirANacion(pesos.toDouble())
               else -> convertirABlue(pesos.toDouble())
           }
            txtResultado.text = resultado.toString()
        } else{
            MostrarMensaje("Completar todos los campos")
        }
    }


    private fun convertirANacion(pesos: Double): Double {
        return if (cbAplicarImpuesto.isChecked){
            pesos / DOLAR_SOLIDARIO
        } else {
            pesos/ DOLAR_NACION
        }

    }

    //PRACTICAMOS HACER FUNCIÓN EN UNA SOLA LÍNEA
    private fun convertirABolsa(pesos: Double) = pesos / DOLAR_BOLSA

    private fun convertirABlue(pesos: Double) = pesos / DOLAR_BLUE

    private fun obtenerRadioButtonSeleccionado(): Int {
        return  rgDolares.checkedRadioButtonId
    }

    private fun MostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        actualizarValorPosicionPreferencias()
        super.onStop()
    }

    private fun actualizarValorPosicionPreferencias() {

        //Nos permite no tener que repetir la variable.
        sharedPreferences.edit().apply(){
        putInt(POSICION, spPesos.selectedItemPosition)
        apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemAyuda -> MostrarMensaje("Ayuda")
            R.id.itemInfo -> MostrarMensaje("Información")
        }
        return super.onOptionsItemSelected(item)
    }
}