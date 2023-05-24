package com.example.odtpatients.patient.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room.databaseBuilder
import com.example.odtpatients.patient.data.Patient
import com.example.odtpatients.patient.data.PatientDatabase
import com.example.odtpatients.patient.repository.PatientsRepository
import kotlinx.coroutines.launch

class PatientsViewModel(application: Application) : AndroidViewModel(application) {
    private val patientsLiveData : LiveData<List<Patient>>
    private val patientsRepository : PatientsRepository

    init {
        val db = databaseBuilder(application, PatientDatabase::class.java, "patient-database").build()
        patientsRepository = PatientsRepository(db)
        patientsLiveData = patientsRepository.getAllPatients()
    }
    fun getAllPatients() : LiveData<List<Patient>> {
      return patientsLiveData
    }
    fun addPatient(patient: Patient) {
        viewModelScope.launch {
            patientsRepository.addPatient(patient)
        }
        //patientsLiveData.value.add(patient)
    }
}