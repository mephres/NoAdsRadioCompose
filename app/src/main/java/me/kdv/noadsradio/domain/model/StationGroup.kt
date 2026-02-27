package me.kdv.noadsradio.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StationGroup(
    val id: Int,
    val name: String,
    val description: String,
    var isCurrent: Boolean = false
): Parcelable
