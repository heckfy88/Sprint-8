package ru.sberschool.hystrix

import feign.Request
import feign.httpclient.ApacheHttpClient
import feign.hystrix.HystrixFeign
import feign.jackson.JacksonDecoder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockserver.client.server.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class SlowlyApiTest {
    private val client: SlowlyApi = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder())
        // для удобства тестирования задаем таймауты на 1 секунду
        .options(Request.Options(1, TimeUnit.SECONDS, 1, TimeUnit.SECONDS, true))
        .target(SlowlyApi::class.java, "http://127.0.0.1:18080", FallbackSlowlyApi())
    lateinit var mockServer: ClientAndServer

    @BeforeEach
    fun setup() {
        // запускаем мок сервер для тестирования клиента
        mockServer = ClientAndServer.startClientAndServer(18080)
    }

    @AfterEach
    fun shutdown() {
        mockServer.stop()
    }


    @Test
    fun fallbackScenarioTest() {
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                HttpRequest
                    .request()
                    .withMethod("GET")
                    .withPath("/")
            )
            .respond(
                HttpResponse
                    .response()
                    .withStatusCode(400)
                    .withDelay(TimeUnit.SECONDS, 30)
                    .withBody("{  \"name\": \"cheri\" }")
            )
        assertEquals("fallback", client.getBerry().name)
    }

    @Test
    fun okScenarioTest() {
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/berry/cheri")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withBody("{  \"name\": \"cheri\" }")
            )
        assertEquals("cheri", client.getBerry().name)
    }
}