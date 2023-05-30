package com.apps.fatty.data.repo

import com.apps.fatty.data.datasource.LocalCourseDataSource
import com.apps.fatty.data.datasource.RemoteCourseDataSource
import com.apps.fatty.domain.model.CourseDomainModel
import com.apps.fatty.domain.repo.CoursesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val YEAR_LOW_LIMIT = 1994
private const val YEAR_HIGH_LIMIT = 2023

//at least should be offline first :)
class OfflineFirstCoursesRepository constructor(
    //injectables
    private val remoteCoursesDataSource: RemoteCourseDataSource,
    private val localCourseDataSource: LocalCourseDataSource
) :
    CoursesRepository {
    override suspend fun retrieveCoursesForYear(
        year: Int,
        page: Int,
        perPage: Int
    ): List<CourseDomainModel> {
        if (year !in YEAR_LOW_LIMIT..YEAR_HIGH_LIMIT) throw InvalidYearRange()
        return remoteCoursesDataSource.findCoursesForYear(year, page, perPage)
            .map { dataModel -> dataModel.toDomainModel() }

    }

    // just to demonstrate the approach and problems that might occur by using flows
    override suspend fun getCoursesForYear(
        year: Int,
        page: Int,
        perPage: Int
    ): Flow<List<CourseDomainModel>> {
        //TODO: fix suspend <-> flow mix
        // having a suspend call mixed with kotlin flows introduces additional handling problems in the upper level
        // flow{} has a suspended collector inside, so might be used for one-shot flows and will fix the problem,
        // but not for pagination
        //TODO: add a unified validation mechanism
        if (year !in YEAR_LOW_LIMIT..YEAR_HIGH_LIMIT) throw InvalidYearRange()
        val remoteModels = remoteCoursesDataSource.findCoursesForYear(year, page, perPage)
        return localCourseDataSource.saveCourses(remoteModels)
            .map { it.map { dataCourse -> dataCourse.toDomainModel() } }
    }
}

class InvalidYearRange() :
    IllegalStateException("Please provide valid year! It must be within range of $YEAR_LOW_LIMIT .. $YEAR_HIGH_LIMIT")