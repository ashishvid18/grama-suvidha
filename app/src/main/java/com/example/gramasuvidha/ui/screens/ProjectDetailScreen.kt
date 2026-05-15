package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gramasuvidha.R
import com.example.gramasuvidha.data.model.Project
import com.example.gramasuvidha.ui.theme.ProgressGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    project: Project,
    onBackClick: () -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(text = project.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // Details
            Text(text = stringResource(R.string.budget, project.budget), fontWeight = FontWeight.SemiBold)
            Text(text = stringResource(R.string.completion_date, project.completionDate))
            Text(text = stringResource(R.string.status, project.status))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Progress Bar
            Text(text = "Digital Progress", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = project.progressPercentage / 100f,
                modifier = Modifier.fillMaxWidth().height(12.dp),
                color = ProgressGreen,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Text(
                text = stringResource(R.string.progress_complete, project.progressPercentage),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Images
            if (project.beforeImageUrl.isNotEmpty()) {
                Text(text = stringResource(R.string.before_photo), fontWeight = FontWeight.Bold)
                AsyncImage(
                    model = project.beforeImageUrl,
                    contentDescription = "Before Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(200.dp).padding(vertical = 8.dp)
                )
            }

            if (project.afterImageUrl.isNotEmpty()) {
                Text(text = stringResource(R.string.after_photo), fontWeight = FontWeight.Bold)
                AsyncImage(
                    model = project.afterImageUrl,
                    contentDescription = "After Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(200.dp).padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Feedback Section
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.citizen_feedback), style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = stringResource(R.string.rate_project))
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                for (i in 1..5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star $i",
                        tint = if (i <= rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { rating = i }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle report issue */ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.report_issue))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
