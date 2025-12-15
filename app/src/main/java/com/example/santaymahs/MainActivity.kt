package com.example.santaymahs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.santaymahs.database.AppDatabase
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnMulaiTes).setOnClickListener {
            startActivity(Intent(this, KuesionerActivity::class.java))
        }

        findViewById<View>(R.id.btnRiwayat).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<View>(R.id.btnProfil).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<View>(R.id.btnBantuan).setOnClickListener {
            startActivity(Intent(this, HotlineActivity::class.java))
        }

        val cardLastTest = findViewById<CardView>(R.id.cardLastTest)
        val tvTanggal = findViewById<TextView>(R.id.tvTanggalTerakhir)
        val tvLevel = findViewById<TextView>(R.id.tvLevelTerakhir)

        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId != -1) {
            val db = AppDatabase.getDatabase(this)

            db.appDao().getLatestResult(userId).observe(this, Observer { hasil ->
                if (hasil != null) {
                    cardLastTest.visibility = View.VISIBLE

                    val date = java.util.Date(hasil.tanggalTes)
                    val format = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                    tvTanggal.text = format.format(date)

                    tvLevel.text = hasil.levelStres
                } else {
                    cardLastTest.visibility = View.GONE
                }
            })
        }

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(14, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "WeeklyCheck",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}