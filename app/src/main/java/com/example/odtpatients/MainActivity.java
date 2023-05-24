package com.example.odtpatients;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.odtpatients.databinding.MainLayoutBinding;
import com.example.odtpatients.patient.data.Patient;
import com.example.odtpatients.patient.view_model.PatientsViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    private MainLayoutBinding mainLayoutBinding;
    private PatientsViewModel patientsViewModel;
    private List<Patient> patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayoutBinding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(mainLayoutBinding.getRoot());
        patientsViewModel = new ViewModelProvider(this).get(PatientsViewModel.class);

        patientsViewModel.getAllPatients().observe(this, it ->
        {
            patients = it;
            if (patients == null || patients.isEmpty()) {
                mainLayoutBinding.tvEmptyList.setVisibility(View.VISIBLE);
                mainLayoutBinding.fragmentContainer.setVisibility(View.GONE);
            } else {
                mainLayoutBinding.tvEmptyList.setVisibility(View.GONE);
                mainLayoutBinding.fragmentContainer.setVisibility(View.VISIBLE);
            }
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
                    Toast.makeText(this,"Exists",Toast.LENGTH_LONG).show();
                } else {
                    patientsViewModel.addPatient(new Patient(patientName));
                    Toast.makeText(this,"NOT Exists",Toast.LENGTH_LONG).show();
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