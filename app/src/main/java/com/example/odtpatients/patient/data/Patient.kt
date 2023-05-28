package com.example.odtpatients.patient.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Patient(
    @PrimaryKey
    val name: String,

    @ColumnInfo
    var avatar: String?,

    @ColumnInfo
    var clinicalNotes: String?,

    @ColumnInfo
    var imagesFolderPath: String? // path for images folder
)
{    constructor(name: String):this(name, null, null , null) }
