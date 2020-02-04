package com.example.oblig1

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log

class Oblig1Application : Application() {
    var data = ArrayList<Person>()

    override fun onCreate() {
        super.onCreate()
        //data.add(Person("cage", BitmapFactory.decodeResource(resources, R.drawable.nic_cage)))
        //data.add(Person("cat", BitmapFactory.decodeResource(resources, R.drawable.grumpy_cat)))
        //data.add(Person("dog", BitmapFactory.decodeResource(resources, R.drawable.dog)))
        //data.add(Person("turtle", BitmapFactory.decodeResource(resources, R.drawable.turtle)))

        //for (x in PersonDatabase2.getInstance(context = baseContext).personDao().getAll()) {
        //    Log.i("dev", x.name)
        //}
    }
}