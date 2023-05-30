package com.apps.fatty.network.datasource

import com.apps.fatty.data.datasource.RemoteCourseDataSource
import com.apps.fatty.data.model.CourseDataModel
import com.apps.fatty.network.ktor.TeachersApi
import kotlinx.coroutines.flow.flow


class CoursesRemoteDataSource constructor(val api: TeachersApi) : RemoteCourseDataSource {


    override suspend fun findCoursesForYear(
        year: Int,
        page: Int,
        perPage: Int
    ) = api.fetchCourses(year, page, perPage).data.map { it.toDataModel() }

}