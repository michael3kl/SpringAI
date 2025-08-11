package com.michael.springai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest // Mengganti dengan JsonObjectRequest untuk kemudahan
import com.android.volley.toolbox.Volley
import com.michael.springai.ui.theme.SpringAITheme // Impor tema Compose Anda
import org.json.JSONObject

class MainActivity : ComponentActivity() { // Ubah menjadi ComponentActivity

    // Ganti dengan URL ngrok kamu (misalnya: https://abc123.ngrok.io/api/ai/ask)
    private val apiUrl = "http://10.0.2.2:8081/api/ai/ask"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpringAITheme { // Terapkan tema Compose Anda di sini
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen(apiUrl = apiUrl)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Diperlukan untuk TextField dan Button
@Composable
fun ChatScreen(apiUrl: String) {
    var inputMessage by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("Response will appear here...") }
    val context = LocalContext.current // Untuk membuat Volley request queue

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
                    sendMessageCompose(context, apiUrl, inputMessage) { response ->
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
                .weight(1f) // Agar mengambil sisa ruang
                .padding(vertical = 8.dp)
        )
    }
}

// Fungsi sendMessage yang dimodifikasi untuk digunakan dengan Compose (menggunakan callback)
private fun sendMessageCompose(
    context: android.content.Context,
    apiUrl: String,
    message: String,
    onResponse: (String) -> Unit
) {
    val queue = Volley.newRequestQueue(context)
    val jsonBody = JSONObject()

    try {
        jsonBody.put("message", message)
    } catch (e: org.json.JSONException) {
        Log.e("MainActivityCompose", "Error creating JSON: ${e.message}", e)
        onResponse("Error creating request data.")
        return
    }

    val request = JsonObjectRequest( // Menggunakan JsonObjectRequest
        Request.Method.POST, apiUrl, jsonBody,
        { response ->
            Log.d("MainActivityCompose", "Response: $response")
            // Asumsikan respons server adalah JSON dengan field yang ingin ditampilkan,
            // atau langsung string. Jika JSON, Anda perlu mem-parse-nya.
            // Untuk contoh ini, kita anggap responsnya langsung string atau kita ambil toString().
            onResponse(response.toString())
        },
        { error ->
            Log.e("MainActivityCompose", "Volley Error: ${error.message}", error)
            var errorMessage = "Error: "
            if (error.networkResponse != null) {
                errorMessage += "Status Code: ${error.networkResponse.statusCode} - "
                try {
                    val errorData = String(error.networkResponse.data, Charsets.UTF_8)
                    errorMessage += errorData
                } catch (e: Exception) {
                    // Abaikan jika tidak bisa membaca data error
                }
            } else if (error.message != null) {
                errorMessage += error.message
            } else {
                errorMessage += "Unknown error"
            }
            onResponse(errorMessage)
        }
    )
    // Tidak perlu override getBody() dan getBodyContentType() untuk JsonObjectRequest
    // jika jsonBody sudah disediakan di konstruktor.

    queue.add(request)
}
