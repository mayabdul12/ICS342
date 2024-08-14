package com.example.ics342
import com.example.ics342.model.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val nav = rememberNavController()
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://todos.simpleapi.dev")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
            val api = retrofit.create(Api::class.java)
            CompositionLocalProvider(Api_variable provides api) {
                Surface(color = MaterialTheme.colors.background) {
                    AppNavigation(nav)
                }
            }
        }
    }
}