package com.example.santaymahs

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.santaymahs.database.AppDatabase
import com.example.santaymahs.database.User
import com.example.santaymahs.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                currentUser = db.appDao().getUserById(userId)
                currentUser?.let {
                    binding.etName.setText(it.nama)
                    binding.etAge.setText(it.usia.toString())
                    binding.etEmail.setText(it.email)
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val newName = binding.etName.text.toString()
            val newAge = binding.etAge.text.toString()
            val newEmail = binding.etEmail.text.toString()
            val newPass = binding.etPassword.text.toString()

            if (newName.isNotEmpty() && newAge.isNotEmpty() && newEmail.isNotEmpty()) {
                lifecycleScope.launch {
                    val updatedUser = currentUser?.copy(
                        nama = newName,
                        usia = newAge.toInt(),
                        email = newEmail,
                        password = if (newPass.isNotEmpty()) newPass else currentUser!!.password
                    )

                    if (updatedUser != null) {
                        db.appDao().updateUser(updatedUser)
                        Toast.makeText(this@EditProfileActivity, "Profil Berhasil Diupdate!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Nama, Email, dan Usia tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}