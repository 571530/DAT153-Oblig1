package com.example.oblig1

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PersonAdapter(private val context: Activity, private val viewModel: PersonViewModel): RecyclerView.Adapter<PersonAdapter.ViewHolder>() {
    private var persons = emptyList<Person>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.textView)!!
        val imageView = view.findViewById<ImageView>(R.id.imageView)!!
        val button = view.findViewById<Button>(R.id.remove_button)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.person_list_item, parent, false))
    }

    internal fun setPersons(persons: List<Person>) {

        if (this.persons.isEmpty()) {
            this.persons = persons
            notifyDataSetChanged()
        }
        else {
            class ExtendedPerson(val old: Boolean, val index: Int, val person: Person)

            val all = this.persons.mapIndexed { i, it -> ExtendedPerson(true, i, it) } + persons.mapIndexed { i, it -> ExtendedPerson(false, i, it) }

            val grouped = all.groupBy { it.person.id }

            val addedOrDeleted = grouped.filter { it.value.size == 1 }.map { it.value[0] }

            for (person_ex in addedOrDeleted) {
                if (person_ex.old) {
                    notifyItemRemoved(person_ex.index)
                }
                else {
                    notifyItemInserted(person_ex.index)
                }
            }
            this.persons = persons
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = persons[position]?.name

        Glide
            .with(context)
            .load(persons[position]?.filepath)
            .circleCrop()
            .into(holder.imageView)

        holder.button.setOnClickListener {
            val pos = holder.adapterPosition
            val person = persons[pos]
            if (person != null) {
                viewModel.delete(person)
            }
        }
    }

    override fun getItemCount() = persons.size
}