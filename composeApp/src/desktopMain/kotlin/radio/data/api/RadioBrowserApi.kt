package radio.data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.timeout
import org.slf4j.LoggerFactory
import java.net.URL
import java.net.UnknownHostException
import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLHandshakeException
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.serialization.Serializable
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestRetry.Configuration
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration.Companion.seconds

class RadioBrowserApi {
    private val logger = LoggerFactory.getLogger(RadioBrowserApi::class.java)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 10000
        }
        install(DefaultRequest) {
            header("User-Agent", "RadioBrowser-Windows/1.0")
        }
        install(HttpRequestRetry) {
            maxRetries = 3
            retryOnServerErrors()
            retryOnException()
        }
    }

    data class MirrorInfo(
        val url: String,
        val responseTime: Long,
        val isAvailable: Boolean,
        val error: String? = null,
        val statusCode: Int? = null
    )

    @Serializable
    data class ServerStatus(
        val url: String,
        val ip: String,
        val name: String? = null,
        val isAvailable: Boolean,
        val responseTime: Long,
        val error: String? = null,
        val statusCode: Int? = null,
        val lastChecked: Long = System.currentTimeMillis()
    )

    private val baseUrl = AtomicReference<String>("")
    private var availableMirrors: List<MirrorInfo> = emptyList()
    private val serverStatuses = mutableListOf<ServerStatus>()
    private val serverStatusesLock = Any()

    init {
        // Initialize with a default mirror
        baseUrl.set("http://all.api.radio-browser.info")
    }

    private suspend fun testServerConnection(host: String, port: Int = 80): Boolean {
        return try {
            withTimeout(5.seconds) {
                val socket = Socket()
                val address = InetAddress.getByName(host)
                socket.connect(java.net.InetSocketAddress(address, port), 5000)
                socket.close()
                true
            }
        } catch (e: Exception) {
            logger.debug("Connection test failed for $host: ${e.message}")
            false
        }
    }

    private suspend fun getAvailableServers(): List<String> {
        val mirrors = listOf(
            "all.api.radio-browser.info",
            "de1.api.radio-browser.info",
            "nl1.api.radio-browser.info",
            "fr1.api.radio-browser.info",
            "uk1.api.radio-browser.info",
            "us1.api.radio-browser.info",
            "at1.api.radio-browser.info",
            "ch1.api.radio-browser.info",
            "es1.api.radio-browser.info",
            "pl1.api.radio-browser.info",
            "ie1.api.radio-browser.info",
            "br1.api.radio-browser.info",
            "fi1.api.radio-browser.info",
            "ru1.api.radio-browser.info",
            "ua1.api.radio-browser.info",
            "cz1.api.radio-browser.info",
            "sk1.api.radio-browser.info",
            "jp1.api.radio-browser.info",
            "kr1.api.radio-browser.info",
            "cn1.api.radio-browser.info",
            "162.55.180.156"  // CleanIp
        )

        val availableMirrors = mutableListOf<String>()
        val jobs = mutableListOf<Job>()

        mirrors.forEach { mirror ->
            val job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val ip = java.net.InetAddress.getByName(mirror).hostAddress
                    if (testServerConnection(ip)) {
                        availableMirrors.add(mirror)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            jobs.add(job)
        }

        jobs.joinAll()
        return availableMirrors
    }

    private suspend fun findFastestMirror(mirrors: List<String>): String? {
        val jobs = mirrors.map { mirror ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val startTime = System.currentTimeMillis()
                    val response = client.get("http://$mirror/json/stations/search?limit=1")
                    val endTime = System.currentTimeMillis()
                    val responseTime = endTime - startTime

                    synchronized(serverStatusesLock) {
                        serverStatuses.add(
                            ServerStatus(
                                url = "http://$mirror",
                                ip = java.net.InetAddress.getByName(mirror).hostAddress,
                                isAvailable = response.status.isSuccess(),
                                responseTime = responseTime,
                                error = null,
                                lastChecked = System.currentTimeMillis()
                            )
                        )
                    }
                } catch (e: Exception) {
                    synchronized(serverStatusesLock) {
                        serverStatuses.add(
                            ServerStatus(
                                url = "http://$mirror",
                                ip = "",
                                isAvailable = false,
                                responseTime = 0,
                                error = e.message,
                                lastChecked = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
        }

        jobs.joinAll()
        return serverStatuses
            .filter { it.isAvailable }
            .minByOrNull { it.responseTime }
            ?.url
    }

    fun getAvailableMirrors(): List<MirrorInfo> = availableMirrors

    fun getServerStatuses(): List<ServerStatus> {
        return synchronized(serverStatusesLock) {
            serverStatuses.toList()
        }
    }

    private suspend fun ensureBaseUrl() {
        if (baseUrl.get().isEmpty()) {
            val mirrors = getAvailableServers()
            val fastestMirror = findFastestMirror(mirrors)
            if (fastestMirror != null) {
                baseUrl.set(fastestMirror)
            }
        }
    }

    private suspend fun <T> makeRequestWithRetry(
        url: String,
        maxRetries: Int = 3,
        block: suspend () -> T
    ): T {
        var lastException: Exception? = null
        for (attempt in 1..maxRetries) {
            try {
                return block()
            } catch (e: Exception) {
                lastException = e
                if (attempt < maxRetries) {
                    delay((300L * attempt).milliseconds) // Even shorter delay between retries
                    logger.warn("Retry attempt $attempt for $url")
                }
            }
        }
        throw lastException ?: Exception("Unknown error")
    }

    suspend fun searchStations(query: String, limit: Int = 50): List<RadioStation> {
        val mirror = baseUrl.get()
        val url = "$mirror/json/stations/search?name=$query&limit=$limit"
        
        return try {
            val response = client.get(url)
            if (response.status.isSuccess()) {
                val json = response.bodyAsText()
                // Parse the JSON manually to handle the tags field
                val stations = mutableListOf<RadioStation>()
                val jsonArray = Json.parseToJsonElement(json).jsonArray
                
                for (element in jsonArray) {
                    val obj = element.jsonObject
                    val tagsString = obj["tags"]?.jsonPrimitive?.content ?: ""
                    val tags = if (tagsString.isNotEmpty()) {
                        tagsString.split(",").map { it.trim() }
                    } else {
                        emptyList()
                    }
                    
                    stations.add(RadioStation(
                        id = obj["id"]?.jsonPrimitive?.content ?: "",
                        name = obj["name"]?.jsonPrimitive?.content ?: "",
                        url = obj["url"]?.jsonPrimitive?.content ?: "",
                        favicon = obj["favicon"]?.jsonPrimitive?.content,
                        tags = tags,
                        country = obj["country"]?.jsonPrimitive?.content,
                        language = obj["language"]?.jsonPrimitive?.content,
                        votes = obj["votes"]?.jsonPrimitive?.int ?: 0,
                        codec = obj["codec"]?.jsonPrimitive?.content,
                        bitrate = obj["bitrate"]?.jsonPrimitive?.int
                    ))
                }
                stations
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error searching stations: ${e.message}")
            emptyList()
        }
    }

    suspend fun getStationsByCountry(countryCode: String): List<RadioStation> = coroutineScope {
        ensureBaseUrl()
        makeRequestWithRetry("$baseUrl/json/stations/bycountrycodeexact") {
            client.get("$baseUrl/json/stations/bycountrycodeexact") {
                url {
                    parameters.append("countrycode", countryCode)
                    parameters.append("limit", "100")
                }
            }.body()
        }
    }

    suspend fun getStationsByLanguage(language: String): List<RadioStation> = coroutineScope {
        ensureBaseUrl()
        makeRequestWithRetry("$baseUrl/json/stations/bylanguage") {
            client.get("$baseUrl/json/stations/bylanguage") {
                url {
                    parameters.append("language", language)
                    parameters.append("limit", "100")
                }
            }.body()
        }
    }

    suspend fun getStationsByTag(tag: String): List<RadioStation> = coroutineScope {
        ensureBaseUrl()
        makeRequestWithRetry("$baseUrl/json/stations/bytag") {
            client.get("$baseUrl/json/stations/bytag") {
                url {
                    parameters.append("tag", tag)
                    parameters.append("limit", "100")
                }
            }.body()
        }
    }

    suspend fun getCountries(): List<Country> = coroutineScope {
        ensureBaseUrl()
        makeRequestWithRetry("$baseUrl/json/countries") {
            client.get("$baseUrl/json/countries").body()
        }
    }

    suspend fun getLanguages(): List<Language> = coroutineScope {
        ensureBaseUrl()
        makeRequestWithRetry("$baseUrl/json/languages") {
            client.get("$baseUrl/json/languages").body()
        }
    }

    suspend fun getTags(): List<Tag> = coroutineScope {
        ensureBaseUrl()
        makeRequestWithRetry("$baseUrl/json/tags") {
            client.get("$baseUrl/json/tags").body()
        }
    }

    suspend fun refreshMirrors() {
        try {
            val mirrors = getAvailableServers()
            val fastestMirror = findFastestMirror(mirrors)
            if (fastestMirror != null) {
                baseUrl.set(fastestMirror)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Serializable
data class RadioStation(
    val id: String,
    val name: String,
    val url: String,
    val favicon: String? = null,
    val tags: List<String>? = null,
    val country: String? = null,
    val language: String? = null,
    val votes: Int = 0,
    val codec: String? = null,
    val bitrate: Int? = null
)

@Serializable
data class Country(
    val name: String,
    val iso_3166_1: String,
    val stationcount: Int
)

@Serializable
data class Language(
    val name: String,
    val iso_639: String,
    val stationcount: Int
)

@Serializable
data class Tag(
    val name: String,
    val stationcount: Int
) 