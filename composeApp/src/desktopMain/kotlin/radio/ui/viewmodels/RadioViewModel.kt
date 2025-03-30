package radio.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import radio.data.api.RadioBrowserApi
import radio.data.api.RadioStation

class RadioViewModel {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val api = RadioBrowserApi()

    var stations by mutableStateOf<List<RadioStation>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var searchQuery by mutableStateOf("")
        private set

    fun searchStations(query: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null
                searchQuery = query
                stations = api.searchStations(query)
            } catch (e: Exception) {
                error = e.message
                stations = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshMirrors() {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null
                api.refreshMirrors()
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }
} 