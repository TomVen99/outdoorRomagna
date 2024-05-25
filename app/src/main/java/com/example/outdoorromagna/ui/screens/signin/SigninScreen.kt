package com.example.outdoorromagna.ui.screens.signin

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoneOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.UsersViewModel

@Composable
fun SigninScreen(
    state: SigninState,
    actions: SigninActions,
    onSubmit: (User) -> Unit,
    navController: NavHostController,
    viewModel: UsersViewModel
    ) {
        val signinResult by viewModel.signinResult.observeAsState()
        val signinLog by viewModel.signinLog.observeAsState()

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
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )

                OutlinedTextField(
                    value = state.username,
                    onValueChange = actions::setUsername,
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 4.dp)
                )
                var pwd by remember { mutableStateOf(state.password) }
                PasswordTextField(
                    password = pwd,
                    onPasswordChange = { newPassword -> pwd = newPassword},
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 4.dp),
                    label = "Password",
                    actions)

                OutlinedTextField(
                    value = state.name,
                    onValueChange = actions::setFirstName,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 4.dp)
                )
                OutlinedTextField(
                    value = state.surname,
                    onValueChange = actions::setSurname,
                    label = { Text("Surname") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 4.dp)
                )
                OutlinedTextField(
                    value = state.mail,
                    onValueChange = actions::setMail,
                    label = { Text("Mail") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(Modifier.size(10.dp))
                Button(
                    onClick = {
                        if (!state.canSubmit) return@Button
                        val salt = viewModel.generateSalt()
                        val password = viewModel.hashPassword(state.password, salt)
                        onSubmit(
                            User(
                            username = state.username,
                            password = password,
                                salt = salt,
                                urlProfilePicture = "",
                                name = state.name,
                                surname = state.surname,
                                mail = state.mail
                            ))
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.align(Alignment.End).padding(end = 15.dp, bottom = 15.dp)
                ) {
                    Icon(
                        Icons.Outlined.DoneOutline,
                        contentDescription = "done icon",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Registrati")
                }
                if (signinResult == false) {
                    Text(signinLog.toString(), color = Color.Red)
                } else if (signinResult == true) {
                    navController.navigate(OutdoorRomagnaRoute.Login.route)
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
    actions: SigninActions
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