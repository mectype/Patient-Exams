package com.example.odtpatients.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odtpatients.databinding.PatientRowLayoutBinding;
import com.example.odtpatients.interfaces.PatientRowClicked;
import com.example.odtpatients.patient.data.Patient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.PatientViewHolder> {
    private int rowIndex = -1;
    private final PatientRowClicked listener;
    private final List<Patient> patientsList;


    public PatientsAdapter(List<Patient> list, PatientRowClicked pListener) {
        patientsList = list;
        listener = pListener;
    }

    @NonNull
    @Override
    public PatientsAdapter.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PatientRowLayoutBinding view = PatientRowLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patientsList.get(position);
        holder.binding.getRoot().setOnClickListener(v-> {
            int oldIndex = rowIndex;
            rowIndex = holder.getBindingAdapterPosition();
            listener.onPatientClicked(patient, rowIndex);
            notifyItemChanged(rowIndex);
            if (oldIndex != - 1) {
                notifyItemChanged(oldIndex);
            }
        });
        holder.binding.patientName.setText(patient.getName());
        Picasso.get().load(patient.getAvatar()).into(holder.binding.patientAvatar);
        holder.binding.getRoot().setBackgroundColor((rowIndex == position) ? Color.BLUE : Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }

    public void resetSelectedPatient() {
        rowIndex = -1;
    }

    public void addPatient(Patient p) {
        patientsList.add(p);
        notifyItemInserted(getItemCount());
    }

    public boolean patientNameAlreadyExists(String patientName) {
        if (null == patientsList || patientsList.isEmpty()) return false;
        boolean foundName = false;
        for(Patient patient : patientsList) {
            if (patient.getName().equalsIgnoreCase(patientName)) {  // john Doe and John Doe should be the same name, so we would like to avoid that.
                foundName = true;
                break;
            }
        }
        return foundName;
    }

    public void removePatient(Patient currentPatient) {
        patientsList.remove(currentPatient);
        notifyItemRemoved(rowIndex);
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        PatientRowLayoutBinding binding;

        public PatientViewHolder(@NonNull PatientRowLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
