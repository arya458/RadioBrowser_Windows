package radio.data.repository

import de.sfuhrm.radiobrowser4j.AdvancedSearch
import radio.domain.repository.RadioRepository
import de.sfuhrm.radiobrowser4j.ConnectionParams
import de.sfuhrm.radiobrowser4j.EndpointDiscovery
import de.sfuhrm.radiobrowser4j.RadioBrowser
import de.sfuhrm.radiobrowser4j.Station
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import player.util.RadioState

class RadioImpl : RadioRepository {



    private fun endpoint(): String {
        return EndpointDiscovery("").discover().get()
    }

    private fun radioBrowser(): RadioBrowser {
        return RadioBrowser(
            ConnectionParams.builder().apiUrl(endpoint()).userAgent("Demo agent/1.0").timeout(5000)
                .build()
        )
    }

    override fun getListTopVoteStations(limit: Int): Flow<RadioState<List<Station>>> {
        return flow<RadioState<List<Station>>> {

            emit(RadioState.Loading())
            try {
                val starions = radioBrowser().listTopVoteStations().limit(limit.toLong()).toList()
                emit(RadioState.Success(starions))
            }catch (e:Exception){
                println(e.message.toString())
                emit(RadioState.Error(e.message.toString()))
            }
        }

    }

    override fun getListTopClickStations(limit: Int): Flow<RadioState<List<Station>>> {
        return flow<RadioState<List<Station>>> {

            emit(RadioState.Loading())
            try {
                val stations =radioBrowser().listTopClickStations().limit(limit.toLong()).toList()
                emit(RadioState.Success(stations))
            }catch (e:Exception){
                println(e.message.toString())
                emit(RadioState.Error(e.message.toString()))
            }
        }
    }

    override fun getSearchReasult(search:AdvancedSearch,limit: Int): Flow<RadioState<List<Station>>> {
        return flow<RadioState<List<Station>>> {

            emit(RadioState.Loading())
            try {
                val stations =radioBrowser().listStationsWithAdvancedSearch(search).limit(limit.toLong()).toList()
                emit(RadioState.Success(stations))
            }catch (e:Exception){
                println(e.message.toString())
                emit(RadioState.Error(e.message.toString()))
            }
        }
    }
}