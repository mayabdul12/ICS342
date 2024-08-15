import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ics342.model.Api
import com.example.ics342.model.EnhancedUser
import com.example.ics342.modelview.LoginViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val apiService: Api = mockk()
    private val context = mockk<Context>(relaxed = true)
    private val viewModel = LoginViewModel(apiService, context)

    @Test
    fun `login success should navigate to todoListRoute`() = runBlockingTest {
        // Arrange
        val response = mockk<Response<EnhancedUser>>(relaxed = true)
        coEvery { apiService.login(any(), any()) } returns response
        coEvery { response.isSuccessful } returns true
        coEvery { response.body() } returns mockk()

        // Act
        viewModel.login("munamuna@gmail.com", "munamuna", mockk())

        // Assert
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
        coVerify { apiService.login(any(), any()) }
    }

    @Test
    fun `login failure should set error`() = runBlockingTest {
        // Arrange
        val response = mockk<Response<EnhancedUser>>(relaxed = true)
        coEvery { apiService.login(any(), any()) } returns response
        coEvery { response.isSuccessful } returns false
        coEvery { response.message() } returns "Error"

        // Act
        viewModel.login("munamuna@gmail.com", "munamuna", mockk())

        // Assert
        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
        coVerify { apiService.login(any(), any()) }
    }
}
