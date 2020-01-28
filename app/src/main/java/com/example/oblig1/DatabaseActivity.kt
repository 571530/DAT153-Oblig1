package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DividerItemDecoration





class DatabaseActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        val data = PersonDatabase(application).all()

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            (recyclerView.layoutManager as LinearLayoutManager).orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = PersonAdapter(data)
        recyclerView.adapter = adapter
    }


    fun startAdd(view: View) {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
