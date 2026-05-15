package com.example.gramasuvidha.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val budget: String,
    val completionDate: String,
    val status: String,
    val progressPercentage: Int,
    val beforeImageUrl: String,
    val afterImageUrl: String
)
