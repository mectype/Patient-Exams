package com.example.odtpatients.interfaces

import com.example.odtpatients.patient.data.Patient

interface PatientRowClicked {
    fun onPatientClicked(patient : Patient, selectedIndex: Int)
}