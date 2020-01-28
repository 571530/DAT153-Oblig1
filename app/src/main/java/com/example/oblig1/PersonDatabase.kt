package com.example.oblig1

import android.app.Application

class PersonDatabase(val app: Application) {
    val oblig1app = app as Oblig1Application
    val data = oblig1app.data

    fun all(): ArrayList<Person> {
        return data
    }
    fun add(person: Person) {
        data.add(person)
    }
    operator fun get(position: Int): Person {
        return data[position]
    }
}