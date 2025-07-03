package com.example.cropswap

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TenderApplicationDao {
    @Insert
    suspend fun insert(application: TenderApplication)

    @Query("SELECT * FROM tender_applications")
    suspend fun getAll(): List<TenderApplication>
} 