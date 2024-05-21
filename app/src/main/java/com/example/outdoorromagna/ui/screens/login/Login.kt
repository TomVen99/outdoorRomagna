package com.example.outdoorromagna.ui.screens.login

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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


@Composable
fun Login(
    state: LoginState,
    actions: LoginActions,
    onSubmit: () -> Unit,
    navController: NavHostController,
    viewModel: UsersViewModel,
    sharedPreferences: SharedPreferences,
) {
    val signinResult by viewModel.loginResult.observeAsState()
    val signinLog by viewModel.loginLog.observeAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentHeight(Alignment.CenterVertically))
    {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RectangleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logooudoorromagnarectangle),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            OutlinedTextField(
                value = state.username,
                onValueChange = actions::setUsername,
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            var pwd by remember { mutableStateOf(state.password) }

            PasswordTextField(
                password = pwd,
                onPasswordChange = { newPassword -> pwd = newPassword },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = "Password",
                actions
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (signinResult == false) {
                    Text(signinLog.toString(), color = Color.Red)
                } else if (signinResult == true) {
                    navController.navigate(OutdoorRomagnaRoute.Home.buildWithoutPosition(state.username))
                } else if (signinResult == null) {
                    Spacer(Modifier.size(15.dp))
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                onClick = {
                    if (!state.canSubmit) return@Button
                    onSubmit()
                    val edit = sharedPreferences.edit()
                    edit.putBoolean("isUserLogged", true)
                    edit.putString("username", state.username)
                    edit.apply()
                    //sharedPreferences.getString("username", "")?.let { Log.d("TAG", "dentro Login " + it) }
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Accedi")
            }
            Text(text = "Oppure")
            TextButton(
                onClick = {
                    navController.navigate(OutdoorRomagnaRoute.Signin.route)
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Registrati ora")
            }
        }
    }
}
@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    actions: LoginActions
) {
    actions.setPassword(password)
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = modifier,
        label = { Text(label) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        )
    )
}

fun saveLoggedUser(username: String, sharedPreferences: SharedPreferences, context: Context) {
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    editor.putBoolean(context.getString(R.string.username_shared_pref), true)
    editor.putString(context.getString(R.string.username_shared_pref), username)
    editor.apply()
}