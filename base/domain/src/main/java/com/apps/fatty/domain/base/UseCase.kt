package com.apps.fatty.domain.base

import kotlinx.coroutines.flow.Flow


//TODO: decide whether we want to have a high level orchestrator - a mechanism for running/executing our use cases
//TODO: in example a UseCase executor
interface UseCase<in Request : RequestValues, out Result> {
    suspend operator fun invoke(request: Request): Result
    suspend fun flowExample(request: Request): Flow<Result>

    // Based on our agreements we can have diff wraps around the return(result) type.
    // Other possible options - define differently functioning use cases,
    // i.e. having separate abstraction for specific use cases.
    // key principle of any architectural approach is to ensure loose coupling
    // between use cases and other system components, particularly data.

    // operator fun invoke(request: Request): NextBleedingEdgeTechnology<Result>
    // or just raw Result if we are gone stay extremely "clean"
    // operator fun invoke(request: Request): Result
    // operator fun invoke(request: Request): Flow<Result>
}

//TODO: we potentially might need to define boundaries for our use case and specify then to some extend
interface RequestValues
