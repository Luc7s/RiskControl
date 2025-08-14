package com.example.riskcontrol

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class MapaActivity : AppCompatActivity() {

    private lateinit var imageMapa: ImageView
    private val REQUEST_CODE_PICK_IMAGE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        imageMapa = findViewById(R.id.imageMapa)
        val btnExportar = findViewById<Button>(R.id.btnExportarMapa)
        val btnSubstituir = findViewById<Button>(R.id.btnSubstituirMapa)

        btnExportar.setOnClickListener {
            exportarMapa()
        }

        btnSubstituir.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }
    }

    private fun exportarMapa() {
        val bitmap = (imageMapa.drawable as BitmapDrawable).bitmap
        val file = File(getExternalFilesDir(null), "mapa_exportado.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageMapa.setImageURI(data.data)
        }
    }
}
