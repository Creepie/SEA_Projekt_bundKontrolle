package com.example.sea_projekt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bund_info.*
import kotlinx.android.synthetic.main.activity_main.*
import com.example.sea_projekt.Fehler as Fehler



class MainActivity : AppCompatActivity(), View.OnClickListener {

    val fehlerlist = mutableListOf<testfehler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bT_bK_neuerFehler.setOnClickListener(this)
        iV_bK_bundInfo.setOnClickListener(this)

        for(i in 0 ..5 ){
            fehlerlist.add(testfehler("nummer $i,", "$i"))
        }

        rV_bK_inspektionsdaten.layoutManager = LinearLayoutManager(this)
        rV_bK_inspektionsdaten.adapter = MyRecyclerAdapter(fehlerlist)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 999 && resultCode == Activity.RESULT_OK){
            val test5 = data?.getParcelableExtra<testfehler>("neuerFehler")
            if (test5 != null) {
                fehlerlist.add(test5)
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

    val tV_Header = view.findViewById<TextView>(R.id.tV_inspitem_fehlercode)

    init {

    }
}

//for RecyclerView Inspektionsdaten
class MyRecyclerAdapter(val list: MutableList<testfehler>) : RecyclerView.Adapter<MyViewHolder>() {

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
        holder.tV_Header.text = item.schluessel.toString()

        holder.itemView.setOnClickListener{
            Toast.makeText(
                it.context,"${item.schluessel.toString()} , ${item.sperrKz.toString()}", Toast.LENGTH_SHORT
            ).show()
        }
    }


}






