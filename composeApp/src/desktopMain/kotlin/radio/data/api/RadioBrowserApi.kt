package radio.data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.timeout
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import java.net.URL
import java.net.UnknownHostException
import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLHandshakeException
import kotlin.time.Duration.Companion.milliseconds

class RadioBrowserApi {
    private val logger = LoggerFactory.getLogger(RadioBrowserApi::class.java)
    private var baseUrl: String? = null
    private var availableMirrors: List<MirrorInfo> = emptyList()
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
            connectTimeoutMillis = 5000
            socketTimeoutMillis = 5000
        }
        install(DefaultRequest) {
            header("User-Agent", "RadioBrowser-Windows/1.0")
        }
    }

    data class MirrorInfo(
        val url: String,
        val responseTime: Long,
        val isAvailable: Boolean,
        val error: String? = null,
        val statusCode: Int? = null
    )

    data class ServerInfo(
        val ip: String,
        val name: String
    )

    data class ServerStatus(
        val url: String,
        val ip: String,
        val name: String?,
        val isAvailable: Boolean,
        val responseTime: Long,
        val error: String? = null,
        val statusCode: Int? = null,
        val lastChecked: Long = System.currentTimeMillis()
    )

    // Updated list of known mirrors with only IPv4 addresses
    private val knownMirrors = listOf(
        "162.55.180.156",  // CleanIp
        "95.179.139.106",  // nl1
        "89.58.16.19",     // at1
        "188.68.62.16"     // de1
    )

    private var serverStatuses: List<ServerStatus> = emptyList()

    private suspend fun testServerConnection(host: String, port: Int = 443): Boolean {
        return try {
            val socket = Socket()
            val address = InetAddress.getByName(host)
            socket.connect(java.net.InetSocketAddress(address, port), 5000)
            socket.close()
            true
        } catch (e: Exception) {
            logger.debug("Connection test failed for $host: ${e.message}")
            false
        }
    }

    private fun formatUrl(ip: String): String {
        return if (ip.contains(":")) {
            // IPv6 address
            "https://[$ip]"
        } else {
            // IPv4 address
            "https://$ip"
        }
    }

    private suspend fun getAvailableServers(): List<String> {
        return try {
            // First try to get servers from the API
            logger.info("Fetching server list from API...")
            val servers = client.get("https://162.55.180.156/json/servers").body<List<ServerInfo>>()
            
            if (servers.isNotEmpty()) {
                logger.info("Found ${servers.size} servers from API")
                return servers.map { formatUrl(it.ip) }
            }

            // If API call fails, try known mirrors
            logger.info("API call failed, trying known mirrors...")
            val responsiveMirrors = knownMirrors.filter { ip ->
                val isAvailable = testServerConnection(ip)
                if (isAvailable) {
                    logger.info("Direct connection successful to $ip")
                } else {
                    logger.debug("Direct connection failed to $ip")
                }
                isAvailable
            }

            if (responsiveMirrors.isNotEmpty()) {
                logger.info("Found ${responsiveMirrors.size} responsive mirrors")
                return responsiveMirrors.map { formatUrl(it) }
            }

            // If no mirrors work, try DNS lookup
            logger.info("No mirrors responsive, trying DNS lookup...")
            val addresses = InetAddress.getAllByName("all.api.radio-browser.info")
            logger.info("Found ${addresses.size} available servers")
            
            addresses.map { address ->
                try {
                    address.hostAddress
                } catch (e: Exception) {
                    logger.warn("Failed to get IP for ${address.hostAddress}: ${e.message}")
                    null
                }
            }.filterNotNull().map { formatUrl(it) }
        } catch (e: Exception) {
            logger.error("Failed to get available servers: ${e.message}")
            // Return all known mirrors as fallback
            logger.info("Using all known mirrors as fallback")
            knownMirrors.map { formatUrl(it) }
        }
    }

    private suspend fun checkMirrorAvailability(mirror: String): MirrorInfo {
        return try {
            // First try DNS resolution
            val host = URL(mirror).host
            try {
                InetAddress.getAllByName(host)
            } catch (e: UnknownHostException) {
                logger.error("DNS resolution failed for $mirror: ${e.message}")
                return MirrorInfo(mirror, Long.MAX_VALUE, false, "DNS resolution failed: ${e.message}")
            }

            // Then try HTTP request
            val startTime = System.currentTimeMillis()
            val response = client.get("$mirror/json/stats")
            val endTime = System.currentTimeMillis()
            val responseTime = endTime - startTime
            
            if (response.status.isSuccess()) {
                logger.info("Mirror $mirror responded in ${responseTime}ms with status ${response.status}")
                MirrorInfo(mirror, responseTime, true, null, response.status.value)
            } else {
                val errorMsg = "Status: ${response.status}"
                logger.error("Mirror $mirror returned status ${response.status}")
                MirrorInfo(mirror, Long.MAX_VALUE, false, errorMsg, response.status.value)
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is HttpRequestTimeoutException -> "Timeout after 5000ms"
                is ClientRequestException -> "Request failed: ${e.message}"
                is UnknownHostException -> "DNS resolution failed: ${e.message}"
                is SSLHandshakeException -> "SSL handshake failed: ${e.message}"
                else -> "Error: ${e.javaClass.simpleName} - ${e.message}"
            }
            logger.error("Mirror $mirror failed: $errorMessage")
            MirrorInfo(mirror, Long.MAX_VALUE, false, errorMessage)
        }
    }

    private suspend fun findFastestMirror(): String {
        val mirrors = getAvailableServers()
        logger.info("Checking ${mirrors.size} available mirrors")

        return coroutineScope {
            val results = mirrors.map { mirror ->
                async {
                    // Try up to 3 times with delay between attempts
                    var result: MirrorInfo? = null
                    for (attempt in 1..3) {
                        if (attempt > 1) {
                            delay(500.milliseconds)
                        }
                        result = checkMirrorAvailability(mirror)
                        if (result.isAvailable) break
                    }
                    result ?: MirrorInfo(mirror, Long.MAX_VALUE, false, "All attempts failed")
                }
            }
            availableMirrors = results.map { it.await() }
            
            // Update server statuses
            serverStatuses = availableMirrors.map { mirror ->
                ServerStatus(
                    url = mirror.url,
                    ip = URL(mirror.url).host,
                    name = null, // We'll update this when we get server info
                    isAvailable = mirror.isAvailable,
                    responseTime = mirror.responseTime,
                    error = mirror.error,
                    statusCode = mirror.statusCode
                )
            }

            // Try to find the fastest available mirror
            val fastestMirror = availableMirrors
                .filter { it.isAvailable }
                .minByOrNull { it.responseTime }
                ?.url

            if (fastestMirror != null) {
                logger.info("Selected fastest mirror: $fastestMirror")
                return@coroutineScope fastestMirror
            }

            // If no mirrors are available, try the main API
            logger.warn("No mirrors available, trying main API")
            try {
                val mainApi = "https://all.api.radio-browser.info"
                val result = checkMirrorAvailability(mainApi)
                if (result.isAvailable) {
                    logger.info("Main API is available")
                    return@coroutineScope mainApi
                }
            } catch (e: Exception) {
                logger.error("Main API failed: ${e.message}")
            }

            // If still no mirror is available, use the first one as fallback
            val fallbackMirror = mirrors.first()
            logger.warn("Using fallback mirror: $fallbackMirror")
            fallbackMirror
        }
    }

    fun getAvailableMirrors(): List<MirrorInfo> = availableMirrors

    fun getServerStatuses(): List<ServerStatus> = serverStatuses

    private suspend fun ensureBaseUrl() {
        if (baseUrl == null) {
            baseUrl = findFastestMirror()
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

    suspend fun searchStations(query: String): List<RadioStation> = coroutineScope {
        ensureBaseUrl()
        makeRequestWithRetry("$baseUrl/json/stations/search") {
            client.get("$baseUrl/json/stations/search") {
                url {
                    parameters.append("name", query)
                    parameters.append("limit", "100")
                }
            }.body()
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
        baseUrl = null
        ensureBaseUrl()
    }
}

@kotlinx.serialization.Serializable
data class RadioStation(
    val id: String,
    val name: String,
    val url: String,
    val favicon: String?,
    val tags: List<String>?,
    val country: String?,
    val language: String?,
    val votes: Int,
    val codec: String?,
    val bitrate: Int?
)

@kotlinx.serialization.Serializable
data class Country(
    val name: String,
    val iso_3166_1: String,
    val stationcount: Int
)

@kotlinx.serialization.Serializable
data class Language(
    val name: String,
    val iso_639: String,
    val stationcount: Int
)

@kotlinx.serialization.Serializable
data class Tag(
    val name: String,
    val stationcount: Int
) 