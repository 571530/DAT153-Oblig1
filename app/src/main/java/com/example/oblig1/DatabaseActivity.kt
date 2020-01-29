package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DividerItemDecoration


class DatabaseActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PersonAdapter
    private lateinit var data: ArrayList<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        data = PersonDatabase(application).all()

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

        adapter = PersonAdapter(data)
        recyclerView.adapter = adapter
    }


    fun startAdd(view: View) {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    private fun updateVisibility() {
        val textView = findViewById<TextView>(R.id.no_items)
        textView.visibility =
            if (data.size > 0)
                View.INVISIBLE
            else
                View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
