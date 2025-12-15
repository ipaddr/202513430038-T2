package com.example.santaymahs

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.santaymahs.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scoreStress = intent.getIntExtra("SCORE", 0)
        val level = intent.getStringExtra("LEVEL") ?: "Normal"

        binding.tvLevelStres.text = level.uppercase()
        binding.tvScoreValue.text = scoreStress.toString()

        val animation = ObjectAnimator.ofInt(binding.progressBarScore, "progress", 0, scoreStress)
        animation.duration = 1500 // 1.5 detik
        animation.interpolator = DecelerateInterpolator()
        animation.start()

        updateUIBasedOnLevel(level)

        binding.btnBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun updateUIBasedOnLevel(level: String) {
        when (level) {
            "Normal" -> {
                binding.tvEmoji.text = "😄"
                binding.tvLevelStres.setTextColor(ContextCompat.getColor(this, R.color.primary_sage))
                binding.progressBarScore.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_sage))

                binding.tvRecommendation.text = "Hebat! Kondisi mentalmu sangat prima.\n\n" +
                        "✅ Tetap pertahankan hobi positifmu.\n" +
                        "✅ Bagikan energi positif ke teman-teman."

                binding.btnActionPrimary.visibility = View.GONE
            }
            "Stres Ringan" -> {
                binding.tvEmoji.text = "🙂"
                binding.tvLevelStres.setTextColor(ContextCompat.getColor(this, R.color.primary_sage))
                binding.progressBarScore.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_sage))

                binding.tvRecommendation.text = "Ada sedikit tekanan, tapi masih terkendali.\n\n" +
                        "💡 Coba buat To-Do List agar tugas tidak menumpuk.\n" +
                        "💡 Tidur 7-8 jam malam ini."

                binding.btnActionPrimary.text = "Profil Saya"
                binding.btnActionPrimary.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_orange))
                binding.btnActionPrimary.setOnClickListener {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
            }
            "Stres Sedang" -> {
                binding.tvEmoji.text = "😐"
                binding.tvLevelStres.setTextColor(ContextCompat.getColor(this, R.color.accent_orange))
                binding.progressBarScore.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_orange))

                binding.tvRecommendation.text = "Kamu sepertinya butuh istirahat sejenak.\n\n" +
                        "⚠️ Lakukan teknik pernapasan 4-7-8 sekarang.\n" +
                        "⚠️ Ceritakan masalahmu ke teman dekat."

                binding.btnActionPrimary.text = "Cari Bantuan"
                binding.btnActionPrimary.setOnClickListener {
                    startActivity(Intent(this, HotlineActivity::class.java))
                }
            }
            else -> {
                binding.tvEmoji.text = "😫"
                binding.tvLevelStres.setTextColor(ContextCompat.getColor(this, R.color.red_danger))
                binding.progressBarScore.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_danger))

                binding.tvRecommendation.text = "Kondisi ini memerlukan perhatian serius.\n\n" +
                        "🆘 Jangan dipendam sendiri.\n" +
                        "🆘 Sangat disarankan menghubungi Konselor Kampus."

                binding.btnActionPrimary.text = "DARURAT"
                binding.btnActionPrimary.setOnClickListener {
                    startActivity(Intent(this, HotlineActivity::class.java))
                }
            }
        }
    }
}