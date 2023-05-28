package com.example.odtpatients.activities

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.green
import androidx.core.view.drawToBitmap
import com.example.odtpatients.databinding.EditImageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


class EditImageActivity : AppCompatActivity() {
    private lateinit var binding: EditImageBinding
    private lateinit var originalBitmap: Bitmap
    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filePath = intent.extras!!.getString(EditExamActivity.FILE_PATH)!!
        val uriString = intent.extras!!.getString(EditExamActivity.URI_IMAGE)
        val uri = Uri.parse(uriString)
        binding = EditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editedImage.setImageURI(uri)
        binding.editedImage.post { originalBitmap = binding.editedImage.drawToBitmap() }
        binding.imageDiscard.setOnClickListener { finish() }
        binding.imageSave.setOnClickListener { saveImageAndFinish() }
        binding.greenFilter.setOnClickListener { CoroutineScope(Dispatchers.IO).launch {setGreenFilter() }}
        binding.grayFilter.setOnClickListener { CoroutineScope(Dispatchers.IO).launch {setGrayFilter() }}
        binding.noFilter.setOnClickListener { setNoFilter() }
    }

    private fun saveImageAndFinish() {
        saveImageToFile()
        setResult(RESULT_OK)
        finish()
    }

    private fun saveImageToFile() {
        val bos = ByteArrayOutputStream()
        binding.editedImage.drawToBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()

        val fos = FileOutputStream(filePath)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
    }

    private fun setNoFilter() {
        binding.editedImage.setImageBitmap(originalBitmap)
    }

    private fun setGrayFilter() {
        val targetBitmap = originalBitmap.copy(originalBitmap.config, true)
        for(x in 0 until originalBitmap.width) {
            for (y in 0 until originalBitmap.height) {
                val color: Int = originalBitmap.getPixel(x, y)

                val red = Color.red(color)
                val green = Color.green(color)
                val blue = Color.blue(color)
                val avg = (red+green+blue) / 3
                targetBitmap.setPixel(x, y, Color.rgb(avg,avg,avg))
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            binding.editedImage.setImageBitmap(targetBitmap)
        }
    }

    private fun setGreenFilter() {
        val targetBitmap = originalBitmap.copy(originalBitmap.config, true)
        for(x in 0 until originalBitmap.width) {
            for (y in 0 until originalBitmap.height) {
                targetBitmap.setPixel(x, y, Color.rgb(0, originalBitmap.getPixel(x, y).green,0))
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            binding.editedImage.setImageBitmap(targetBitmap)
        }
    }
}