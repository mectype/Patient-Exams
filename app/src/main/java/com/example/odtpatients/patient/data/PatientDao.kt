package com.example.odtpatients.patient.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    suspend fun addPatient(patient: Patient)

    @Delete
    suspend fun removePatient(patient: Patient)
}