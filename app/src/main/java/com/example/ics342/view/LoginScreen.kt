package com.example.ics342.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ics342.modelview.LoginViewModel
import com.example.todoapp.R

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.todo_login),
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label =  { Text(stringResource(R.string.email_address))},
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { viewModel.login(email, password, navController) }),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(email, password, navController) },
            modifier = Modifier.fillMaxWidth(),
            enabled = email.isNotEmpty() && password.isNotEmpty() && !isLoading,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(stringResource(R.string.login))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate("createAccountRoute") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.create_an_account))
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        error?.let {
            Text(text = it, color = MaterialTheme.colors.error, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
