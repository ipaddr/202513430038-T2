package com.example.santaymahs

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.santaymahs.database.AppDatabase
import com.example.santaymahs.database.HasilTes
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        val chart = findViewById<LineChart>(R.id.lineChart)

        val adapter = HistoryAdapter()
        rvHistory.adapter = adapter
        rvHistory.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId != -1) {
            val db = AppDatabase.getDatabase(this)

            db.appDao().getAllHistory(userId).observe(this, Observer { data ->
                if (data.isNotEmpty()) {
                    adapter.setData(data)

                    setupChart(chart, data.reversed())
                }
            })
        }
    }

    private fun setupChart(chart: LineChart, historyList: List<HasilTes>) {
        val entries = ArrayList<Entry>()
        val dates = ArrayList<String>()

        historyList.forEachIndexed { index, hasil ->
            entries.add(Entry(index.toFloat(), hasil.totalSkor.toFloat()))

            val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
            dates.add(sdf.format(hasil.tanggalTes))
        }

        val dataSet = LineDataSet(entries, "Tingkat Stres")
        dataSet.color = ContextCompat.getColor(this, R.color.primary_sage)
        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.text_title)
        dataSet.lineWidth = 3f
        dataSet.circleRadius = 5f
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.accent_orange))
        dataSet.setDrawFilled(true) // Area bawah garis diwarnai
        dataSet.fillColor = ContextCompat.getColor(this, R.color.primary_light)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER // Garis melengkung halus

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textColor = ContextCompat.getColor(this, R.color.text_body)

        val yAxisLeft = chart.axisLeft
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.axisMaximum = 60f
        yAxisLeft.textColor = ContextCompat.getColor(this, R.color.text_body)

        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.animateX(1000)
        chart.invalidate()
    }
}