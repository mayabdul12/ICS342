import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class CreateAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val apiService: Api = mockk()
    private val viewModel = CreateAccountViewModel(apiService)

    @Test
    fun `createAccount success should navigate to loginRoute`() = runBlockingTest {
        // Arrange
        val response = mockk<Response<User>>(relaxed = true)
        coEvery { apiService.register(any(), any()) } returns response
        coEvery { response.isSuccessful } returns true
        coEvery { response.body() } returns mockk()

        // Act
        viewModel.createAccount("maymuna", "munamuna@gmail.com.com", "munamuna", mockk())

        // Assert
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
        coVerify { apiService.register(any(), any()) }
    }

    @Test
    fun `createAccount failure should set error`() = runBlockingTest {
        // Arrange
        val response = mockk<Response<User>>(relaxed = true)
        coEvery { apiService.register(any(), any()) } returns response
        coEvery { response.isSuccessful } returns false
        coEvery { response.message() } returns "Error"

        // Act
        viewModel.createAccount("maymuna", "munamuna@gmail.com.com", "munamuna", mockk())

        // Assert
        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
        coVerify { apiService.register(any(), any()) }
    }
}
