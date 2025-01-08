package com.example.kotlinmultiplatformminiproject.android.ui.eventlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kotlinmultiplatformminiproject.domain.Event
import com.example.kotlinmultiplatformminiproject.android.MyApplicationTheme
import com.example.kotlinmultiplatformminiproject.android.Route
import com.example.kotlinmultiplatformminiproject.android.ui.components.AppLoading

@Composable
fun EventListScreen(
    navController: NavController,
    viewModel: EventListViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    if (viewModel.showLoading.collectAsStateWithLifecycle().value) {
        AppLoading()
    }

    EventListContent(
        navigateToEventModify = {
            viewModel.navigateToModifyEvent(it, navController)
        },
        onAddClicked = {
            navController.navigate(Route.EVENT_CREATE)
        },
        events = uiState?.events ?: emptyList(),
        deleteEvent = { event ->
            viewModel.deleteEvent(event)
        }
    )
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun EventListContentPreview() {
    val listItems = List(10) {
        Event(
            id = it,
            name = "Event $it name",
            location = "Location $it",
            country = "Country $it",
            capacity = IntRange(1, 15).random()
        )
    }
    MyApplicationTheme {
        EventListContent(
            navigateToEventModify = { },
            onAddClicked = { },
            events = listItems,
            deleteEvent = { }
        )
    }
}

@Composable
fun EventListContent(
    navigateToEventModify: (Event) -> Unit,
    onAddClicked: () -> Unit,
    events: List<Event>,
    deleteEvent: (Event) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            val listItems = List(10) {
//                Event(
//                    id = it,
//                    name = "Event $it name",
//                    location = "Location $it",
//                    country = "Country $it",
//                    capacity = IntRange(1, 15).random()
//                )
//            }

            items(events) {
                EventListItem(
                    event = it,
                    navigateToEventModify = navigateToEventModify,
                    deleteEvent = deleteEvent
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onAddClicked() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Add")
                }
            }
        }
    }
}

@Composable
fun EventList(
    events: List<Event>,
    navigateToEventModify: (Event) -> Unit,
    deleteEvent: (Event) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(events) {
            EventListItem(
                event = it,
                navigateToEventModify = navigateToEventModify,
                deleteEvent = deleteEvent
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun EventListPreview() {
    MyApplicationTheme {
        EventList(
            events = listOf(
                Event(
                    id = 1,
                    name = "Event 1",
                    location = "Location 1",
                    country = "Country 1",
                    capacity = 1
                ),
                Event(
                    id = 2,
                    name = "Event 2",
                    location = "Location 2",
                    country = "Country 2",
                    capacity = 2
                ),
                Event(
                    id = 3,
                    name = "Event 3",
                    location = "Location 3",
                    country = "Country 3",
                    capacity = 3
                ),
                Event(
                    id = 4,
                    name = "Event 4",
                    location = "Location 4",
                    country = "Country 4",
                    capacity = 4
                ),
            ),
            navigateToEventModify = {},
            deleteEvent = {}
        )
    }
}

@Composable
fun EventListItem(
    event: Event,
    navigateToEventModify: (Event) -> Unit,
    deleteEvent: (Event) -> Unit
) {
    // név, helyszín, kapacitás és akciók
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
            .clickable {
                navigateToEventModify(event)
            },
//        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                event.name, style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    "Capacity: " + event.capacity.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(0.1f)
                .clickable {
                    deleteEvent(event)
                },
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun EventListItemPreview() {
    MyApplicationTheme {
        EventListItem(
            event = Event(
                id = 1,
                name = "Event 1",
                location = "Location 1",
                country = "Country 1",
                capacity = 1
            ),
            navigateToEventModify = {},
            deleteEvent = {}
        )
    }
}

