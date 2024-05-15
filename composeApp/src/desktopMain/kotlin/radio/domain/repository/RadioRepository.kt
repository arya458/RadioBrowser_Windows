package radio.domain.repository

import de.sfuhrm.radiobrowser4j.AdvancedSearch
import de.sfuhrm.radiobrowser4j.Station
import kotlinx.coroutines.flow.Flow
import player.util.RadioState

interface RadioRepository {

//    fun endpoint(): String
//
//    fun radioBrowser(): RadioBrowser

    fun getListTopVoteStations(limit: Int): Flow<RadioState<List<Station>>>

    fun getListTopClickStations(limit: Int): Flow<RadioState<List<Station>>>

    fun getSearchReasult(search: AdvancedSearch,limit: Int): Flow<RadioState<List<Station>>>




}