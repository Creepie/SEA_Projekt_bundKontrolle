package com.example.sea_projekt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import com.example.sea_projekt.Fehler as Fehler



class MainActivity : AppCompatActivity(), View.OnClickListener {

    //Rinnen Liste
    val rinnenList = mutableListOf<Rinne>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bT_bK_neuerFehler.setOnClickListener(this)
        iV_bK_bundInfo.setOnClickListener(this)

        //Ablageplatz Spinner befüllen
        val ablageplatz = resources.getStringArray(R.array.Ablageplatz)
        if (sP_bK_ablageplatz != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ablageplatz)
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
                rV_bK_inspektionsdaten.adapter = MyRecyclerAdapter(rinnenList[position].fehlerList)
            }

        }

        //leere Rinnen in RinnenListe einfügen
        for (i in 0..5){
            rinnenList.add(i, Rinne("Rinne$i",null, mutableListOf()))
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
                rinnenList[spinnerPos].fehlerList.add(fehler)
                rV_bK_inspektionsdaten.adapter?.notifyItemInserted(rinnenList[0].fehlerList.size);
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
    val tV_meterPosVon = view.findViewById<TextView>(R.id.tV_inspitem_meterPosVon)
    val tV_meterPosBis = view.findViewById<TextView>(R.id.tV_inspitem_meterPosBis)

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
        holder.tV_meterPosVon.text = item.meterPosVon.toString()
        holder.tV_meterPosBis.text = item.meterPosBis.toString()

        holder.itemView.setOnClickListener{
            Toast.makeText(
                it.context,"${item.schluessel.toString()} , ${item.sperrKz.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }

}

data class Rinne(val platzName: String, val platz: Platz?, val fehlerList: MutableList<Fehler>)

data class Platz(val id: Int, val rinne: Int, val barcode: String, val bezeichnung: String, val bund: Bund)

data class Bund(val bundId: Int, val menr: String, val untr: String, val bundKontrolliert: Boolean, val bundVerbucht: Boolean, val bundGesperrt: Boolean, val folgeAst: String, val baender: MutableList<Band>)

data class Band(val bandId: Int, val bandauslID: Int, val menr: String, val untr: String, val inspektionsdatensatz: MutableList<Fehler> )











