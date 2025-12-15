package com.example.santaymahs.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "hasil_tes_table",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HasilTes(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val totalSkor: Int,
    val levelStres: String,
    val tanggalTes: Long
)