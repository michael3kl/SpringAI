package com.michael.springai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.michael.springai.ui.theme.SpringAITheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    private val apiUrl = "http://10.0.2.2:8081/api/ai/ask"
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestQueue = Volley.newRequestQueue(this)

        setContent {
            SpringAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen(apiUrl = apiUrl, requestQueue = requestQueue)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(apiUrl: String, requestQueue: RequestQueue) {
    var inputMessage by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("Response will appear here...") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = inputMessage,
            onValueChange = { inputMessage = it },
            label = { Text("Enter your message") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (inputMessage.isNotBlank()) {
                    sendMessageCompose(requestQueue, apiUrl, inputMessage) { response ->
                        responseText = response
                    }
                } else {
                    responseText = "Message cannot be empty"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send")
        }

        Text(
            text = responseText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
        )
    }
}

private fun sendMessageCompose(
    requestQueue: RequestQueue,
    apiUrl: String,
    message: String,
    onResponse: (String) -> Unit
) {
    val jsonBody = JSONObject().apply {
        put("message", message)
    }

    val request = object : com.android.volley.toolbox.StringRequest(
        Method.POST, apiUrl,
        { response ->
            Log.d("MainActivityCompose", "Raw Response: $response")

            val parsedResponse = try {
                val json = JSONObject(response)
                when {
                    json.has("answer") -> json.getString("answer")
                    json.has("result") -> json.getString("result")
                    else -> json.toString(2)
                }
            } catch (_: Exception) {
                response
            }

            onResponse(parsedResponse)
        },
        { error ->
            Log.e("MainActivityCompose", "Volley Error: ${error.message}", error)
            var errorMessage = "Error: "
            if (error.networkResponse != null) {
                errorMessage += "Status Code: ${error.networkResponse.statusCode} - "
                try {
                    val errorData = String(error.networkResponse.data, Charsets.UTF_8)
                    errorMessage += errorData
                } catch (_: Exception) { }
            } else if (error.message != null) {
                errorMessage += error.message
            } else {
                errorMessage += "Unknown error"
            }
            onResponse(errorMessage)
        }
    ) {
        override fun getBody(): ByteArray {
            return jsonBody.toString().toByteArray(Charsets.UTF_8)
        }

        override fun getBodyContentType(): String {
            return "application/json; charset=utf-8"
        }

        override fun getRetryPolicy(): RetryPolicy {
            return DefaultRetryPolicy(
                30000, // 30 detik
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        }
    }.apply {
        tag = "MainActivityCompose"
    }

    requestQueue.add(request)
}
