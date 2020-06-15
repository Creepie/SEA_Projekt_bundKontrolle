package com.example.sea_projekt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get Font
        var typeface: Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.monoitalic)

        //set on clickListeners
        bT_bK_neuerFehler.setOnClickListener(this)
        iV_bK_bundInfo.setOnClickListener(this)
        bT_bK_absenden.setOnClickListener(this)

        //set recycler on LinearLayout
        rV_bK_inspektionsdaten.layoutManager = LinearLayoutManager(this)

        //Load json and map it to object list
        val jsonFileString = getJsonDataFromAsset(applicationContext, "auslauf_plaetze.json")
        Log.i("data", jsonFileString)

        //get list from json and add it to BundplatzSingelton
        jsonToObject(jsonFileString)

        //get ArrayList for Ablageplatz Spinner
        var ablageplatz  = ArrayList<String>()
        var idAblageplatz = resources.getStringArray(R.array.Ablageplatz)
        for (i in idAblageplatz.indices) {
            if (BundpaltzSingleton.bundablageList[i].bund != null){
                ablageplatz.add("${String.format("%-4s",idAblageplatz[i])} : ${BundpaltzSingleton.bundablageList[i].bund.baender[0].menr}")
            } else {
                ablageplatz.add("${String.format("%-4s",idAblageplatz[i])}")
            }
        }

        if (sP_bK_ablageplatz != null){
            val adapter:ArrayAdapter<String> = object: ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                ablageplatz
            ){
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup
                ): View {
                    val view:TextView = super.getDropDownView(position, convertView, parent) as TextView
                    // set item font and text style
                    view.setTypeface(typeface, Typeface.NORMAL)
                    return view
                }
            }
            sP_bK_ablageplatz.adapter = adapter
        }



        //Ablageplatz Spinner änderungen checken
        sP_bK_ablageplatz.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("LOG", "sP_bK_ablageplatz was clicked and nothing changed")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.i("LOG", "sP_bK_ablageplatz was clicked and changed $position")
                //set color of send button
                var colorValue = 0
                if (BundpaltzSingleton.bundablageList[position].bund != null && BundpaltzSingleton.bundablageList[position].bund.bundKontrolliert){
                        bT_bK_absenden.setBackgroundResource(R.drawable.correct)
                } else{
                    bT_bK_absenden.setBackgroundResource(R.drawable.plane)
                }


                BundpaltzSingleton.spinnerPos = position
                if (BundpaltzSingleton.bundablageList[position].bund != null){
                    if (BundpaltzSingleton.bundablageList[position].bund.baender[0].inspektionsdatensatz == null){
                        BundpaltzSingleton.bundablageList[position].bund.baender[0].inspektionsdatensatz = arrayListOf()
                    }
                    rV_bK_inspektionsdaten.adapter = MyRecyclerAdapter(BundpaltzSingleton.bundablageList[position].bund.baender[0].inspektionsdatensatz)
                } else {
                    val mutableList = mutableListOf<Fehler>()
                    rV_bK_inspektionsdaten.adapter = MyRecyclerAdapter(mutableList)
                }
            }
        }
    }

    //Json
    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun jsonToObject(jsonFileString: String?) {
        val gson = Gson()
        val listAblageType = object : TypeToken<List<Platz>>() {}.type

        var bundablagen: ArrayList<Platz> = gson.fromJson(jsonFileString, listAblageType)
        bundablagen.forEachIndexed { idx, person -> Log.i("data", "> Item $idx:\n$person") }
        //add list to Singleton
        BundpaltzSingleton.bundablageList = bundablagen
    }

    //check the Result of the activity (neuer Fehler)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 999 && resultCode == Activity.RESULT_OK){
            val fehler = data?.getParcelableExtra<Fehler>("neuerFehler")
            if (fehler != null) {
                val spinnerPos = sP_bK_ablageplatz.selectedItemPosition
                //create new arrayList and add new fehler
                if (BundpaltzSingleton.bundablageList[spinnerPos].bund != null){
                    if (BundpaltzSingleton.bundablageList[spinnerPos].bund.baender[0].inspektionsdatensatz == null){
                        BundpaltzSingleton.bundablageList[spinnerPos].bund.baender[0].inspektionsdatensatz = arrayListOf()
                    }
                    BundpaltzSingleton.bundablageList[spinnerPos].bund.baender[0].inspektionsdatensatz.add(fehler)
                    rV_bK_inspektionsdaten.adapter?.notifyItemInserted(BundpaltzSingleton.bundablageList[spinnerPos].bund.baender[0].inspektionsdatensatz.size)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bT_bK_neuerFehler -> {
                if (BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund != null){
                    var bundnummer = BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund.menr
                    Log.i("LOG", "bT_neuerFehler was clicked on a item with bund object != null")
                    val intent = Intent(this, AuslaufNeuerFehler::class.java)
                    intent.putExtra("Bundnummer", bundnummer)
                    startActivityForResult(intent, 999)
                } else {
                    Log.i("LOG", "bT_neuerFehler was clicked on a item with bund object == null")
                }
            }
            R.id.iV_bK_bundInfo -> {
                if (BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund != null){
                    Log.i("LOG", "iV_bundInfo was clicked with bund object != null")
                    val intent = Intent(this, BundInfo::class.java)
                    startActivity(intent)
                } else {
                    Log.i("LOG", "iV_bundInfo was clicked with bund object == null")
                }
            }
            R.id.bT_bK_absenden -> {
                if (BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund != null){
                    Log.i("LOG", "iV_bundInfo was clicked with bund object != null")
                    BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund.bundKontrolliert = true
                    bT_bK_absenden.setBackgroundResource(R.drawable.correct)
                } else {
                    Log.i("LOG", "iV_bundInfo was clicked with bund object == null")
                }
            }
        }
    }
}


//for RecyclerView Inspektionsdaten
class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val tV_sperrKz = view.findViewById<TextView>(R.id.tV_inspitem_SperrKz)
    val tV_schluessel = view.findViewById<TextView>(R.id.tV_inspitem_schluessel)
    val tV_identiziaet = view.findViewById<TextView>(R.id.tV_idensitaet)
    val tV_lageQuer = view.findViewById<TextView>(R.id.tV_lageQuer)
    val tV_hauefigkeit = view.findViewById<TextView>(R.id.tV_inspitem_hauefigkeit)

    init {

    }
}

//for RecyclerView Inspektionsdaten
class MyRecyclerAdapter(val list: MutableList<Fehler>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_inspektions_item,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = list[position]
        holder.tV_sperrKz.text = item.sperrKz.toString()
        holder.tV_schluessel.text = item.schluessel.toString()
        holder.tV_identiziaet.text = item.intensitaet.toString()
        holder.tV_lageQuer.text = item.lageQuer.toString()
        holder.tV_hauefigkeit.text = item.haufeigkeit.toString()

        holder.itemView.setOnClickListener{
            Toast.makeText(
                it.context,"${item.schluessel.toString()} , ${item.sperrKz.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }
}



//Platz Object > Daten pro Ablageplatz
data class Platz(val id: Int,                                   //
                 val rinne: Int,                                //
                 val barcode: String,                           //Barcode der Bundablage falls vorhanden
                 val bezeichnung: String,                       //Bezeichnung der Bundablage
                 val bund: Bund)                                //Objekt des Bundes auf Ablage

//Bund Object > Daten pro Bund > Teil von Platz Objekt
data class Bund(val bundId: Int,                                //Bund ID
                val menr: String,                               //
                val untr: String,                               //
                var bundKontrolliert: Boolean,                  //Bund kontrolliert Ja/Nein
                val bundVerbucht: Boolean,                      //Bund verbucht Ja/Nein
                val bundGesperrt: Boolean,                      //Bund gesperrt Ja/Nein
                val folgeAst: String,                           //folge Arbeitsstufe (für Bundinfos)
                val baender: MutableList<Band>,                 //Liste an Bändern (Teilung eines Bundes möglich)
                val bundParameter: MutableList<Parameter>)      //Liste an Parameter (Teilung eines Bundes möglich)

//Band Object > Daten pro Band > Teilung pro Bund möglich > Teil von Bund Objekt
data class Band(val bandId: Int,                                //Band ID
                val bandauslID: Int,                            //
                val menr: String,                               //
                val untr: String,                               //
                var inspektionsdatensatz: MutableList<Fehler>,  //Liste an Fehlern auf Band (derzeit auf Bundplatz zu Testzwecken)
                val evParameter: MutableList<Parameter>,        //Liste an Parametern von dominierten Auftrag
                val istParameter: MutableList<Parameter>)       //Liste an Parametern

//Parameter Objekt > Teilung pro Bund möglich daher verschiedene Parameter möglich > Teil von Bund Objekt
data class Parameter(val kurztext: String,                      //
                     val langtext: String,                      //
                     val sollIstKennzeichen: String,            //
                     val ermittlungsKennzeichen: String,        //
                     val beareitbar: Boolean,                   //
                     val wert: String,                          //
                     val typ: String,                           //
                     val reihenfolge: Int)                      //











