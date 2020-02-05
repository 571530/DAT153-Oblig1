package com.example.oblig1

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PersonRepository(private val personDao: PersonDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allPersons: LiveData<List<Person>> = personDao.getAll()

    suspend fun insert(person: Person) {
        personDao.insert(person)
    }

    suspend fun delete(person: Person) {
        personDao.delete(person)
    }
}

class PersonViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: PersonRepository
    // LiveData gives us updated words when they change.
    val allPersons: LiveData<List<Person>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val personDao = PersonDatabase.getDatabase(application, viewModelScope).personDao()
        repository = PersonRepository(personDao)
        allPersons = repository.allPersons
    }

    fun insert(person: Person) = viewModelScope.launch {
        repository.insert(person)
    }

    fun delete(person: Person) = viewModelScope.launch {
        repository.delete(person)
    }

}

@Database(entities = arrayOf(Person::class), version = 1)
abstract class PersonDatabase: RoomDatabase() {
    abstract fun personDao(): PersonDao


    private class PersonDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.personDao())
                }
            }
        }

        suspend fun populateDatabase(personDao: PersonDao) {
            if (personDao.getCount() == 0) {
                /*val person1 = Person(name="cage", filepath=Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.nic_cage).toString())
                personDao.insert(person1)
                val person2 = Person(name="cat", filepath=Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.grumpy_cat).toString())
                personDao.insert(person2)
                val person3 = Person(name="turtle", filepath=Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.turtle).toString())
                personDao.insert(person3)
                val person4 = Person(name="dog", filepath=Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.dog).toString())
                personDao.insert(person4)*/
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PersonDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope): PersonDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PersonDatabase::class.java,
                    "person_database"
                ).addCallback(PersonDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
