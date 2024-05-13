package com.example.outdoorromagna.ui.screens.login

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DoneOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.database.Place
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.composables.ImageWithPlaceholder
import com.example.outdoorromagna.ui.composables.Size

/*@Composable
fun Login(/*state: LoginState, */navController: NavHostController) {
    
    Log.d("TAG", "Login")
    OutlinedTextField(
        value = state.username,
        onValueChange = actions::setUsername,
        label = { Text("Username") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = state.password,
        onValueChange = actions::setPassword,
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.size(24.dp))
    Button(
        onClick = {
            if (!state.canSubmit) return@Button
            onSubmit()
        },
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Icon(
            Icons.Outlined.DoneOutline,
            contentDescription = "done icon",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("log-in")
    }
    Spacer(Modifier.size(40.dp))
    Button(
        onClick = {
            navController.navigate(TravelDiaryRoute.SignIn.route)
        },
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        modifier = Modifier.align(Alignment.End),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Text("sign-in")
    }
    /*if (addUserResult == false) {
        Text(addUserLog.toString(), color = Color.Red)
    } else if (addUserResult == true) {
        navController.navigate(TravelDiaryRoute.HomeMap.buildWithoutPosition(state.username))
    }*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelItem(item: Place, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageUri = Uri.parse(item.imageUri)
            ImageWithPlaceholder(imageUri, Size.Sm)
            Spacer(Modifier.size(8.dp))
            Text(
                item.name,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NoItemsPlaceholder(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            Icons.Outlined.LocationOn, "Location icon",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(48.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            "No items",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Tap the + button to add a new place.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
*/
@Composable
fun Login(
    state: LoginState,
    actions: LoginActions,
    //onSubmit: () -> Unit,
    navController: NavHostController,
    viewModel: UsersViewModel
) {
    /*val addUserResult by viewModel.loginResult.observeAsState()
    val addUserLog by viewModel.loginLog.observeAsState()*/

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.outdoorromagna), // Sostituisci "R.drawable.your_image_resource" con il tuo ID di risorsa immagine
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp) // Spazio inferiore tra l'immagine e il modulo di accesso
        )
        OutlinedTextField(
            value = state.username,
            onValueChange = actions::setUsername,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        /*PasswordTextField(
            passwordState = state.password,
            modifier = Modifier.fillMaxWidth(),
            label = "Password"
        )*/

        OutlinedTextField(
            value = state.password,
            onValueChange = actions::setPassword,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.size(24.dp))
        Button(
            onClick = {
                      Log.d("TAG", "bottone")
                    /*if (!state.canSubmit) return@Button
                    onSubmit()*/
            },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(
                Icons.Outlined.DoneOutline,
                contentDescription = "done icon",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Login")
        }
        Spacer(Modifier.size(40.dp))
        Button(
            onClick = {
                navController.navigate(OutdoorRomagnaRoute.Signin.route)
            },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text("sign-in")
        }
        /*if (addUserResult == false) {
            Text(addUserLog.toString(), color = Color.Red)
        } else if (addUserResult == true) {
            navController.navigate(TravelDiaryRoute.HomeMap.buildWithoutPosition(state.username))
        }*/
    }
}
@Composable
fun PasswordTextField(
    passwordState: MutableState<String>,
    modifier: Modifier = Modifier,
    label: String = "Password"
) {
    TextField(
        value = passwordState.value,
        onValueChange = { newValue -> passwordState.value = newValue },
        modifier = modifier,
        label = { Text(label) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        ),
        /*colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        )*/
    )
}