package pawel.zak.findmypasttask.domain.use_case.profile.get_profile_relationships


import kotlinx.coroutines.*
import kotlinx.coroutines.flow.last
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.domain.model.ProfileRelationships
import pawel.zak.findmypasttask.domain.model.Relationships
import pawel.zak.findmypasttask.domain.repository.UserProfilesRepository
import javax.inject.Inject

class GetProfileRelationshipsUseCase @Inject constructor(private val repository: UserProfilesRepository) {
    suspend operator fun invoke(
        userId: String,
        relationships: Relationships, dispatcher: CoroutineDispatcher
    ): Resource<ProfileRelationships> =
        withContext(dispatcher) {
            val father = relationships.father?.let {
                async {
                    repository.getProfile(userId, it)
                }
            }
            val mother = relationships.mother?.let {
                async {
                    repository.getProfile(userId, it)
                }
            }

            val spouse = relationships.spouse?.let {
                async {
                    repository.getProfile(userId, relationships.spouse)
                }
            }

            val childrenDeferred = relationships.children?.map {
                async {
                    repository.getProfile(userId, it)
                }
            }

            return@withContext Resource.Success(
                ProfileRelationships(
                    father = father?.await()?.last()?.data,
                    mother = mother?.await()?.last()?.data,
                    spouse = spouse?.await()?.last()?.data,
                    children = childrenDeferred?.awaitAll()?.map { it ->
                        when (val last = it.last()) {
                            is Resource.Success -> last.data
                            else -> null
                        }
                    }
                )
            )
        }
}
