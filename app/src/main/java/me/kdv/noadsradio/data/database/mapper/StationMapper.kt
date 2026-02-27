package me.kdv.noadsradio.data.database.mapper

import me.kdv.noadsradio.data.database.model.StationDbModel
import me.kdv.noadsradio.data.network.model.StationDto
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationPlaybackState
import javax.inject.Inject

class StationMapper @Inject constructor() {
    fun mapDtoToDb(dto: StationDto): StationDbModel {
        return StationDbModel(
            id = dto.groupId * 1000 + dto.id,
            stationId = dto.id,
            groupId = dto.groupId,
            groupName = dto.groupName ?: noData,
            name = dto.name ?: noData,
            url = dto.url ?: "",
            noJingle = dto.noJingle,
            image = dto.image ?: "",
            state = StationPlaybackState.STOPPED.ordinal,
            enabled = dto.enabled
        )
    }

    fun mapDbToEntity(db: StationDbModel): Station {
        return Station(
            id = db.id,
            stationId = db.stationId,
            groupId = db.groupId,
            groupName = db.groupName,
            name = db.name,
            url = db.url,
            noJingle = db.noJingle,
            image = db.image,
            smallTitle = db.name.getSmallTitle().uppercase(),
            state = StationPlaybackState.values()[db.state]
        )
    }

    fun mapEntityToDb(entity: Station): StationDbModel {
        return StationDbModel(
            id = entity.id,
            stationId = entity.stationId,
            groupId = entity.groupId,
            groupName = entity.groupName,
            name = entity.name,
            url = entity.url,
            noJingle = entity.noJingle,
            image = entity.image,
            state = entity.state.ordinal
        )
    }

    private fun String.getSmallTitle(): String {

        val array = this.split(" ")

        if (array.isEmpty()) return "НД"

        return if(array.size > 1) {
            "${array[0].first()}${array[1].first()}"
        } else {
            array[0].substring(0, 2)
        }
    }

    companion object {
        const val noData = "Нет данных"
    }
}