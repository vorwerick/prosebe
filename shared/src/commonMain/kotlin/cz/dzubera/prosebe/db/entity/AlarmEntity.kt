package cz.dzubera.prosebe.db.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class AlarmEntity : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var name: String = ""
    var created: Long = 0L
}