package com.example.odtpatients.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.odtpatients.R
import com.example.odtpatients.adapters.ImagesAdapter
import com.example.odtpatients.databinding.ActivityEditExamBinding
import com.example.odtpatients.patient.data.Patient
import com.example.odtpatients.patient.view_model.PatientsViewModel
import kotlinx.coroutines.launch
import java.io.File

class EditExamActivity : AppCompatActivity() {

    companion object {
        const val URI_IMAGE: String = "URI_IMAGE_TO_EDIT"
        const val FILE_PATH: String = "FILE_PATH"
    }

    private lateinit var imageAdapter: ImagesAdapter
    private lateinit var patientsViewModel: PatientsViewModel
    private lateinit var binding: ActivityEditExamBinding
    private lateinit var patient: Patient
    private lateinit var imageFilePath : String

    private val selectImageIntent = registerForActivityResult(ActivityResultContracts.GetContent())
    { uri ->
        uri?.let {
            val f = File(filesDir, patient.name.replace(" ".toRegex(), "_"))
            imageFilePath = File(f,System.currentTimeMillis().toString()+".jpeg").absolutePath
            val intent = Intent(this, EditImageActivity::class.java)
            intent.putExtra(URI_IMAGE, it.toString())
            intent.putExtra(FILE_PATH, imageFilePath)
            editImageForResult.launch(intent)
        }
        //imageView.setImageURI(uri)
    }

    private val editImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val image = File(imageFilePath)
            imageAdapter.addNewImage(image)
        } //else { }  // image discarded, do nothing
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditExamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        patientsViewModel = ViewModelProvider(this)[PatientsViewModel::class.java]
        lifecycleScope.launch {
            patient = patientsViewModel.findByName(intent.getStringExtra(MainActivity.PATIENT_NAME)!!)
            binding.clinicalNotes.setText(patient.clinicalNotes)

            val file = File(filesDir, patient.name.replace(" ".toRegex(), "_"))
            if(!file.exists()) {
                file.mkdir()
            }
            patient.imagesFolderPath = file.absolutePath

            imageAdapter = ImagesAdapter(patient.imagesFolderPath!!)
            binding.examImages.adapter = imageAdapter
            binding.examImages.layoutManager = LinearLayoutManager(baseContext)
            imageAdapter.notifyDataSetChanged()
        }
        binding.save.setOnClickListener {
            savePatientsDetails()
            Toast.makeText(this, R.string.record_saved,Toast.LENGTH_LONG).show()
            finish()
        }
        binding.newImage.setOnClickListener { addNewImage() }
        binding.back.setOnClickListener { finish() }
        binding.delete.setOnClickListener {
            showDialogForDelete()
        }
    }

    private fun showDialogForDelete() {
            val alert = AlertDialog.Builder(this)
            alert.setTitle(R.string.confirm_delete)
            alert.setPositiveButton(R.string.ok) { _, _ ->
                deletePatient()
            }
            alert.setNegativeButton(R.string.cancel) { _, _ -> }
            alert.show()
    }

    private fun deletePatient() {
        patientsViewModel.removePatient(patient)
        finish()
    }

    private fun addNewImage() {
        selectImageIntent.launch("image/*")
    }

    private fun savePatientsDetails() {
        patient.clinicalNotes = binding.clinicalNotes.text.toString()
        patientsViewModel.updatePatient(patient)
    }
}