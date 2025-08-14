//sumaia
package com.example.cgpa_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class Semester(val gpa: String = "", val credits: String = "")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CGPACalculatorApp()
        }
    }
}

@Composable
fun CGPACalculatorApp() {
    var semesters by remember { mutableStateOf(listOf(Semester())) }

    fun updateSemester(index: Int, newSemester: Semester) {
        semesters = semesters.toMutableList().also {
            it[index] = newSemester
        }
    }

    val (totalWeightedGPA, totalCredits) = semesters.fold(0.0 to 0.0) { acc, sem ->
        val gpa = sem.gpa.toDoubleOrNull() ?: 0.0
        val credits = sem.credits.toDoubleOrNull() ?: 0.0
        acc.first + gpa * credits to acc.second + credits
    }
    val cgpa = if (totalCredits > 0) totalWeightedGPA / totalCredits else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("CGPA Calculator", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(semesters) { index, semester ->
                SemesterInput(
                    semester = semester,
                    onGpaChange = { gpa -> updateSemester(index, semester.copy(gpa = gpa)) },
                    onCreditsChange = { credits -> updateSemester(index, semester.copy(credits = credits)) }
                )
            }
        }

        Button(
            onClick = {
                semesters = semesters + Semester()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Semester")
        }

        Text(
            text = "Cumulative CGPA: %.2f".format(cgpa),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun SemesterInput(
    semester: Semester,
    onGpaChange: (String) -> Unit,
    onCreditsChange: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = semester.gpa,
            onValueChange = onGpaChange,
            label = { Text("GPA") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = semester.credits,
            onValueChange = onCreditsChange,
            label = { Text("Credits") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
