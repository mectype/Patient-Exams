package com.example.odtpatients.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.odtpatients.R;
import com.example.odtpatients.adapters.PatientsAdapter;
import com.example.odtpatients.databinding.MainLayoutBinding;
import com.example.odtpatients.interfaces.PatientRowClicked;
import com.example.odtpatients.patient.data.Patient;
import com.example.odtpatients.patient.view_model.PatientsViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LifecycleOwner, PatientRowClicked {
    final static String PATIENT_NAME = "patient_name";

    File avatarsImagesDir;
    final static String IMAGES_FOLDER = "images";
    private MainLayoutBinding mainLayoutBinding;
    private PatientsViewModel patientsViewModel;
    private PatientsAdapter patientsAdapter;
    private Patient currentPatient; // Indicating patient to do actions for (view/remove/export)
    private int rowIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayoutBinding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(mainLayoutBinding.getRoot());

        avatarsImagesDir = new File(getCacheDir(), IMAGES_FOLDER);
        if (!avatarsImagesDir.exists()) {
            if (!avatarsImagesDir.mkdir()) {
                Log.e("MainActivity", "Failed to create folder for images");
                System.exit(1);
            }
        }

        patientsViewModel = new ViewModelProvider(this).get(PatientsViewModel.class);
        patientsViewModel.getAllPatients().observe(this, this::updatePatients);

        mainLayoutBinding.addPatient.setOnClickListener(v -> addNewPatient());
        mainLayoutBinding.removePatient.setOnClickListener(v ->  removePatient());
        mainLayoutBinding.viewPatient.setOnClickListener(v ->  viewPatient());
        mainLayoutBinding.exportPatient.setOnClickListener(v ->  exportPatient());
    }

    private boolean assertPatientSelected() {
        boolean patientSelected = (rowIndex != -1);
        if (!patientSelected) {
            Toast.makeText(this, R.string.no_patient_selected, Toast.LENGTH_LONG).show();
        }
        return patientSelected;
    }
    private void exportPatient() {
        if (assertPatientSelected()) {
            showDialogForExport();
        }
    }

    private void showDialogForExport() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.confirm_export);
        alert.setPositiveButton(R.string.ok, (dialog, whichButton) -> {
            exportPatientRecord();
            rowIndex = -1;
        });

        alert.setNegativeButton(R.string.cancel, (dialog, whichButton) -> {  });
        alert.show();
    }

    private void exportPatientRecord() {
        File f = new File(getFilesDir(), currentPatient.getName().replaceAll(" ","_"));
        if(f.mkdir()) {
            Type type = new TypeToken<List<Patient>>(){}.getType();
            String stdJson = new Gson().toJson(currentPatient, type);

            try (FileWriter writer = new FileWriter(new File(f, "data.json"))) {
                writer.write(stdJson);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(this, "Could not make folder "+f.getName(),Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void viewPatient() {
        if (assertPatientSelected()) {
            Intent intent = new Intent(MainActivity.this, EditExamActivity.class);
            intent.putExtra(PATIENT_NAME, currentPatient.getName());
            startActivity(intent);
        }
    }

    private void removePatient() {
        if (assertPatientSelected()) {
            showDialogForRemove();
        }
    }

    private void showDialogForRemove() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.confirm_delete);
        alert.setPositiveButton(R.string.ok, (dialog, whichButton) -> {
            patientsViewModel.removePatient(currentPatient);
            patientsAdapter.removePatient(currentPatient);
            rowIndex = -1;
        });

        alert.setNegativeButton(R.string.cancel, (dialog, whichButton) -> {  });
        alert.show();
    }

    private void addNewPatient() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage(R.string.new_patient_name);
        //alert.setTitle(R.string.add);

        alert.setView(edittext);

        alert.setPositiveButton(R.string.ok, (dialog, whichButton) -> {
            String patientName = edittext.getText().toString().trim();  // add trim, to avoid spaces in both ends of name.
            if(!patientName.isEmpty()) {    // Avoid accidental add on empty name
                if (patientsAdapter.patientNameAlreadyExists(patientName)) {
                    Toast.makeText(this,R.string.patient_name_exists, Toast.LENGTH_LONG).show();
                } else {
                    Patient p = new Patient(patientName);
                    patientsAdapter.addPatient(p);
                    patientsViewModel.addPatient(p);
                }
            }
        });

        alert.setNegativeButton(R.string.cancel, (dialog, whichButton) -> {
            //discard, no need to do nothing
        });

        alert.show();
    }

    private void updatePatients(List<Patient> patients) {
        android.util.Log.e("GUYGUY", "updatePatients "+patients.size());
        if (patients.isEmpty()) {
            mainLayoutBinding.tvEmptyList.setVisibility(View.VISIBLE);
            mainLayoutBinding.patientsList.setVisibility(View.GONE);
        } else {
            mainLayoutBinding.tvEmptyList.setVisibility(View.GONE);
            mainLayoutBinding.patientsList.setVisibility(View.VISIBLE);
        }
        if(patientsAdapter == null) {
            patientsAdapter = new PatientsAdapter(patients, this);
            mainLayoutBinding.patientsList.setAdapter(patientsAdapter);
            mainLayoutBinding.patientsList.setLayoutManager(new LinearLayoutManager(this));
        }
        patientsAdapter.resetSelectedPatient();
        patientsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPatientClicked(@NonNull Patient patient, final int selectedIndex) {
        currentPatient = patient;
        rowIndex = selectedIndex;
    }
}