package radio.data.repository

import radio.domain.repository.RadioRepository
import de.sfuhrm.radiobrowser4j.ConnectionParams
import de.sfuhrm.radiobrowser4j.EndpointDiscovery
import de.sfuhrm.radiobrowser4j.RadioBrowser
import de.sfuhrm.radiobrowser4j.Station
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RadioImpl(var limit: Int = 10) : RadioRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private fun endpoint(): String {
        return EndpointDiscovery("").discover().get()
    }

    private fun radioBrowser(): RadioBrowser {
        return RadioBrowser(
            ConnectionParams.builder().apiUrl(endpoint()).userAgent("Demo agent/1.0").timeout(5000)
                .build()
        )
    }

    override fun getListTopVoteStations(setData :(Station) -> Unit) {
        scope.launch(Dispatchers.IO) {
            radioBrowser().listTopVoteStations().limit(limit.toLong()).forEach {
                setData(it)
            }

        }

    }

    override fun getListTopClickStations(setData :(Station) -> Unit) {
        scope.launch(Dispatchers.IO) {
            radioBrowser().listTopClickStations().limit(limit.toLong()).forEach {
                setData(it)
            }
        }
    }
}