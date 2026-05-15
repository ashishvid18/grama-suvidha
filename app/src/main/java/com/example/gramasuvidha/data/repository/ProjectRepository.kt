package com.example.gramasuvidha.data.repository

import android.content.Context
import com.example.gramasuvidha.data.local.ProjectDao
import com.example.gramasuvidha.data.model.Project
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val context: Context
) {
    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()

    suspend fun checkAndLoadMockData() {
        withContext(Dispatchers.IO) {
            if (projectDao.getProjectCount() == 0) {
                // Read from JSON if database is empty
                try {
                    val inputStream = context.assets.open("projects.json")
                    val reader = InputStreamReader(inputStream)
                    val projectListType = object : TypeToken<List<Project>>() {}.type
                    val projects: List<Project> = Gson().fromJson(reader, projectListType)
                    
                    projectDao.insertAll(projects)
                    reader.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
