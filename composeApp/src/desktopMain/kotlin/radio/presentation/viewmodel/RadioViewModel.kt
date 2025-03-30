package radio.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import radio.data.api.RadioStation
import radio.data.api.Country
import radio.data.api.Language
import radio.data.api.Tag
import radio.domain.repository.RadioRepository
import radio.data.api.RadioBrowserApi

class RadioViewModel(
    private val repository: RadioRepository,
    private val api: RadioBrowserApi,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private val _stations = MutableStateFlow<List<RadioStation>>(emptyList())
    val stations: StateFlow<List<RadioStation>> = _stations.asStateFlow()

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries.asStateFlow()

    private val _languages = MutableStateFlow<List<Language>>(emptyList())
    val languages: StateFlow<List<Language>> = _languages.asStateFlow()

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> = _tags.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun searchStations(query: String) {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _stations.value = repository.searchStations(query)
            } catch (e: Exception) {
                _error.value = e.message
                _stations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getStationsByCountry(countryCode: String) {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _stations.value = repository.getStationsByCountry(countryCode)
            } catch (e: Exception) {
                _error.value = e.message
                _stations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getStationsByLanguage(language: String) {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _stations.value = repository.getStationsByLanguage(language)
            } catch (e: Exception) {
                _error.value = e.message
                _stations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getStationsByTag(tag: String) {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _stations.value = repository.getStationsByTag(tag)
            } catch (e: Exception) {
                _error.value = e.message
                _stations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCountries() {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _countries.value = repository.getCountries()
            } catch (e: Exception) {
                _error.value = e.message
                _countries.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadLanguages() {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _languages.value = repository.getLanguages()
            } catch (e: Exception) {
                _error.value = e.message
                _languages.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTags() {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _tags.value = repository.getTags()
            } catch (e: Exception) {
                _error.value = e.message
                _tags.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshMirrors() {
        coroutineScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                api.refreshMirrors()
                // Refresh the current search
                searchStations("")
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}