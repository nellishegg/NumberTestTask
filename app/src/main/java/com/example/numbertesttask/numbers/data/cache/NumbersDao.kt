package com.example.numbertesttask.numbers.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NumbersDao {
    @Query("SELECT * FROM numbers_table ORDER BY date ASC")
    fun allNumbers(): List<NumberCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(number: NumberCache)

    @Query("SELECT * FROM numbers_table WHERE number = :number")
    fun number(number: String): NumberCache?
}