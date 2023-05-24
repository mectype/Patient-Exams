package com.example.odtpatients.patient.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Patient(
    @PrimaryKey
    val name: String,

    @ColumnInfo
    val avatar: String?,

    @ColumnInfo
    var clinicalNotes: String?,

    @ColumnInfo
    var images: List<String>?

) { constructor(name: String):this(name, null, null , null)}
