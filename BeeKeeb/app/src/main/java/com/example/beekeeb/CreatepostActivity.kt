package com.example.beekeeb

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.example.beekeeb.databinding.ActivityCreatepostBinding

class CreatepostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatepostBinding
    private lateinit var filePath: Uri
    private var chooseImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatepostBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private var chooseImageFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result -> if(result.resultCode == Activity.RESULT_OK && result.data != null){
            filePath = result.data!!.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            binding.ivImg.setImageBitmap(bitmap)
            chooseImage = true
        }
    }
}