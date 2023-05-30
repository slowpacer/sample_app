package com.apps.fatty.domain.repo

import com.apps.fatty.domain.model.CourseDomainModel
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {

    suspend fun retrieveCoursesForYear(year: Int, page: Int, perPage: Int): List<CourseDomainModel>

    suspend fun getCoursesForYear(year: Int, page: Int, perPage: Int): Flow<List<CourseDomainModel>>
    //TODO: implement convenient functions for specific course retrieval approaches
//    fun getCoursesCloseToMe():Flow<List<String>>
}