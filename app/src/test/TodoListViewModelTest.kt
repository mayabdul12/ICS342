import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
class TodoListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val apiService: Api = mockk()
    private val context = mockk<Context>(relaxed = true)
    private val viewModel = TodoListViewModel(apiService, context)

    @Test
    fun `fetchTodos success should update todo list`() = runBlockingTest {
        // Arrange
        val todos = listOf(mockk<Todo>())
        val response = mockk<Response<List<Todo>>>(relaxed = true)
        coEvery { apiService.getTodos(any(), any(), any()) } returns response
        coEvery { response.isSuccessful } returns true
        coEvery { response.body() } returns todos

        // Act
        viewModel.fetchTodos()

        // Assert
        assertEquals(todos, viewModel.todos.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
        coVerify { apiService.getTodos(any(), any(), any()) }
    }

    @Test
    fun `fetchTodos failure should set error`() = runBlockingTest {
        // Arrange
        val response = mockk<Response<List<Todo>>>(relaxed = true)
        coEvery { apiService.getTodos(any(), any(), any()) } returns response
        coEvery { response.isSuccessful } returns false
        coEvery { response.message() } returns "Error"

        // Act
        viewModel.fetchTodos()

        // Assert
        assertTrue(viewModel.todos.value.isEmpty())
        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
        coVerify { apiService.getTodos(any(), any(), any()) }
    }

    @Test
    fun `addTodo success should fetch todos`() = runBlockingTest {
        // Arrange
        val response = mockk<Response<TodoResponse>>(relaxed = true)
        coEvery { apiService.createTodo(any(), any(), any(), any()) } returns response
        coEvery { response.isSuccessful } returns true

        // Act
        viewModel.addTodo("New Task", mockk())

        // Assert
        coVerify { apiService.createTodo(any(), any(), any(), any()) }
    }

    @Test
    fun `addTodo failure should set error`() = runBlockingTest {
        // Arrange
        val response = mockk<Response<TodoResponse>>(relaxed = true)
        coEvery { apiService.createTodo(any(), any(), any(), any()) } returns response
        coEvery { response.isSuccessful } returns false
        coEvery { response.message() } returns "Error"

        // Act
        viewModel.addTodo("New Task", mockk())

        // Assert
        assertNotNull(viewModel.error.value)
        coVerify { apiService.createTodo(any(), any(), any(), any()) }
    }
}
