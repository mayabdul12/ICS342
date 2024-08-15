package com.example.todoapp.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoapp.R
import com.example.todoapp.model.Todo
import com.example.todoapp.modelview.TodoListViewModel
@Composable
fun TodoListScreen(navController: NavController, viewModel: TodoListViewModel) {
    val todoItems by viewModel.todos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    viewModel.fetchTodos()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.todo_tasks)) },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addTodoRoute") },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_new_task))
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)) {
            if (isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(todoItems) { todo ->
                        TodoItemView(todo, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItemView(todo: Todo, viewModel: TodoListViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.completionStatus==1,
                onCheckedChange = { isChecked ->
                    viewModel.updateTodo(todo.itemId,todo.content ,isChecked)
                }
            )
            Text(
                text = todo.content,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
