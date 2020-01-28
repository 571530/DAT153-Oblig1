package com.example.oblig1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter(private val data: ArrayList<Person>): RecyclerView.Adapter<PersonAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.textView)!!
        val imageView = view.findViewById<ImageView>(R.id.imageView)!!
        val button = view.findViewById<Button>(R.id.remove_button)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.person_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = data[position].name
        holder.imageView.setImageBitmap(data[position].image)
        holder.button.setOnClickListener {
            val pos = holder.adapterPosition
            data.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }

    override fun getItemCount() = data.size
}