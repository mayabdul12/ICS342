import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import retrofit2.Response
import com.example.ics342.model.User
import com.example.todoapp.api.Api
import com.example.todoapp.viewmodel.LoginViewModel

class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `loginUser() should assign User object and save token on successful login`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<User>>()
        val mockUser = User("Maymuna", 1, "Maymuna", "munamuna@gmail.com", true, false)
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns mockUser

        val mockApi = mockk<Api>()
        coEvery { mockApi.login(any(), any()) } returns mockResponse

        // Mock the Context and DataStore
        val mockContext = mockk<Context>(relaxed = true)
        val mockDataStore = mockk<Preferences>(relaxed = true)
        val preferencesFlow = MutableStateFlow(mockDataStore)
        coEvery { mockContext.dataStore.data } returns preferencesFlow

        // Create an instance of the ViewModel
        val viewModel = LoginViewModel(mockApi, mockContext)

        // Call the function being tested
        viewModel.loginUser("john@example.com", "password123")

        // Assert that the correct actions were taken
        assertEquals(mockUser, viewModel.user.value)
        coVerify { mockApi.login(any(), any()) }
        coVerify { mockContext.dataStore.edit(any()) }
    }

    @Test
    fun `loginUser() should not assign User object on failed login`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<User>>()
        coEvery { mockResponse.isSuccessful } returns false

        val mockApi = mockk<Api>()
        coEvery { mockApi.login(any(), any()) } returns mockResponse

        // Mock the Context
        val mockContext = mockk<Context>(relaxed = true)

        // Create an instance of the ViewModel
        val viewModel = LoginViewModel(mockApi, mockContext)

        // Call the function being tested
        viewModel.loginUser("john@example.com", "password123")

        // Assert that the correct actions were taken
        assertNull(viewModel.user.value)
        coVerify { mockApi.login(any(), any()) }
    }
}
