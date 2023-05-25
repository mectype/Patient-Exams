package com.example.odtpatients;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odtpatients.databinding.PatientRowLayoutBinding;
import com.example.odtpatients.patient.data.Patient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.PatientViewHolder> {
    private List<Patient> patientsList;
    final File imageDir;

    public PatientsAdapter(File images_dir, List<Patient> list) {
        patientsList = list;
        imageDir = images_dir;
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
        holder.binding.patientName.setText(patient.getName());
        Picasso.get().load(patient.getAvatar()).into(holder.binding.patientAvatar);
    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        PatientRowLayoutBinding binding;

        public PatientViewHolder(@NonNull PatientRowLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
