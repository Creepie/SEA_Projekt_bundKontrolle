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
import androidx.core.view.isVisible
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

        //set bundInfo to invisible
        tV_bK_bindeBand.visibility = View.INVISIBLE
        tV_bK_signo.visibility = View.INVISIBLE
        tV_bK_kantenschutz.visibility = View.INVISIBLE
        tV_bK_nAst.visibility = View.INVISIBLE
        cB_bK_Signo.visibility = View.INVISIBLE
        cB_bK_matte.visibility = View.INVISIBLE


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
                if (BundpaltzSingleton.bundablageList[position].bund != null){
                    tV_bK_bindeBand.visibility = View.VISIBLE
                    tV_bK_signo.visibility = View.VISIBLE
                    tV_bK_kantenschutz.visibility = View.VISIBLE
                    tV_bK_nAst.visibility = View.VISIBLE
                    cB_bK_Signo.visibility = View.VISIBLE
                    cB_bK_matte.visibility = View.VISIBLE
                } else
                {
                    tV_bK_bindeBand.visibility = View.INVISIBLE
                    tV_bK_signo.visibility = View.INVISIBLE
                    tV_bK_kantenschutz.visibility = View.INVISIBLE
                    tV_bK_nAst.visibility = View.INVISIBLE
                    cB_bK_Signo.visibility = View.INVISIBLE
                    cB_bK_matte.visibility = View.INVISIBLE
                }
                if (BundpaltzSingleton.bundablageList[position].bund != null && BundpaltzSingleton.bundablageList[position].bund.bundKontrolliert){
                        bT_bK_absenden.setBackgroundResource(R.drawable.correct)
                        cB_bK_matte.isChecked=true
                        cB_bK_Signo.isChecked=true
                } else{
                    bT_bK_absenden.setBackgroundResource(R.drawable.plane)
                    cB_bK_Signo.isChecked=false
                    cB_bK_matte.isChecked=false
                }

                val emptyMutableList = mutableListOf<Fehler>()
                BundpaltzSingleton.spinnerPos = position
                if (BundpaltzSingleton.bundablageList[position].bund != null){
                    if (BundpaltzSingleton.bundablageList[position].bund.baender[0].inspektionsdatensatz == null){
                        BundpaltzSingleton.bundablageList[position].bund.baender[0].inspektionsdatensatz = arrayListOf()
                    }
                    rV_bK_inspektionsdaten.adapter = MyRecyclerAdapter(BundpaltzSingleton.bundablageList[position].bund.baender[0].inspektionsdatensatz)
                } else {
                    rV_bK_inspektionsdaten.adapter = MyRecyclerAdapter(emptyMutableList)
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
                if (BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund != null && !BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund.bundKontrolliert){
                    var bundnummer = BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund.menr
                    Log.i("LOG", "bT_neuerFehler was clicked on a item with bund object != null")
                    val intent = Intent(this, AuslaufNeuerFehler::class.java)
                    intent.putExtra("Bundnummer", bundnummer)
                    startActivityForResult(intent, 999)
                } else {
                    Log.i("LOG", "bT_neuerFehler was clicked on a item with bund object == null or bund already checked")
                }
            }
            R.id.iV_bK_bundInfo -> {
                if (BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund != null){
                    var bundnummer = BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund.menr
                    Log.i("LOG", "iV_bundInfo was clicked with bund object != null")
                    val intent = Intent(this, BundInfo::class.java)
                    intent.putExtra("Bundnummer", bundnummer)
                    startActivity(intent)
                } else {
                    Log.i("LOG", "iV_bundInfo was clicked with bund object == null")
                }
            }
            R.id.bT_bK_absenden -> {
                if (BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund != null){
                    if (cB_bK_matte.isChecked && cB_bK_Signo.isChecked){
                        Log.i("LOG", "iV_bundInfo was clicked with bund object != null")
                        BundpaltzSingleton.bundablageList[BundpaltzSingleton.spinnerPos].bund.bundKontrolliert = true
                        bT_bK_absenden.setBackgroundResource(R.drawable.correct)
                    } else {
                        val myToast = Toast.makeText(applicationContext,"Bitte die Checkboxen kontrollieren",Toast.LENGTH_SHORT)
                        myToast.show()
                    }
                } else {
                    Log.i("LOG", "iV_bundInfo was clicked with bund object == null")
                }
            }
        }
    }
}











