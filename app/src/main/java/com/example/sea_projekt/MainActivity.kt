package com.example.sea_projekt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bT_bK_neuerFehler.setOnClickListener(this)
        iV_bK_bundInfo.setOnClickListener(this)



    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bT_bK_neuerFehler -> {
                Log.i("LOG", "bT_neuerFehler was clicked")
                val intent = Intent(this, AuslaufNeuerFehler::class.java)
                startActivity(intent)
            }
            R.id.iV_bK_bundInfo -> {
                Log.i("LOG", "iV_bundInfo was clicked")
                val intent = Intent(this, BundInfo::class.java)
                startActivity(intent)
            }
        }
    }
}
