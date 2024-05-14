package Radio

import de.sfuhrm.radiobrowser4j.RadioBrowser
import de.sfuhrm.radiobrowser4j.Station
import java.util.*
import java.util.stream.Stream

interface RadioRepository {

//    fun endpoint(): String
//
//    fun radioBrowser(): RadioBrowser

    fun getListTopVoteStations(setData: (Station) -> Unit)

    fun getListTopClickStations(setData: (Station) -> Unit)



}