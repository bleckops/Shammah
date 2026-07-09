package co.bleck.shammah.data.mapper

import co.bleck.shammah.domain.model.User
import com.google.firebase.auth.FirebaseUser

object UserMapper {

    fun toDomain(firebaseUser: FirebaseUser): User = User(
        id = firebaseUser.uid,
        isAnonymous = firebaseUser.isAnonymous
    )
}
