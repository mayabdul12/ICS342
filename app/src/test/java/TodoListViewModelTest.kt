import com.example.ics342.model.Api
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

import retrofit2.Response
import com.example.ics342.model.Todo
import com.example.ics342.model.TodoResponse
import com.example.ics342.modelview.TodoListViewModel
import org.junit.Assert.assertEquals
import org.junit.Test


class TodoListViewModelTest {

    @Test
    fun `fetchTodos() should update on successful fetch`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<List<Todo>>>()
        val todoItems = listOf(Todo(1, 1, "Test Todo", 0, "John Doe", null))
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns todoItems

        val mockApi = mockk<Api>()
        coEvery { mockApi.getTodos(any(), any(), any()) } returns mockResponse

        // Create an instance of the ViewModel
        val viewModel = TodoListViewModel(mockApi, mockk(relaxed = true))

        // Call the function being tested
        viewModel.fetchTodos()

        // Assert that the correct actions were taken
        assertEquals(todoItems, viewModel.todos.value)
        coVerify { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `fetchTodos() should not update on failed fetch`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<List<Todo>>>()
        coEvery { mockResponse.isSuccessful } returns false

        val mockApi = mockk<Api>()
        coEvery { mockApi.getTodos(any(), any(), any()) } returns mockResponse

        // Create an instance of the ViewModel
        val viewModel = TodoListViewModel(mockApi, mockk(relaxed = true))

        // Call the function being tested
        viewModel.fetchTodos()

        // Assert that the correct actions were taken
        assertEquals(emptyList<Todo>(), viewModel.todos.value)
        coVerify { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `createTodo() should fetchTodos after successful creation`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<TodoResponse>>()
        val mockFetchResponse = mockk<Response<List<Todo>>>()
        val todoItems = listOf(Todo(1, 1, "Test Todo", 0, "John Doe", null))
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockFetchResponse.isSuccessful } returns true
        coEvery { mockFetchResponse.body() } returns todoItems

        val mockApi = mockk<Api>()
        coEvery { mockApi.createTodo(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockApi.getTodos(any(), any(), any()) } returns mockFetchResponse

        // Create an instance of the ViewModel
        val viewModel = TodoListViewModel(mockApi, mockk(relaxed = true))

        // Call the function being tested
        viewModel.createTodo("Test Todo")

        // Assert that the correct actions were taken
        assertEquals(todoItems, viewModel.todos.value)
        coVerify { mockApi.createTodo(any(), any(), any(), any()) }
        coVerify { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `createTodo() should not fetchTodos on failed creation`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<TodoResponse>>()
        coEvery { mockResponse.isSuccessful } returns false

        val mockApi = mockk<Api>()
        coEvery { mockApi.createTodo(any(), any(), any(), any()) } returns mockResponse

        // Create an instance of the ViewModel
        val viewModel = TodoListViewModel(mockApi, mockk(relaxed = true))

        // Call the function being tested
        viewModel.createTodo("Test Todo")

        // Assert that the correct actions were taken
        assertEquals(emptyList<Todo>(), viewModel.todos.value)
        coVerify { mockApi.createTodo(any(), any(), any(), any()) }
        coVerify(exactly = 0) { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `updateTodo() should fetchTodos after successful update`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<TodoResponse>>()
        val mockFetchResponse = mockk<Response<List<Todo>>>()
        val todoItems = listOf(Todo(1, 1, "Updated Todo", 1, "John Doe", null))
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockFetchResponse.isSuccessful } returns true
        coEvery { mockFetchResponse.body() } returns todoItems

        val mockApi = mockk<Api>()
        coEvery { mockApi.updateTodo(any(), any(), any(), any(), any()) } returns mockResponse
        coEvery { mockApi.getTodos(any(), any(), any()) } returns mockFetchResponse

        // Create an instance of the ViewModel
        val viewModel = TodoListViewModel(mockApi, mockk(relaxed = true))

        // Call the function being tested
        viewModel.updateTodo(1, "Updated Todo", true)

        // Assert that the correct actions were taken
        assertEquals(todoItems, viewModel.todos.value)
        coVerify { mockApi.updateTodo(any(), any(), any(), any(), any()) }
        coVerify { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `updateTodo() should not fetchTodos on failed update`() = runTest {
        // Mock the response and the Api
        val mockResponse = mockk<Response<TodoResponse>>()
        coEvery { mockResponse.isSuccessful } returns false

        val mockApi = mockk<Api>()
        coEvery { mockApi.updateTodo(any(), any(), any(), any(), any()) } returns mockResponse

        // Create an instance of the ViewModel
        val viewModel = TodoListViewModel(mockApi, mockk(relaxed = true))

        // Call the function being tested
        viewModel.updateTodo(1, "Updated Todo", true)

        // Assert that the correct actions were taken
        assertEquals(emptyList<Todo>(), viewModel.todos.value)
        coVerify { mockApi.updateTodo(any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) { mockApi.getTodos(any(), any(), any()) }
    }
}

private fun TodoListViewModel.createTodo(s: String) {

}
