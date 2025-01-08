package com.example.kotlinmultiplatformminiproject.android.ui.eventmodify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kotlinmultiplatformminiproject.domain.Event
import com.example.kotlinmultiplatformminiproject.android.MyApplicationTheme
import com.example.kotlinmultiplatformminiproject.android.ui.components.AppLoading

@Composable
fun EventModifyScreen(
    navController: NavController,
    viewModel: EventModifyViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    if (viewModel.showLoading.collectAsStateWithLifecycle().value) {
        AppLoading()
    }

    EventModifyContent(
        onCancel = { navController.popBackStack() },
        event = uiState?.event,
        onNameChanged = { viewModel.onNameChanged(it) },
        onCityChanged = { viewModel.onCityChanged(it) },
        onCountryChanged = { viewModel.onCountryChanged(it) },
        onCapacityChanged = { viewModel.onCapacityChanged(it) },
        modifiedEvent = { event ->
            viewModel.modifiedEvent(event, navController)
        }
    )
}

@Composable
fun EventModifyContent(
    onCancel: () -> Unit,
    event: Event?,
    onNameChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onCapacityChanged: (String) -> Unit,
    modifiedEvent: (Event?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "Modify event",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    value = event?.name ?: "",
                    onValueChange = { onNameChanged(it) },
                    label = { Text("Name") },
                )


                Text(
                    text = "Place",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    modifier = Modifier.padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    value = event?.location ?: "",
                    onValueChange = { onCityChanged(it) },
                    label = { Text("City") },
                )

                OutlinedTextField(
                    modifier = Modifier.padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    value = event?.country ?: "",
                    onValueChange = { onCountryChanged(it) },
                    label = { Text("Country") },
                )

                OutlinedTextField(
                    modifier = Modifier.padding(bottom = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    value = event?.capacity.toString(),
                    onValueChange = { onCapacityChanged(it) },
                    label = { Text("Capacity") },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onCancel() },
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = { modifiedEvent(event) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun EventModifyContentPreview() {
    MyApplicationTheme {
        EventModifyContent(
            onCancel = { },
            event = Event(
                id = 0,
                name = "",
                location = "",
                country = "",
                capacity = 0
            ),
            onNameChanged = { },
            onCityChanged = { },
            onCountryChanged = { },
            onCapacityChanged = { },
            modifiedEvent = { }
            )
    }
}