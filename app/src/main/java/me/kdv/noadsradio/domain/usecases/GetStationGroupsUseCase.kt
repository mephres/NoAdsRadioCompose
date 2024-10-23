package me.kdv.noadsradio.domain.usecases

import kotlinx.coroutines.flow.Flow
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.repository.StationGroupRepository
import javax.inject.Inject

class GetStationGroupsUseCase @Inject constructor(
    private val stationGroupRepository: StationGroupRepository
) {
    operator fun invoke(): Flow<List<StationGroup>> = stationGroupRepository.getStationGroups()
}