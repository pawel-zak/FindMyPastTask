package pawel.zak.findmypasttask.data.remote.model

import pawel.zak.findmypasttask.domain.model.Profile
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class ProfileRemote(
    val dob: String,
    val firstname: String,
    val id: String,
    val image: String?,
    val relationships: RelationshipsRemote? = null,
    val surname: String
)

fun ProfileRemote.toProfile(): Profile {
    val squeezedPattern = "ddMMyyyy"
    val datePattern = "dd-MM-yyyy"

    val squeezedFormatter = DateTimeFormatter.ofPattern(squeezedPattern)
    val dateFormatter = DateTimeFormatter.ofPattern(datePattern)

    val date = try {
        LocalDate
            .parse(dob, squeezedFormatter)
            .format(dateFormatter)
    } catch (ex: Exception) {
        "-"
    }

    return Profile(
        id = id,
        firstname = firstname,
        surname = surname,
        dateOfBirth = date,
        image = image,
        relationships = relationships?.toRelationships()
    )
}
