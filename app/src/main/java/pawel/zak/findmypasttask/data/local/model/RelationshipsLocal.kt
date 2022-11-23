package pawel.zak.findmypasttask.data.local.model

import pawel.zak.findmypasttask.domain.model.Relationships


data class RelationshipsLocal(
    val children: List<String>? = listOf(),
    val father: String? = null,
    val mother: String? = null,
    val spouse: String? = null
)

fun RelationshipsLocal.toRelationships() =
    Relationships(children = children, father = father, mother = mother, spouse = spouse)

fun Relationships.toLocalRelationships() =
    RelationshipsLocal(children = children, father = father, mother = mother, spouse = spouse)