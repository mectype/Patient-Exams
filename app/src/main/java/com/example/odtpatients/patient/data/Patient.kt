package com.example.odtpatients.patient.data


import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity
data class Patient(
    @PrimaryKey
    val name: String,

    @ColumnInfo
    val avatar: String,

    @ColumnInfo
    var clinicalNotes: String?,

    @ColumnInfo
    var imagesFolderPath: String? // path for images folder

) { constructor(name: String):this(name, "https://protocoderspoint.com/wp-content/uploads/2019/10/protocoderspoint-rectangle-round-.png", null , null)}
