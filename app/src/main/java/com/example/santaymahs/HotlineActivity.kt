package com.example.santaymahs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HotlineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotline)

        findViewById<Button>(R.id.btnCallCounselor).setOnClickListener {
            val phoneNumber = "082288340118"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$phoneNumber")
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnCallEmergency).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.halodoc.com/tanya-dokter/kategori/kesehatan-mental")
            startActivity(intent)
        }
    }
}