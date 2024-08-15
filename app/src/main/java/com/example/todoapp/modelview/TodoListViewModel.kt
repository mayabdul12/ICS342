package com.example.todoapp.modelview
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todoapp.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class TodoListViewModel(private val apiService: Api, context: Context) : ViewModel() {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("userdetail", Context.MODE_PRIVATE)

    fun fetchTodos() {
        val userId = getUserId()
        val token = getUserToken()
        if (userId == -1 || token == null) {
            _error.value = "User ID or token not found. Please log in again."
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getTodos(userId, apikey, "Bearer $token")
                if (response.isSuccessful) {
                    _todos.value = response.body() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "Failed to fetch todos: ${response.message()} ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _error.value = "Network or server error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTodo(description: String, navController: NavController) {
        val userId = getUserId()
        val token = getUserToken()
        if (userId == -1 || token == null) {
            _error.value = "User ID or token not found. Please log in again."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val request = TodoCreationRequest(description, completed = false)
                val response = apiService.createTodo(userId, apikey, "Bearer $token", request)
                if (response.isSuccessful) {
                    fetchTodos()
                    navController.navigate("todoListRoute")
                    _error.value = null
                } else {
                    _error.value = "Failed to create todo: ${response.message()} ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _error.value = "Network or server error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateTodo(id: Int, description: String, isCompleted: Boolean) {
        val userId = getUserId()
        val token = getUserToken()
        if (userId == -1 || token == null) {
            _error.value = "User ID or token not found. Please log in again."
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val request = TodoModificationRequest(id, description, isCompleted)
                val response = apiService.updateTodo(userId, id, apikey, "Bearer $token", request)
                if (response.isSuccessful) {
                    fetchTodos()
                    _error.value = null
                } else {
                    _error.value = "Failed to update todo: ${response.message()} ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _error.value = "Network or server error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getUserId(): Int = sharedPreferences.getInt("id", -1)
    private fun getUserToken(): String? = sharedPreferences.getString("token", null)
}
