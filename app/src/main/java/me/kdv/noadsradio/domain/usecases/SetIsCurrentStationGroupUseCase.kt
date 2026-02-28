package me.kdv.noadsradio.domain.usecases

import kotlinx.coroutines.flow.Flow
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.repository.StationGroupRepository
import javax.inject.Inject

class SetIsCurrentStationGroupUseCase @Inject constructor(
    private val stationGroupRepository: StationGroupRepository
) {
    suspend operator fun invoke(id: Int?) = stationGroupRepository.setStationIsCurrentBy(id = id)
}