package com.apps.fatty.courses.ui.model

import com.apps.fatty.domain.model.CourseDomainModel

data class CourseUIModel(
    val id: String,
    val name: String,
    val about: String,
    val image: String = "https://cdn.iconscout.com/icon/premium/png-256-thumb/course-4081561-3376834.png"
) {
    constructor(domainCourse: CourseDomainModel) : this(
        domainCourse.id,
        domainCourse.name,
        domainCourse.about
    )
}