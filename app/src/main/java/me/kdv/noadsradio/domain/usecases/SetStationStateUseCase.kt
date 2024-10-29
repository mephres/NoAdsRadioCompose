package me.kdv.noadsradio.domain.usecases

import kotlinx.coroutines.flow.Flow
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.repository.StationGroupRepository
import me.kdv.noadsradio.domain.repository.StationRepository
import javax.inject.Inject

class SetStationStateUseCase @Inject constructor(
    private val stationRepository: StationRepository
) {
    suspend operator fun invoke(id: Int, state: Int) = stationRepository.setStationState(id = id, state = state)
}