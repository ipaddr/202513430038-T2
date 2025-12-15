package com.example.santaymahs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.santaymahs.database.AppDatabase
import com.example.santaymahs.database.HasilTes
import com.example.santaymahs.databinding.ActivityKuesionerBinding
import kotlinx.coroutines.launch

class KuesionerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKuesionerBinding
    
    private val questionBank = listOf(
        "Saya merasa terbebani dengan banyaknya tugas dan ujian.",
        "Jadwal kuliah dan tugas yang padat membuat saya stres.",
        "Saya kesulitan mengatur waktu antara kuliah, tugas, dan istirahat.",
        "Saya khawatir nilai saya tidak memenuhi harapan.",
        "Materi kuliah sulit dipahami dalam waktu singkat.",
        "Sistem kuliah (praktikum, kuis mendadak) membuat saya tertekan.",
        "Dosen mengajar terlalu cepat sehingga membuat saya stres.",
        "Tugas besar/skripsi membuat saya kewalahan.",
        "Tuntutan dari keluarga membuat saya tertekan.",
        "Saya merasa gagal jika tidak memenuhi standar pribadi saya.",
        "Saya kurang tidur karena banyak tugas kuliah.",
        "Saya merasa cemas memikirkan tugas dan kuliah.",
        "Saya kesulitan menjaga motivasi belajar karena tekanan.",
        "Hubungan dengan teman/kelompok membuat saya stres.",
        "Saya kurang mendapatkan dukungan dari teman/keluarga/dosen.",
        "Kegiatan organisasi menambah stres akademik saya.",
        "Masalah keuangan membuat saya stres.",
        "Saya khawatir mengenai masa depan atau karier.",
        "Aktivitas kuliah membuat saya sering kelelahan.",
        "Saya stres ketika tugas kelompok tidak berjalan baik.",
        "Saya sulit fokus karena terlalu banyak pikiran tentang kuliah.",
        "Skripsi/proyek akhir membuat saya merasa kehilangan kontrol.",
        "Tekanan sosial dari lingkungan membuat saya stres.",
        "Saya tidak punya cukup waktu untuk relaksasi karena tugas kuliah.",
        "Saya pernah berpikir mengambil cuti karena tekanan kuliah terlalu berat.",
        "Tekanan akademik membuat mood saya berubah-ubah.",
        "Saya merasa kemampuan saya menurun ketika stres.",
        "Saya sulit mengontrol emosi ketika banyak tugas.",
        "Lingkungan kampus terasa kurang mendukung kesehatan mental.",
        "Stres akademik memengaruhi interaksi saya dengan orang lain.",
        "Saya merasa stres ketika banyak deadline datang bersamaan.",
        "Saya merasa tidak mampu mengimbangi beban belajar di kampus.",
        "Saya merasa takut mengecewakan orang tua terkait prestasi kuliah.",
        "Saya sulit tidur karena memikirkan tugas/ujian.",
        "Saya sering merasa putus asa memikirkan kuliah.",
        "Saya kehilangan minat pada hal yang biasanya menyenangkan karena kuliah.",
        "Saya merasa kemampuan saya menurun saat tekanan tinggi.",
        "Saya menghindari tugas karena merasa kewalahan.",
        "Saya sering merasa pusing saat menghadapi tekanan akademik.",
        "Saya sering kehilangan konsentrasi ketika belajar.",
        "Saya stres ketika mempelajari mata kuliah yang sulit/minim minat.",
        "Saya takut gagal dalam ujian atau tugas besar.",
        "Saya cemas memikirkan IPK saya.",
        "Saya merasa tekanan dari teman-teman dalam persaingan akademik.",
        "Saya stres saat mengerjakan tugas yang membutuhkan waktu lama.",
        "Saya merasa tidak siap menghadapi beban tiap semester.",
        "Saya gugup saat presentasi di depan kelas.",
        "Media sosial membuat saya merasa tertekan terkait kuliah.",
        "Saya sulit menyeimbangkan waktu antara kuliah dan kehidupan pribadi.",
        "Saya stres ketika harus beradaptasi dengan lingkungan kampus.",
        "Saya merasa stres ketika mengambil terlalu banyak mata kuliah.",
        "Saya sering menunda tugas karena merasa kewalahan mental.",
        "Saya menarik diri dari pergaulan karena stres akademik."
    )

    private var activeQuestions: List<String> = emptyList()
    private var currentQuestionIndex = 0
    private var totalScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKuesionerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activeQuestions = questionBank.shuffled().take(20)

        binding.progressBar.max = activeQuestions.size
        binding.progressBar.progress = 0

        loadQuestion()

        binding.btnNextQuestion.setOnClickListener {
            val selectedId = binding.rgOptions.checkedRadioButtonId
            if (selectedId != -1) {
                val score = getScore(selectedId)
                totalScore += score

                currentQuestionIndex++
                if (currentQuestionIndex < activeQuestions.size) {
                    loadQuestion()
                    binding.rgOptions.clearCheck()
                } else {
                    saveAndFinish()
                }
            } else {
                Toast.makeText(this, "Pilih salah satu jawaban", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadQuestion() {
        binding.tvProgress.text = "Pertanyaan ${currentQuestionIndex + 1} dari ${activeQuestions.size}"
        binding.tvQuestionText.text = activeQuestions[currentQuestionIndex]

        binding.progressBar.progress = currentQuestionIndex + 1

        binding.btnNextQuestion.text = if (currentQuestionIndex == activeQuestions.size - 1) "Selesai" else "Lanjut"
    }

    private fun getScore(viewId: Int): Int {
        return when (viewId) {
            R.id.rbOption0 -> 0
            R.id.rbOption1 -> 1
            R.id.rbOption2 -> 2
            R.id.rbOption3 -> 3
            else -> 0
        }
    }

    private fun saveAndFinish() {

        val level = when {
            totalScore <= 20 -> "Stres Ringan"
            totalScore <= 40 -> "Stres Sedang"
            else -> "Stres Berat"
        }

        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                val hasil = HasilTes(
                    userId = userId,
                    totalSkor = totalScore,
                    levelStres = level,
                    tanggalTes = System.currentTimeMillis()
                )
                AppDatabase.getDatabase(this@KuesionerActivity).appDao().insertHasil(hasil)

                val intent = Intent(this@KuesionerActivity, ResultActivity::class.java)
                intent.putExtra("SCORE", totalScore)
                intent.putExtra("LEVEL", level)
                intent.putExtra("ANXIETY", 0)
                intent.putExtra("DEPRESSION", 0)

                startActivity(intent)
                finish()
            }
        }
    }
}