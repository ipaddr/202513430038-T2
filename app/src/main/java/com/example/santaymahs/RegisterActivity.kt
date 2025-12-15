package com.example.santaymahs

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.santaymahs.database.AppDatabase
import com.example.santaymahs.database.User
import com.example.santaymahs.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val genders = arrayOf("Laki-laki", "Perempuan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        binding.spinnerGender.adapter = adapter

        val db = AppDatabase.getDatabase(this)

        binding.btnRegister.setOnClickListener {
            val nama = binding.etName.text.toString()
            val usiaStr = binding.etAge.text.toString()
            val email = binding.etRegEmail.text.toString()
            val pass = binding.etRegPassword.text.toString()
            val gender = binding.spinnerGender.selectedItem.toString()

            if (nama.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && usiaStr.isNotEmpty()) {
                lifecycleScope.launch {
                    val newUser = User(
                        nama = nama,
                        email = email,
                        password = pass,
                        usia = usiaStr.toInt(),
                        jenisKelamin = gender
                    )
                    db.appDao().insertUser(newUser)
                    Toast.makeText(this@RegisterActivity, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Mohon lengkapi data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}