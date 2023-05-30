package com.apps.fatty.data.model

import com.apps.fatty.domain.model.CourseDomainModel

class CourseDataModel(val id: String, val name: String, val description: String) {
    //welcome to endless mapping experience
    fun toDomainModel() = CourseDomainModel(id, name, description)
}