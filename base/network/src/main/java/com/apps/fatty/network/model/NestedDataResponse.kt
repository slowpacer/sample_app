package com.apps.fatty.grasper.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NestedDataResponse<T>(val data: List<T>)