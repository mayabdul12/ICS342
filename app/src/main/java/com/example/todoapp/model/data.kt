package com.example.todoapp.model
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
public val apikey="3f3884d5-260e-4265-86fe-197b597d95db"
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "token") val authToken: String,
    @Json(name = "id") val userId: Int,
    @Json(name = "name") val userName: String,
    @Json(name = "email") val userEmail: String,
    @Json(name = "enabled") val isActive: Boolean,
    @Json(name = "admin") val isAdmin: Boolean
)
@JsonClass(generateAdapter = true)
data class EnhancedUser(
    @Json(name = "token") val authToken: String,
    @Json(name = "id") val userId: Int,
    @Json(name = "name") val userName: String,
    @Json(name = "email") val userEmail: String,
    @Json(name = "enabled") val isEnabledInt: Int,
    @Json(name = "admin") val isAdminInt: Int
) {
    val isEnabled: Boolean
        get() = isEnabledInt == 1
    val isAdmin: Boolean
        get() = isAdminInt == 1
}
@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)
@JsonClass(generateAdapter = true)
data class RegistrationRequest(
    @Json(name = "name") val userName: String,
    @Json(name = "email") val userEmail: String,
    @Json(name = "password") val userPassword: String
)
@JsonClass(generateAdapter = true)
data class TodoModificationRequest(
    val id: Int,
    val description: String,
    @Json(name = "completed") val isFinished: Boolean,
    val metadata: Map<String, Any> = emptyMap()
)
@JsonClass(generateAdapter = true)
data class TodoCreationRequest(
    val description: String,
    val completed: Boolean,
    val metadata: Map<String, Any> = emptyMap()
)
@JsonClass(generateAdapter = true)
data class TodoResponse(
    @Json(name = "id") val itemId: Int,
    @Json(name = "description") val content: String,
    @Json(name = "completed") val isCompleted: Boolean,
    @Json(name = "meta") val metadata: Map<String, Any>?
)
@JsonClass(generateAdapter = true)
data class TodoUpdateResponse(
    @Json(name = "id") val itemId: Int,
    @Json(name = "description") val content: String,
    @Json(name = "completed") val isCompleted: Boolean
)
@JsonClass(generateAdapter = true)
data class Todo(
    @Json(name = "id") val itemId: Int,
    @Json(name = "user_id") val userIdentifier: Int,
    @Json(name = "description") val content: String,
    @Json(name = "completed") val completionStatus: Int,
    @Json(name = "author") val creator: String,
    @Json(name = "meta") val metadata: Map<String, Any>?
)