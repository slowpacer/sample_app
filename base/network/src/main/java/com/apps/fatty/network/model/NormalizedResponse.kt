package com.apps.fatty.network.model

import kotlinx.serialization.Serializable

interface NormalizedResponse {
    val id: String
    val type: String
}