package com.example.ics342.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ics342.modelview.TodoListViewModel
import com.example.todoapp.R

@Composable
fun AddTodoScreen(navController: NavController, viewModel: TodoListViewModel) {
    var description by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.task_description)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (description.isNotEmpty()) {
                        viewModel.addTodo(description, navController)
                        navController.popBackStack()
                    }
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (description.isNotEmpty()) {
                            viewModel.addTodo(description, navController)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = description.isNotEmpty() && !isLoading
                ) {
                    Text(stringResource(R.string.add_task))
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { description = "" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.clear))
                }
            }

            if (isLoading) {
                CircularProgressIndicator()
            }

            error?.let {
                Text(text = it, color = colors.error, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}
