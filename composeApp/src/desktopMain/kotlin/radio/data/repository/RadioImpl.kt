package radio.data.repository

import radio.data.api.RadioBrowserApi
import radio.data.api.RadioStation
import radio.data.api.Country
import radio.data.api.Language
import radio.data.api.Tag
import radio.domain.repository.RadioRepository

class RadioImpl(private val api: RadioBrowserApi) : RadioRepository {
    override suspend fun searchStations(query: String): List<RadioStation> {
        return api.searchStations(query)
    }

    override suspend fun getStationsByCountry(countryCode: String): List<RadioStation> {
        return api.getStationsByCountry(countryCode)
    }

    override suspend fun getStationsByLanguage(language: String): List<RadioStation> {
        return api.getStationsByLanguage(language)
    }

    override suspend fun getStationsByTag(tag: String): List<RadioStation> {
        return api.getStationsByTag(tag)
    }

    override suspend fun getCountries(): List<Country> {
        return api.getCountries()
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages()
    }

    override suspend fun getTags(): List<Tag> {
        return api.getTags()
    }
}