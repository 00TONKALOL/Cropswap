package com.example.cropswap

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TenderApplication::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tenderApplicationDao(): TenderApplicationDao
} 