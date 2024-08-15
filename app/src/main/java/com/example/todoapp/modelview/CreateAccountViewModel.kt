package com.example.todoapp.modelview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todoapp.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class CreateAccountViewModel(private val apiService: Api) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    fun createAccount(name: String, email: String, password: String, navController: NavController) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.register(apikey, RegistrationRequest(name, email, password))
                if (response.isSuccessful && response.body() != null) {
                    _error.value = null
                    navController.navigate("loginRoute")
                } else {
                    _error.value = "Account creation failed: ${response.message()} ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _error.value = "Network or server error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
