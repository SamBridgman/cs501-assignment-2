package com.example.focus_plan_builder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.focus_plan_builder.ui.theme.Focus_Plan_BuilderTheme

data class FocusPlan(
    val subject: String,
    val minutes: Int,
    val category: String,
    val breakMinutes: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Focus_Plan_BuilderTheme {
                FocusPlanRoute()
            }
        }
    }
}

@Composable
fun FocusPlanRoute(
    modifier: Modifier = Modifier
) {

    var studySubject by rememberSaveable() {
        mutableStateOf("")
    }

    var studySubjectError by rememberSaveable() {
        mutableStateOf(false)
    }

    var studyMinutes by rememberSaveable() {
        mutableStateOf("")
    }
    var plan: FocusPlan? = null

    var studyMinutesError by rememberSaveable() {
        mutableStateOf(false)
    }

    fun updateStudySubject(subject: String) {
        studySubject = subject
        if (subject.isBlank()) {
            studySubjectError = true
        }
        else {
            studySubjectError = false
        }
    }

    fun updateStudyMinutes(minutes: String) {

        val minutesAsInt: Int? = minutes.toIntOrNull()

        studyMinutes = minutes

        if (minutes.isBlank() || minutesAsInt !in 10..180 || minutesAsInt == null) {
            studyMinutesError = true
        }
        else {

            studyMinutesError = false
        }
    }
    fun durationCategory(minutes: Int): String  {
        return when  {
            (minutes < 10) -> "Invalid"
            (minutes in 10..29) -> "Quick Review"
            (minutes in 30..60) -> "Focused Session"
            else ->  "Extended Session"
        }
    }

    fun recommendedBreak(minutes: Int): Int {
        return when {
            (minutes < 10) -> 0
            (minutes in 10..29) -> 5
            (minutes in 30..60) -> 10
            else -> 15
        }
    }

    fun createFocusPlan() {
        val minutesAsInt: Int = studyMinutes.toInt()
        plan = FocusPlan(subject = studySubject, minutes = minutesAsInt, category = durationCategory(minutesAsInt), breakMinutes = recommendedBreak(minutesAsInt))
    }





    FocusPlanScreen(modifier,
        studySubject,
        studyMinutes,
        studySubjectError,
        studyMinutesError,
        plan,
        onSubjectChange = ::updateStudySubject,
        onMinuteChange = ::updateStudyMinutes,
        onCreatePlan = ::createFocusPlan
        )
}

@Composable
fun FocusPlanScreen(modifier: Modifier =
                        Modifier,
                    studySubject: String,
                    studyMinutes: String,
                    studySubjectError: Boolean,
                    studyMinutesError: Boolean,
                    plan: FocusPlan?,
                    onSubjectChange: (String) -> Unit,
                    onMinuteChange: (String) -> Unit,
                    onCreatePlan: () -> Unit
                    ) {

    Column(
        modifier.fillMaxSize()
            .safeDrawingPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Welcome to Focus Study Plan",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary

        )

        Text(
            text="This is an application that will help you focus and study better!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = studySubject,
                onValueChange = {
                    onSubjectChange(it)
                },

                label = {
                    Text("Study Subject")
                },


                modifier = Modifier.weight(0.6f),
                maxLines = 20,
                isError = studySubjectError,

                supportingText = {
                    if (studySubjectError) {
                        Text("Enter a study subject")
                    }
                }
            )

                OutlinedTextField(
                    value = studyMinutes,
                    onValueChange = {
                        onMinuteChange(it)
                    },

                    modifier = Modifier.weight(0.4f),

                    label = {
                        Text("Study Minutes")
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    maxLines = 1,
                    isError = studyMinutesError,

                    supportingText = {
                        if (studyMinutesError) {
                            Text("Number must be between 10-180")
                        }
                    }
                )

        }

        Button(
            onClick = {
                onCreatePlan()
            },
            enabled = !studyMinutesError && !studySubjectError && studySubject.isNotBlank() && studyMinutes.isNotBlank()
        ) {
            Text("Create Plan")
        }

        Card(
            modifier.fillMaxWidth()
        ) {

        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Focus_Plan_BuilderTheme {
//        Greeting("Android")
//    }
//}