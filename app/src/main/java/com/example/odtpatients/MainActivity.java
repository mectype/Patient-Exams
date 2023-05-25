package com.example.odtpatients;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.odtpatients.databinding.MainLayoutBinding;
import com.example.odtpatients.patient.data.Patient;
import com.example.odtpatients.patient.view_model.PatientsViewModel;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    final static String IMAGES_FOLDER = "images";
    private MainLayoutBinding mainLayoutBinding;
    private PatientsViewModel patientsViewModel;
    private List<Patient> patients;
    private PatientsAdapter patientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayoutBinding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(mainLayoutBinding.getRoot());

        File imagesDir = new File(getCacheDir(), IMAGES_FOLDER);
        if (!imagesDir.exists()) {
            if (!imagesDir.mkdir()) {
                Log.e("MainActivity", "Falied to create folder for images");
                System.exit(1);
            }
        }

        patientsViewModel = new ViewModelProvider(this).get(PatientsViewModel.class);

        patientsViewModel.getAllPatients().observe(this, it ->
        {
            patients = it;
            if (patients == null || patients.isEmpty()) {
                mainLayoutBinding.tvEmptyList.setVisibility(View.VISIBLE);
                mainLayoutBinding.patientsList.setVisibility(View.GONE);
            } else {
                mainLayoutBinding.tvEmptyList.setVisibility(View.GONE);
                mainLayoutBinding.patientsList.setVisibility(View.VISIBLE);
            }
            if(patientsAdapter == null) {
                patientsAdapter = new PatientsAdapter(imagesDir, patients);
                mainLayoutBinding.patientsList.setAdapter(patientsAdapter);
                mainLayoutBinding.patientsList.setLayoutManager(new LinearLayoutManager(this));
            }
            patientsAdapter.notifyDataSetChanged();
        });
        mainLayoutBinding.addPatient.setOnClickListener((
                v -> addNewPatient() ));
    }

    private void addNewPatient() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage(R.string.new_patient_name);
        alert.setTitle(R.string.add);

        alert.setView(edittext);

        alert.setPositiveButton(R.string.ok, (dialog, whichButton) -> {
            String patientName = edittext.getText().toString().trim();  // add trim, to avoid spaces in both ends of name.
            if(!patientName.isEmpty()) {    // Avoid accidental add on empty name
                if (patientNameAlreadyExists(patientName)) {
                    Toast.makeText(this,R.string.patient_name_exists, Toast.LENGTH_LONG).show();
                } else {
                    Patient p = new Patient(patientName);
                    patients.add(p);
                    patientsAdapter.notifyItemInserted(patients.size());
                    patientsViewModel.addPatient(p);
                }
            }
        });

        alert.setNegativeButton(R.string.cancel, (dialog, whichButton) -> {
            //discard, no need to do nothing
        });

        alert.show();
    }

    private boolean patientNameAlreadyExists(String patientName) {
        if (null == patients || patients.isEmpty()) return false;
        boolean foundName = false;
        for(Patient patient : patients) {
            if (patient.getName().equalsIgnoreCase(patientName)) {  // john Doe and John Doe should be the same name, so we would like to avoid that.
                foundName = true;
                break;
            }
        }
        return foundName;
    }
}