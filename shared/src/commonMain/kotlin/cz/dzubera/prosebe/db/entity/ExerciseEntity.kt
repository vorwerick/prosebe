package cz.dzubera.prosebe.db.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ExerciseEntity : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var name: String = ""
    var created: Long = 0L
    var doneAt: Long = 0L
    var system: Boolean = false
}