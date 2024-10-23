package me.kdv.noadsradio.domain.usecases

import kotlinx.coroutines.flow.Flow
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.repository.StationGroupRepository
import me.kdv.noadsradio.domain.repository.StationRepository
import javax.inject.Inject

class GetStationsUseCase @Inject constructor(
    private val stationRepository: StationRepository
) {
    operator fun invoke() = stationRepository.getStations()
}