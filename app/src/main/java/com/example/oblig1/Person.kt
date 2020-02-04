package com.example.oblig1

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.*


@Entity(tableName = "person")
data class Person(
    val name: String,
    val filepath: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    fun getAll(): LiveData<List<Person>>

    @Query("SELECT COUNT(*) FROM person")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: Person)

    @Delete
    suspend fun delete(user: Person)
}