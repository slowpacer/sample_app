package com.apps.fatty.domain.usecase

import com.apps.fatty.domain.base.RequestValues
import com.apps.fatty.domain.base.UseCase
import com.apps.fatty.domain.model.CourseDomainModel
import com.apps.fatty.domain.repo.CoursesRepository
import kotlinx.coroutines.flow.Flow

class GetCoursesUseCase constructor(val coursesRepository: CoursesRepository) :
    UseCase<CoursesInquiry, List<CourseDomainModel>> {

    override suspend fun invoke(request: CoursesInquiry): List<CourseDomainModel> {
        return with(request) {
            coursesRepository.retrieveCoursesForYear(year, page, coursesPerPage)
        }
    }

    override suspend fun flowExample(request: CoursesInquiry): Flow<List<CourseDomainModel>> {
        return with(request) {
            coursesRepository.getCoursesForYear(year, page, coursesPerPage)
        }
    }
}

data class CoursesInquiry(val year: Int, val page: Int, val coursesPerPage: Int = 20) :
    RequestValues
