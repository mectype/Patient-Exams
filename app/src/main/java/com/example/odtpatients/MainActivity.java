package com.example.odtpatients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;

import com.example.odtpatients.data.Patient;
import com.example.odtpatients.data.PatientDao;
import com.example.odtpatients.data.PatientDatabase;
import com.example.odtpatients.databinding.MainLayoutBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainLayoutBinding mainLayoutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayoutBinding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(R.layout.main_layout);

        PatientDatabase db = Room.databaseBuilder(getApplicationContext(),
                PatientDatabase.class, "patient-database").build();
        PatientDao patientDao = db.patientDao();
        LiveData<List<Patient>> patientList = patientDao.getAllPatients();
        List<Patient> patients = patientList.getValue();
        if (patients == null || patients.isEmpty()) {
            mainLayoutBinding.tvEmptyList.setVisibility(View.VISIBLE);
            mainLayoutBinding.fragmentContainer.setVisibility(View.GONE);
        } else {
            mainLayoutBinding.tvEmptyList.setVisibility(View.GONE);
            mainLayoutBinding.fragmentContainer.setVisibility(View.VISIBLE);
        }
    }
}