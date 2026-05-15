package com.example.gramasuvidha.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramasuvidha.data.local.AppDatabase
import com.example.gramasuvidha.data.model.Project
import com.example.gramasuvidha.data.repository.ProjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectRepository

    val projects: StateFlow<List<Project>>

    init {
        val projectDao = AppDatabase.getDatabase(application).projectDao()
        repository = ProjectRepository(projectDao, application)
        
        // Convert Flow from Repository to StateFlow for Compose
        projects = repository.allProjects.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.checkAndLoadMockData()
        }
    }
}
