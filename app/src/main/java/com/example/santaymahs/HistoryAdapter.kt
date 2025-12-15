package com.example.santaymahs

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.santaymahs.database.HasilTes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var listHistory = emptyList<HasilTes>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvLevel: TextView = view.findViewById(R.id.tvLevel)
        val tvScore: TextView = view.findViewById(R.id.tvScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listHistory[position]

        val date = Date(item.tanggalTes)
        val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

        holder.tvDate.text = format.format(date)
        holder.tvLevel.text = item.levelStres
        holder.tvScore.text = "Skor: ${item.totalSkor}"

        when (item.levelStres) {
            "Stres Ringan" -> holder.tvLevel.setTextColor(Color.parseColor("#66BB6A"))
            "Stres Sedang" -> holder.tvLevel.setTextColor(Color.parseColor("#FF9800"))
            "Stres Berat" -> holder.tvLevel.setTextColor(Color.parseColor("#F44336"))
        }
    }

    override fun getItemCount(): Int = listHistory.size

    fun setData(data: List<HasilTes>) {
        this.listHistory = data
        notifyDataSetChanged()
    }
}