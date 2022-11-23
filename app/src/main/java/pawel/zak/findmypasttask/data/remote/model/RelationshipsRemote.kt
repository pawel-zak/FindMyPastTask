package pawel.zak.findmypasttask.data.remote.model

import pawel.zak.findmypasttask.domain.model.Relationships


data class RelationshipsRemote(
    val children: List<String>? = listOf(),
    val father: String?,
    val mother: String?,
    val spouse: String?
)

fun RelationshipsRemote.toRelationships() =
    Relationships(children = children, father = father, mother = mother, spouse = spouse)