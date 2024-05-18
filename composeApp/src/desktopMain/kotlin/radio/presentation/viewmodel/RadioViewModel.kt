package radio.presentation.viewmodel

import de.sfuhrm.radiobrowser4j.AdvancedSearch
import de.sfuhrm.radiobrowser4j.FieldName
import de.sfuhrm.radiobrowser4j.Station
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import radio.util.RadioState
import radio.domain.repository.RadioRepository

class RadioViewModel(private val radio: RadioRepository = KoinJavaComponent.getKoin().get()) {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val searchBuilder = AdvancedSearch.builder()
    private val limit = 50

    private val _radioState: MutableStateFlow<RadioState<List<Station>>> = MutableStateFlow(RadioState.Loading())
    val radioState: StateFlow<RadioState<List<Station>>> = _radioState


    init {
        getRadioTopVoteStations()
    }

    fun getRadioTopClickStations() {
        scope.launch {
            radio.getListTopClickStations(limit).collectLatest {
                _radioState.value = it
            }
        }
    }
    fun getRadioTopVoteStations() {
        scope.launch {
            radio.getListTopVoteStations(limit).collectLatest {
                _radioState.value = it
            }
        }
    }
    private fun getSearchReasult(search: AdvancedSearch) {
        scope.launch {
            radio.getSearchReasult(search,limit).collectLatest {
                _radioState.value = it
            }
        }
    }

    fun search(name:String){
        getSearchReasult(searchBuilder
            .name(name)
            .order(FieldName.VOTES)
            .build())
    }


}