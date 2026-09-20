package com.example.focus_plan_builder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.focus_plan_builder.ui.theme.Focus_Plan_BuilderTheme

data class FocusPlan(
    val subject: String,
    val minutes: Int,
    val category: String,
    val breakMinutes: Int
)

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

    var subject by rememberSaveable() {
        mutableStateOf("")
    }

    var minutesText by rememberSaveable() {
        mutableStateOf("")
    }
    var plan by remember {
        mutableStateOf<FocusPlan?>(null)
    }
    val minutes: Int? = minutesText.toIntOrNull()

    val canCreatePlan =
        subject.isNotBlank() &&
                minutes != null &&
                minutes in 10..180


    fun createFocusPlan() {
        if (!canCreatePlan) return

        plan = FocusPlan(
            subject = subject.trim(),
            minutes = minutes,
            category = durationCategory(minutes),
            breakMinutes = recommendedBreak(minutes)
        )
    }






    FocusPlanScreen(modifier,
        subject,
        minutesText,
        plan,
        onSubjectChange = { newSubject:String ->
            subject = newSubject
            plan = null
        },
        onMinutesChange = { newMinutes:String ->
            minutesText = newMinutes
            plan = null
        },
        canCreatePlan,
        onCreatePlan = ::createFocusPlan
        )
}

@Composable
fun FocusPlanScreen(modifier: Modifier =
                        Modifier,
                    studySubject: String,
                    studyMinutes: String,
                    plan: FocusPlan?,
                    onSubjectChange: (String) -> Unit,
                    onMinutesChange: (String) -> Unit,
                    canCreatePlan: Boolean,
                    onCreatePlan: () -> Unit
                    ) {

    Column(
        modifier.fillMaxSize()
            .safeDrawingPadding()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),

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

            )

                OutlinedTextField(
                    value = studyMinutes,
                    onValueChange = {
                        onMinutesChange(it)
                    },

                    modifier = Modifier.weight(0.4f),

                    label = {
                        Text("Study Minutes")
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    maxLines = 1,
                )

        }

        Button(
            onClick = {
                onCreatePlan()
            },
            enabled = canCreatePlan
        ) {
            Text("Create Plan")
        }

        plan?.let { createdPlan ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = createdPlan.subject,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Duration: ${createdPlan.minutes} minutes")
                    Text("Category: ${createdPlan.category}")
                    Text("Recommended break: ${createdPlan.breakMinutes} minutes")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Study ${createdPlan.subject} for " +
                                "${createdPlan.minutes} minutes, and then take a " +
                                "${createdPlan.breakMinutes}-minute break."
                    )
                }
            }
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