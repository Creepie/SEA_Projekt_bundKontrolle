package com.example.sea_projekt

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import com.example.sea_projekt.Fehler as Fehler



class MainActivity : AppCompatActivity(), View.OnClickListener {

    //Rinnen Liste
    lateinit var bundablageList: MutableList<Bundplatz>

    fun createBundablageList():List<Bundplatz>{
        val plaetze = resources.getStringArray(R.array.Ablageplatz)
        val size = plaetze.size
        val bundablageList = mutableListOf<Bundplatz>()
        for (i in 0 until size){
            bundablageList.add(i, Bundplatz("Rinne$i",null, mutableListOf()))
        }
        return bundablageList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var typeface: Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.monoitalic)
        bundablageList = createBundablageList() as MutableList<Bundplatz>

        bT_bK_neuerFehler.setOnClickListener(this)
        iV_bK_bundInfo.setOnClickListener(this)



        //Ablageplatz Spinner befüllen
        val ablageplatz = resources.getStringArray(R.array.Ablageplatz)
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
                rV_bK_inspektionsdaten.adapter = MyRecyclerAdapter(bundablageList[position].fehlerList)
            }

        }

        //set recycler on LinearLayout
        rV_bK_inspektionsdaten.layoutManager = LinearLayoutManager(this)
    }



    //check the Result of the activity (neuer Fehler)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 999 && resultCode == Activity.RESULT_OK){
            val fehler = data?.getParcelableExtra<Fehler>("neuerFehler")
            if (fehler != null) {
                val spinnerPos = sP_bK_ablageplatz.selectedItemPosition
                bundablageList[spinnerPos].fehlerList.add(fehler)
                rV_bK_inspektionsdaten.adapter?.notifyItemInserted(bundablageList[0].fehlerList.size);
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bT_bK_neuerFehler -> {
                Log.i("LOG", "bT_neuerFehler was clicked")
                val intent = Intent(this, AuslaufNeuerFehler::class.java)
                startActivityForResult(intent, 999)
            }
            R.id.iV_bK_bundInfo -> {
                Log.i("LOG", "iV_bundInfo was clicked")
                val intent = Intent(this, BundInfo::class.java)
                startActivity(intent)
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

        holder.itemView.setOnClickListener{
            Toast.makeText(
                it.context,"${item.schluessel.toString()} , ${item.sperrKz.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }

}


//test Klasse (ev nacher einfach ArrayList mit Platz Objekten!?)
data class Bundplatz(val platzName: String,                     //Name der Bundablage
                     val platz: Platz?,                         //Objekt der Bundablage
                     val fehlerList: MutableList<Fehler>)       //Test fehlerListe (wird danach entfernt)

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
                val bundKontrolliert: Boolean,                  //Bund kontrolliert Ja/Nein
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
                val inspektionsdatensatz: MutableList<Fehler>,  //Liste an Fehlern auf Band (derzeit auf Bundplatz zu Testzwecken)
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











