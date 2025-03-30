package radio.domain.repository

import radio.data.api.RadioStation
import radio.data.api.Country
import radio.data.api.Language
import radio.data.api.Tag

interface RadioRepository {
    suspend fun searchStations(query: String): List<RadioStation>
    suspend fun getStationsByCountry(countryCode: String): List<RadioStation>
    suspend fun getStationsByLanguage(language: String): List<RadioStation>
    suspend fun getStationsByTag(tag: String): List<RadioStation>
    suspend fun getCountries(): List<Country>
    suspend fun getLanguages(): List<Language>
    suspend fun getTags(): List<Tag>
}