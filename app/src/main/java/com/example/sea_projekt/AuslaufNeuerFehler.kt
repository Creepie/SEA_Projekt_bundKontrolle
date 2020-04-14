package com.example.sea_projekt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_auslauf_neuer_fehler.*

class AuslaufNeuerFehler : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auslauf_neuer_fehler)

        bT_nF_zurueck.setOnClickListener(this)
        bT_nF_speichern.setOnClickListener(this)
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
