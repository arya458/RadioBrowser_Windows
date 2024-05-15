package radio.domain.repository

import de.sfuhrm.radiobrowser4j.Station

interface RadioRepository {

//    fun endpoint(): String
//
//    fun radioBrowser(): RadioBrowser

    fun getListTopVoteStations(setData: (Station) -> Unit)

    fun getListTopClickStations(setData: (Station) -> Unit)



}