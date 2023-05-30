package com.apps.fatty.network.model

import com.apps.fatty.data.model.CourseDataModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    override val id: String,
    override val type: String,
    val attributes: CourseAttributes,
) : NormalizedResponse {


    // that could be an ext fun, a separate mapper, or whatever else you prefer, let's keep it simple
    // by having a model per layer(module) approach we are achieving loose coupling as well
    // based on the app complexity level, that might be wrapped into just a feature module
    // and dealt with in a more straightforward manner
    fun toDataModel() = CourseDataModel(id, attributes.name, attributes.about?:"")
}

@Serializable
data class CourseAttributes(
    val code: String,
    val name: String,
    @SerialName("about_course")
    val about: String?,
)
