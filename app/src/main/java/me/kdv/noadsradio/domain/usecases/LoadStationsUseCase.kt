package me.kdv.noadsradio.domain.usecases

import me.kdv.noadsradio.domain.repository.StationGroupRepository
import javax.inject.Inject

class LoadStationsUseCase @Inject constructor(
    private val stationGroupRepository: StationGroupRepository
) {
    suspend operator fun invoke() = stationGroupRepository.loadStationList()
}