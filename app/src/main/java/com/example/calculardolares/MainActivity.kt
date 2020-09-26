package com.example.calculardolares

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

const val DOLAR_BLUE = 133.0
const val DOLAR_BOLSA = 120.46
const val DOLAR_NACION = 78.25
const val DOLAR_SOLIDARIO = DOLAR_NACION * 1.3

class MainActivity : AppCompatActivity() {
    private lateinit var etPesos: EditText
    private lateinit var rgDolares: RadioGroup
    private lateinit var cbAplicarImpuesto: CheckBox
    private lateinit var btnConvertir: Button
    private lateinit var txtResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
    }

    private fun setupUI() {
        etPesos = findViewById(R.id.etPesos)
        rgDolares = findViewById(R.id.rgDolares)
        cbAplicarImpuesto = findViewById(R.id.cbAplicarImpuesto)
        btnConvertir = findViewById(R.id.btnConvertir)
        txtResultado = findViewById(R.id.txtResultado)

        btnConvertir.setOnClickListener { realizarConversion() }

        //escuchamos cuando se selecciona un radio button
        rgDolares.setOnCheckedChangeListener { group, checkedId ->
            onRadioButtonCheckedChange(checkedId)
        }
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
        val pesos = etPesos.text.toString()
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
}