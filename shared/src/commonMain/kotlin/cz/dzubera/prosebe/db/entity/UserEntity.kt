package cz.dzubera.prosebe.db.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UserEntity : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var email: String = ""
}