package com.example.odtpatients.patient.repository

import com.example.odtpatients.patient.data.Patient
import com.example.odtpatients.patient.data.PatientDatabase

class PatientsRepository(patientsDatabase: PatientDatabase) {
    private var patientDao = patientsDatabase.patientDao()
    private var patientsList = patientDao.getAllPatients()

    fun getAllPatients() = patientsList
    suspend fun addPatient(patient: Patient) {
            patientDao.addPatient(patient)
    }

    suspend fun removePatient(patient: Patient) {
        patientDao.removePatient(patient)
    }
}