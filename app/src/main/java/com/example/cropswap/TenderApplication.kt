package com.example.cropswap

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tender_applications")
data class TenderApplication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenderCounty: String,
    val tenderItem: String,
    val tenderDescription: String,
    val applicantName: String,
    val applicantContact: String,
    val timestamp: Long,
    val profilePhotoUri: String? = null
) 