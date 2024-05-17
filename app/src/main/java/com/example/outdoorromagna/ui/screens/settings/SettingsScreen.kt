package com.example.outdoorromagna.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.screens.sideBarMenu.SideBarMenu
import com.example.outdoorromagna.ui.screens.sideBarMenu.getMyDrawerState
import com.example.outdoorromagna.ui.theme.OutdoorRomagnaTheme
import com.example.outdoorromagna.ui.theme.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    /*state: SettingsState,
    onUsernameChanged: (String) -> Unit,*/
    settingsVm : SettingsViewModel,
    navController: NavHostController,
    user: User
) {/*
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(12.dp).fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.username,
            onValueChange = onUsernameChanged,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(36.dp))
        Text(
            text = state.username,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyLarge
        )
    }*/
    val scope = rememberCoroutineScope()
    val myScaffold: @Composable () -> Unit = {
        val theme by settingsVm.theme.collectAsState(initial = "")
        OutdoorRomagnaTheme(darkTheme = theme == "Dark") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        navController = navController,
                        currentRoute = OutdoorRomagnaRoute.Profile.title,
                        drawerState = getMyDrawerState(),
                        scope = scope
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        navController = navController,
                        user = user
                    )
                }
            ) { paddingValues ->
                Column(
                    Modifier
                        .selectableGroup()
                        .padding(paddingValues)
                        .padding(10.dp)
                        .fillMaxSize(),
                ) {
                    Text(
                        text = "Tema dell'app",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(3.dp)
                    )

                    Spacer(modifier = Modifier.size(3.dp))

                    val radioOptions = listOf("Light", "Dark")
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (text == theme),
                                    onClick = {
                                        settingsVm.saveTheme(text)
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == theme),
                                onClick = null, // null recommended for accessibility with screenreaders
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }

        }
    }
    SideBarMenu(
        myScaffold = myScaffold,
        navController
    )
}
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val theme by settingsViewModel.theme.collectAsState(initial = "")
    Mobile_projectTheme(darkTheme = theme == "Dark") {

        Scaffold { paddingValues ->
            Column(
                Modifier
                    .selectableGroup()
                    .padding(paddingValues)
                    .padding(10.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "Tema dell'app",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(3.dp)
                )

                Spacer(modifier = Modifier.size(3.dp))

                val radioOptions = listOf("Light", "Dark")
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == theme),
                                onClick = {
                                    settingsViewModel.saveTheme(text)
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == theme),
                            onClick = null, // null recommended for accessibility with screenreaders
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}*/

