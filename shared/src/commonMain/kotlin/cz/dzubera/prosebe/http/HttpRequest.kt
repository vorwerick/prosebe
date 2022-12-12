package cz.dzubera.prosebe.http

import com.soywiz.krypto.md5
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.*

class HttpRequest {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }

    companion object {
        const val HOST = ""
    }

    suspend fun loginUser(name: String, password: String): HttpResponse {
        return  client.get("https://pro-sebe-default-rtdb.europe-west1.firebasedatabase.app/users.json")
    }

    suspend fun sendFeedback(email: String, note: String): HttpResponse {
        return client.submitForm("https://mailthis.to/ales@dzubera.cz") {
            parameter("text",note)
            parameter("email",email)
        }
    }


    suspend fun registerUser(email: String, name: String, password: String): HttpResponse {
        return client.post("https://pro-sebe-default-rtdb.europe-west1.firebasedatabase.app/users.json") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(
                    buildJsonObject {
                        put("name", name)
                        put("email", email)
                        put("password", password.toByteArray().md5().base64)
                    }
                )
            }
    }
}

class Response(val status: Int, val data: String) {


}