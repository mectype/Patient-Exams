package com.example.odtpatients.patient.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room.databaseBuilder
import com.example.odtpatients.GetAvatars
import com.example.odtpatients.patient.data.Patient
import com.example.odtpatients.patient.data.PatientDatabase
import com.example.odtpatients.patient.repository.PatientsRepository
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    }

    fun updatePatient(patient: Patient) {
        viewModelScope.launch {
            patientsRepository.updatePatient(patient)
        }
    }
    fun removePatient(patient: Patient) {
        viewModelScope.launch {
            patientsRepository.removePatient(patient)
        }
    }

    suspend fun findByName(fullName: String) : Patient {
        return patientsRepository.findByName(fullName)
    }

    fun generateNewAvatarIcon(position : Int) {
        val api = "https://api.pexels.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(api)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val service = retrofit.create(GetAvatars::class.java)
        val call = service.getPexelsImages("face,icon")
        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
            onSuccess = {
                it?.let { pexel ->
                    viewModelScope.launch {
                        patientsLiveData.value?.last()?.let { newPatient ->
                            newPatient.avatar = pexel.photos[position].src.small
                            updatePatient(newPatient)
                        }
                    }
                }
            }, onError = {
                android.util.Log.e("subscribeBy Fail", "Fail ${it.message}")
            }
        )
    }
}