import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import retrofit2.Response
import androidx.navigation.NavController
import okhttp3.ResponseBody
import com.example.ics342.model.Api
import com.example.ics342.model.User
import com.example.ics342.modelview.CreateAccountViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class CreateAccountViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createAccount() should navigate to loginRoute on successful registration`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<User>>()
        val mockUser = User("Maymuna", 1, "mayabdul", "munamuna@gmail.com", true, false)
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns mockUser
        val mockApi = mockk<Api>()
        coEvery { mockApi.register(any(), any()) } returns mockResponse

        // Mock the NavController
        val mockNavController = mockk<NavController>(relaxed = true)

        // Create an instance of the ViewModel
        val viewModel = CreateAccountViewModel(mockApi)

        // Call the function being tested
        viewModel.createAccount("Maymuna", "munamuna@gmail.com", "munamuna", mockNavController)

        // Advance coroutine execution to ensure all tasks complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert that the correct actions were taken
        assertNull(viewModel.error.value)  // No error should be set
        assertEquals(false, viewModel.isLoading.value)
        coVerify { mockApi.register(any(), any()) }
        coVerify { mockNavController.navigate("loginRoute") }
    }

    @Test
    fun `createAccount() should set error on failed registration`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<User>>()
        val mockErrorBody = mockk<ResponseBody>()
        coEvery { mockResponse.isSuccessful } returns false
        coEvery { mockResponse.message() } returns "Registration failed"
        coEvery { mockResponse.errorBody() } returns mockErrorBody
        coEvery { mockErrorBody.string() } returns "Some error message"
        val mockApi = mockk<Api>()
        coEvery { mockApi.register(any(), any()) } returns mockResponse

        // Mock the NavController
        val mockNavController = mockk<NavController>()

        // Create an instance of the ViewModel
        val viewModel = CreateAccountViewModel(mockApi)

        // Call the function being tested
        viewModel.createAccount("Maymuna", "munamuna@.com", "munamuna", mockNavController)

        // Advance coroutine execution to ensure all tasks complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert that the correct actions were taken
        assertEquals("Account creation failed: Registration failed Some error message", viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
        coVerify { mockApi.register(any(), any()) }
        coVerify(exactly = 0) { mockNavController.navigate("loginRoute") }
    }
}
