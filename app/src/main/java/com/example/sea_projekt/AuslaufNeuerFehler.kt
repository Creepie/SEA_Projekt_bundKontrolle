package com.example.sea_projekt

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_auslauf_neuer_fehler.*

class AuslaufNeuerFehler : AppCompatActivity(), View.OnClickListener {

    var selected_error_code: String? = null
    var selected_staerke: String? = null
    var selected_hauefigkeit: String? = null
    var selected_lageQuer: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auslauf_neuer_fehler)



        bT_nF_zurueck.setOnClickListener(this)
        bT_nF_speichern.setOnClickListener(this)

//        val fehlercode = resources.getStringArray(R.array.Fehlercode)
//        if (sP_nF_fehler != null){
//            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fehlercode)
//            sP_nF_fehler.adapter = adapter
//        }

        val error_code_list: ArrayList<String> = ArrayList()
        val error_code = resources.getStringArray(R.array.error_code)

        val error_code_description = resources.getStringArray(R.array.error_code_description)
        for (i in error_code.indices) {
            error_code_list.add(error_code[i].toString() + ": " + error_code_description[i])
        }

        if (sP_nF_fehler != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, error_code_list)
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

        var isSpinnerTouched = false

        sP_nF_fehler.setOnTouchListener { v, event ->
            isSpinnerTouched = true
            false
        }
        sP_nF_fehler.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //text_test.text =""
            }

            override fun onItemSelected(
                adapter: AdapterView<*>?, arg1: View,
                arg2: Int, arg3: Long
            ) {
                if (!isSpinnerTouched) return
                if (sP_nF_fehler.selectedItemPosition == 0)
                {
                    onNothingSelected(parent = null);
                }else {
                   selected_error_code = error_code[sP_nF_fehler.selectedItemPosition].toString()

                }

            }
        })
    }



   override fun onClick(v: View?) {
        when(v?.id){
            R.id.bT_nF_zurueck -> {
                Log.i("LOG", "bT_neuer_fehler_zurück was clicked")
                finish()
            }
            R.id.bT_nF_speichern -> {
                Log.i("LOG", "bT_neuer_fehler_speichern was clicked")

                val i = intent
                i.putExtra("neuerFehler", Fehler(selected_error_code, "S", "GB", "MS", "DG", 1510F, 1540F, true ))
                setResult(Activity.RESULT_OK, i)
                finish()
            }
        }
    }




}


//neuer Fehler implementiert Parcelable interface für Object übergabe von Neuer Fehler to Main
data class Fehler(val schluessel: String?, val sperrKz: String?, val lageQuer: String?, val intensitaet: String?,
             val haufeigkeit: String?, val meterPosVon: Float, val meterPosBis: Float, val toleriert: Boolean) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(schluessel)
        parcel.writeString(sperrKz)
        parcel.writeString(lageQuer)
        parcel.writeString(intensitaet)
        parcel.writeString(haufeigkeit)
        parcel.writeFloat(meterPosVon)
        parcel.writeFloat(meterPosBis)
        parcel.writeByte(if (toleriert) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Fehler> {
        override fun createFromParcel(parcel: Parcel): Fehler {
            return Fehler(parcel)
        }

        override fun newArray(size: Int): Array<Fehler?> {
            return arrayOfNulls(size)
        }
    }

}