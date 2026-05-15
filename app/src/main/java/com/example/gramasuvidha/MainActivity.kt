package com.example.gramasuvidha

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gramasuvidha.data.model.Project
import com.example.gramasuvidha.ui.screens.ProjectDetailScreen
import com.example.gramasuvidha.ui.screens.ProjectListScreen
import com.example.gramasuvidha.ui.theme.GramaSuvidhaTheme
import com.example.gramasuvidha.ui.viewmodel.ProjectViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: ProjectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Read saved language preference or default to English
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val languageCode = prefs.getString("language", "en") ?: "en"
        setLocale(this, languageCode)

        setContent {
            GramaSuvidhaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val projects by viewModel.projects.collectAsStateWithLifecycle()
                    var selectedProject by remember { mutableStateOf<Project?>(null) }

                    if (selectedProject == null) {
                        ProjectListScreen(
                            projects = projects,
                            onProjectClick = { selectedProject = it },
                            onSwitchLanguage = {
                                val newLang = if (languageCode == "en") "kn" else "en"
                                prefs.edit().putString("language", newLang).apply()
                                setLocale(this@MainActivity, newLang)
                                recreate() // Recreate activity to apply new language
                            }
                        )
                    } else {
                        ProjectDetailScreen(
                            project = selectedProject!!,
                            onBackClick = { selectedProject = null }
                        )
                    }
                }
            }
        }
    }

    private fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
