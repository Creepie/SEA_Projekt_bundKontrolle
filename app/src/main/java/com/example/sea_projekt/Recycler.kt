package com.example.sea_projekt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

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