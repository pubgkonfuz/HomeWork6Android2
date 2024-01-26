package com.example.homework6android2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.homework6android2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            Log.e("isGranted", isGranted.toString() )
            if (isGranted) {
                shouldShowRequestPermissionRationale(
                    "Разрешение есть"
                )
            } else {
                createDialog()
            }
        }

    private fun setupListeners() = with(binding) {
        btnGallery.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getContent.launch("image/*")
                Log.e("permission", "Разрешение есть")
            }else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.ivImage.setImageURI(uri)
        }

    private fun createDialog() {
        AlertDialog.Builder(this).setTitle(getString(R.string.permision_read))
            .setMessage(getString(R.string.go_to_settings))
            .setPositiveButton(getString(R.string.go)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }.setNegativeButton(getString(R.string.no_i_wantto_stay)) { _, _ ->
            }.show()
    }
}