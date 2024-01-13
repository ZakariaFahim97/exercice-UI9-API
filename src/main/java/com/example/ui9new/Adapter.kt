package com.example.ui9new

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adapter(private val context: Context, private val dataList: List<Pc>):
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nom1)
        val prix: TextView = itemView.findViewById(R.id.prix1)
        val imag: ImageView = itemView.findViewById(R.id.imageV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pc = dataList[position]

        holder.name.text = "Nom: ${pc.name}"
        holder.prix.text = "Prix: ${pc.prix}"

        Picasso.get()
            .load(pc.image)
            .into(holder.imag)

        /*holder.itm.setOnClickListener{
            val id = pc.id
            Toast.makeText(context, "id is ${id}", Toast.LENGTH_SHORT).show()

        }*/
    }

}