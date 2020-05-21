package com.example.sea_projekt

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_auslauf_neuer_fehler.*


class AuslaufNeuerFehler : AppCompatActivity(), View.OnClickListener {

    lateinit var error_code_list: ArrayList<String>
    lateinit var intensitaet_list: ArrayList<String>
    lateinit var haeufigkeit_list: ArrayList<String>
    lateinit var lageQuer_list: ArrayList<String>
    lateinit var error_code: Array<String>
    lateinit var staerke: Array<String>
    lateinit var haeufigkeit: Array<String>
    lateinit var lageQuer: Array<String>
    var Sperrkennzeichen: String = "N"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auslauf_neuer_fehler)

        bT_nF_zurueck.setOnClickListener(this)
        bT_nF_speichern.setOnClickListener(this)
        sW_nF_sperre.setOnClickListener(this)

        var typeface: Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.monoitalic)

        //errorCode Spinner
        error_code_list  = ArrayList()
        error_code = resources.getStringArray(R.array.error_code)
        val error_code_description = resources.getStringArray(R.array.error_code_description)

        for (i in error_code.indices) {
            error_code_list.add("${String.format("%-4s",error_code[i])} : ${error_code_description[i]}")
        }

        if (sP_nF_fehler != null) {
            //Adapter for errorCodes
            sP_nF_fehler.adapter = getSpinnerAdapter(error_code_list,typeface)
        }

        //intensitaet  Spinner
        intensitaet_list  = ArrayList()
        staerke = resources.getStringArray(R.array.Intensität)
        val intensitaet_description = resources.getStringArray(R.array.Intensität_description)

        for (i in staerke.indices) {
            intensitaet_list.add("${String.format("%-3s",staerke[i])}: ${intensitaet_description[i]}")
        }
        if (sP_nF_intensitaet != null){
            //Adapter for Intensitaet
            sP_nF_intensitaet.adapter = getSpinnerAdapter(intensitaet_list,typeface)
        }

        //hauefigkeit Spinner
        haeufigkeit_list = ArrayList()
        haeufigkeit = resources.getStringArray(R.array.Häufigkeit)
        val haeufigkeit_description = resources.getStringArray(R.array.Häufigkeit_description)

        for (i in haeufigkeit.indices) {
            haeufigkeit_list.add("${String.format("%-4s",haeufigkeit[i])}: ${haeufigkeit_description[i]}")
        }
        if (sP_nF_haeufigkeit != null){
            sP_nF_haeufigkeit.adapter = getSpinnerAdapter(haeufigkeit_list,typeface)
        }

        //lageQuer Spinner
        lageQuer_list = ArrayList()
        lageQuer = resources.getStringArray(R.array.Lage_quer)
        val lageQuer_description = resources.getStringArray(R.array.Lage_quer_description)

        for (i in lageQuer.indices) {
            lageQuer_list.add("${String.format("%-4s",lageQuer[i])} : ${lageQuer_description[i]}")
        }
        if (sP_nF_lageQuer != null){
            sP_nF_lageQuer.adapter = getSpinnerAdapter(lageQuer_list,typeface)
        }




    }


    //function to get Spinner Adapter for each Spinner with new mono font
    fun getSpinnerAdapter(stringList: ArrayList<String>, typeface: Typeface?): ArrayAdapter<String> {
        val adapter:ArrayAdapter<String> = object: ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            stringList
        ){
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup
            ): View {
                val view:TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item font and text style
                view.setTypeface(typeface, Typeface.NORMAL)
                return view
            }
        }
        return adapter
    }

    //Toast Message
    fun getToast() {
        val toast = Toast.makeText(applicationContext, "Bitte alle Felder korrekt ausfüllen", Toast.LENGTH_LONG)
        toast.show()
    }

    //check if every spinner is != default position and edit Text is not empty
    fun allFilled(): Boolean {
        return !(sP_nF_fehler.selectedItemPosition == 0 ||
                sP_nF_lageQuer.selectedItemPosition == 0 ||
                sP_nF_intensitaet.selectedItemPosition == 0 ||
                sP_nF_haeufigkeit.selectedItemPosition == 0 ||
                eT_nF_wert.text.length==0

                )
    }


    //onCLicks listen for Buttons
   override fun onClick(v: View?) {
        when(v?.id){
            R.id.bT_nF_zurueck -> {
                Log.i("LOG", "bT_neuer_fehler_zurück was clicked")
                finish()
            }
            R.id.bT_nF_speichern -> {
                Log.i("LOG", "bT_neuer_fehler_speichern was clicked")

                if (!allFilled()) {
                    getToast()
               } else {
                    val i = intent
                    i.putExtra(
                        "neuerFehler", Fehler(
                            error_code[sP_nF_fehler.selectedItemPosition],
                            Sperrkennzeichen,
                            lageQuer[sP_nF_lageQuer.selectedItemPosition],
                            staerke[sP_nF_intensitaet.selectedItemPosition],
                            haeufigkeit[sP_nF_haeufigkeit.selectedItemPosition],
                            1510F,
                            1540F,
                            true
                        )
                    )
                    setResult(Activity.RESULT_OK, i)
                    finish()
                }
            }
            //Sperre Switch
            R.id.sW_nF_sperre -> {
                when (sW_nF_sperre.isChecked) {
                    true -> {
                        Sperrkennzeichen = "S"
                        sW_nF_sperre.text = "Ja"
                    }

                    false -> {
                        Sperrkennzeichen = "N"
                        sW_nF_sperre.text = "Nein"
                    }
                }
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
    )


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

