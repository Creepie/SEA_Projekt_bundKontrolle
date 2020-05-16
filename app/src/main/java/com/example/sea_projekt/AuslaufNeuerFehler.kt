package com.example.sea_projekt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_auslauf_neuer_fehler.*

class AuslaufNeuerFehler : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auslauf_neuer_fehler)

        bT_nF_zurueck.setOnClickListener(this)
        bT_nF_speichern.setOnClickListener(this)

        val fehlercode = resources.getStringArray(R.array.Fehlercode)
        if (sP_nF_fehler != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fehlercode)
            sP_nF_fehler.adapter = adapter
        }

        val staerke = resources.getStringArray(R.array.Intensität)
        if (sP_nF_intensitaet != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, staerke)
            sP_nF_intensitaet.adapter = adapter
        }

        val hauefigkeit = resources.getStringArray(R.array.Häufigkeit)
        if (sP_nF_haeufigkeit != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hauefigkeit)
            sP_nF_haeufigkeit.adapter = adapter
        }

        val lageQuer = resources.getStringArray(R.array.Lage_quer)
        if (sP_nF_lageQuer != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lageQuer)
            sP_nF_lageQuer.adapter = adapter
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bT_nF_zurueck -> {
                Log.i("LOG", "bT_neuer_fehler_zurück was clicked")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.bT_nF_speichern -> {
                Log.i("LOG", "bT_neuer_fehler_speichern was clicked")

                //check filter > if all fields are filled
                //var filter = true

                //if filter is ok > create new object
                //if (filter){
                //    var test = Fehler("S", "VZC", "M", "L", "DG", 100F, 200F, false)
                    //add in Array list


                //} else {
                    //create toast > "bitte eingaben kontrollieren"
               // }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}

class Fehler(val schluessel: String, val sperrKz: String, val lageQuer: String, val intensitaet: String,
             val haufeigkeit: String, val meterPosVon: Float, val meterPosBis: Float, val toleriert: Boolean)

data class testfehler(val schluessel: String, val sperrKz: String)