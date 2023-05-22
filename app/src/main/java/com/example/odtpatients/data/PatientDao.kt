package com.example.odtpatients.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PatientDao {
    @Query("SELECT * FROM Patient")
    fun getAllPatients(): LiveData<List<Patient>>

    @Query("SELECT * FROM Patient WHERE name Like :fullName")
    fun findByName(fullName: String) : Patient

    @Insert
    fun addUser(patient: Patient)

    @Delete
    fun removePatient(patient: Patient)
}