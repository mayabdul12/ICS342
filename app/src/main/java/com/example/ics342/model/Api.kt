package com.example.ics342.model
import retrofit2.Response
import retrofit2.http.*
import androidx.compose.runtime.compositionLocalOf
val Api_variable = compositionLocalOf<Api> {
    error("Service Error occurred. Please try later.")
}

interface Api {
    @POST("/api/users/register")
    suspend fun register(
        @Query("apikey") apiKey: String,
        @Body user: RegistrationRequest
    ): Response<User>

    @POST("/api/users/login")
    suspend fun login(
        @Query("apikey") apiKey: String,
        @Body user: LoginRequest
    ): Response<EnhancedUser>

    @GET("/api/users/{user_id}/todos")
    suspend fun getTodos(
        @Path("user_id") Id: Int,
        @Query("apikey") apiKey: String,
        @Header("Authorization") authHeader: String
    ): Response<List<Todo>>

    @POST("/api/users/{user_id}/todos")
    suspend fun createTodo(
        @Path("user_id") Id: Int,
        @Query("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Body todo: TodoCreationRequest
    ): Response<TodoResponse>

    @PUT("/api/users/{user_id}/todos/{id}")
    suspend fun updateTodo(
        @Path("user_id") Id: Int,
        @Path("id") t_Id: Int,
        @Query("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Body todo: TodoModificationRequest
    ): Response<TodoResponse>
}
