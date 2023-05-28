package com.example.odtpatients.patient.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PatientDao {
    @Query("SELECT * FROM Patient")
    fun getAllPatients(): LiveData<List<Patient>>

    @Query("SELECT * FROM Patient WHERE name Like :fullName")
    suspend fun findByName(fullName: String) : Patient

    @Update
    suspend fun updatePatient(patient: Patient)

    @Insert
    suspend fun addPatient(patient: Patient)

    @Delete
    suspend fun removePatient(patient: Patient)
}