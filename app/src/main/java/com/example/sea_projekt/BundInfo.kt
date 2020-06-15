package com.example.sea_projekt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bund_info.*
import kotlinx.android.synthetic.main.activity_main.*

class BundInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bund_info)

        val intent = intent
        val bundnummer = intent.getStringExtra("Bundnummer")

        tV_bI_nummer.text = bundnummer

        val mutableList = mutableListOf<Fehler>()
        mutableList.add(Fehler("OZE", "S", "RR", "S", "V", 10f, 20f, false))
        mutableList.add(Fehler("VZC", "N", "GB", "L", "DG", 10f, 20f, false))
        mutableList.add(Fehler("OZE", "S", "RL", "MS", "V", 10f, 20f, false))
        mutableList.add(Fehler("VVW", "N", "GB", "L", "DG", 10f, 20f, false))

        rV_bI_inspektion.layoutManager = LinearLayoutManager(this)
        rV_bI_inspektion.adapter = MyRecyclerAdapter(mutableList)
        rV_bI_inspektion.adapter?.notifyDataSetChanged()
    }
}
