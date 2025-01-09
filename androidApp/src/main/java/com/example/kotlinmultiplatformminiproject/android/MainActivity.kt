package com.example.kotlinmultiplatformminiproject.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinmultiplatformminiproject.android.ui.eventcreate.EventCreateScreen
import com.example.kotlinmultiplatformminiproject.android.ui.eventcreate.EventCreateViewModel
import com.example.kotlinmultiplatformminiproject.android.ui.eventlist.EventListScreen
import com.example.kotlinmultiplatformminiproject.android.ui.eventlist.EventListViewModel
import com.example.kotlinmultiplatformminiproject.android.ui.eventmodify.EventModifyScreen
import com.example.kotlinmultiplatformminiproject.android.ui.eventmodify.EventModifyViewModel
import com.example.kotlinmultiplatformminiproject.android.ui.login.LoginScreen
import com.example.kotlinmultiplatformminiproject.android.ui.login.LoginViewModel
import com.example.kotlinmultiplatformminiproject.android.ui.manager.EventManager
import com.example.kotlinmultiplatformminiproject.database.DriverFactory
import com.example.kotlinmultiplatformminiproject.database.createDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val eventManager = EventManager()
                val database = createDatabase(DriverFactory(this))

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.LOGIN
                    ) {
                        composable(route = Route.LOGIN) {
                            val loginViewModel = LoginViewModel()
                            LoginScreen(
                                navController = navController,
                                viewModel = loginViewModel
                            )
                        }
                        composable(route = Route.EVENT_LIST) {
                            val eventListViewModel = EventListViewModel(
                                eventManager = eventManager,
                                db = database
                            )
                            EventListScreen(
                                navController = navController,
                                viewModel = eventListViewModel
                            )
                        }
                        composable(route = Route.EVENT_CREATE) {
                            val eventCreateViewModel = EventCreateViewModel(
                                eventManager = eventManager,
                                db = database
                            )

                            EventCreateScreen(
                                navController = navController,
                                viewModel = eventCreateViewModel
                            )
                        }
                        composable(
                            route = "Route.EVENT_MODIFY/{eventId}",
                            arguments = listOf(
                                navArgument(name = "eventId") {
                                    type = NavType.IntType
                               }
                            )
                        ) {
                            backStackEntry ->
                            val eventId = backStackEntry.arguments?.getInt("eventId")

                            val eventModifyViewModel = EventModifyViewModel(
                                eventId = eventId!!,
                                eventManager = eventManager,
                                db = database
                            )

                            EventModifyScreen(
                                navController = navController,
                                viewModel = eventModifyViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

object Route {
    const val LOGIN = "login"
    const val EVENT_LIST = "event_list"
    const val EVENT_CREATE = "event_create"
    const val EVENT_MODIFY = "event_modify"
}

@Composable
fun GreetingView(text: String, modifier: Modifier) {
    Box(modifier = modifier) {
        Text(text = text)
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!", Modifier)
    }
}
