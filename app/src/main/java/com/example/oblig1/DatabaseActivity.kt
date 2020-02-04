package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DividerItemDecoration


class DatabaseActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: PersonViewModel
    private lateinit var adapter: PersonAdapter
    private var size: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        viewModel = ViewModelProvider(this).get(PersonViewModel::class.java)
        viewModel.allPersons.observe(this, Observer { persons ->
            persons?.let {
                adapter.setPersons(persons)
                size = persons.size
            }
        })

        adapter = PersonAdapter(this, viewModel)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            // ☹☹☹
            updateVisibility()
        }
        recyclerView.adapter = adapter

    }

    fun startAdd(view: View) {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    private fun updateVisibility() {
        val textView = findViewById<TextView>(R.id.no_items)
        textView.visibility =
            if (size > 0)
                View.INVISIBLE
            else
                View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
