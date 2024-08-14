package com.example.ics342.modelview
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ics342.model.*
import com.example.ics342.model.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class LoginViewModel(private val apiService: Api, context: Context) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("userdetail", Context.MODE_PRIVATE)

    fun login(email: String, password: String, navController: NavController) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.login(apikey, LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    with(sharedPreferences.edit()) {
                        putString("token", user.authToken)
                        putInt("id", user.userId)
                        apply()
                    }
                    navController.navigate("todoListRoute")
                    _error.value = null
                } else {
                    _error.value = "Login failed: ${response.message()} ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _error.value = "Network or server error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}