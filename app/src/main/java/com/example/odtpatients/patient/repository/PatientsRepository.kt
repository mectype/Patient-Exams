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

    suspend fun updatePatient(patient: Patient) {
        patientDao.updatePatient(patient)
    }

    suspend fun removePatient(patient: Patient) {
        patientDao.removePatient(patient)
    }

    suspend fun findByName(fullName : String) : Patient {
        return patientDao.findByName(fullName)
    }
}