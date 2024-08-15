package com.example.todoapp
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todoapp.model.*
import com.example.todoapp.modelview.*
import com.example.todoapp.view.*

@Composable

fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "loginRoute") {
        composable("loginRoute") {
            val loginViewModel = LoginViewModel(Api_variable.current, LocalContext.current)
            LoginScreen(viewModel = loginViewModel, navController = navController)
        }
        composable("createAccountRoute") {
            val createAccountViewModel = CreateAccountViewModel(Api_variable.current)
            CreateAccountScreen(viewModel = createAccountViewModel, navController = navController)
        }
        composable("todoListRoute") {
            val todoListViewModel = TodoListViewModel(Api_variable.current, LocalContext.current)
            TodoListScreen(viewModel = todoListViewModel, navController = navController)
        }
        composable("addTodoRoute") {
            val todoListViewModel = TodoListViewModel(Api_variable.current, LocalContext.current)
            AddTodoScreen(viewModel = todoListViewModel, navController = navController)
        }
    }
}
