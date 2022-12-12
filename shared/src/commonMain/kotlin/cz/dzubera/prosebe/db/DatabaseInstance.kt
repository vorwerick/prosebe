package cz.dzubera.prosebe.db

import cz.dzubera.prosebe.db.entity.AlarmEntity
import cz.dzubera.prosebe.db.entity.ExerciseEntity
import cz.dzubera.prosebe.db.entity.UserEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class DatabaseInstance {
    private var realm: Realm

    init {
        val config = RealmConfiguration.Builder(
            schema = setOf(AlarmEntity::class, ExerciseEntity::class, UserEntity::class)
        ).build()

        // Open Realm
        realm = Realm.open(config)
    }

    fun getExerciseEntities(): List<ExerciseEntity> {
        return realm.query<ExerciseEntity>().find().toList()
    }

    fun getAlarmEntities(): List<AlarmEntity> {
        return realm.query<AlarmEntity>().find().toList()
    }

    fun getUser(): UserEntity? {
        return realm.query<UserEntity>().find().toList().firstOrNull()
    }

    fun updateUser(name: String, email: String) {
        val all = realm.query<UserEntity>().find()
        val isExist = all.any { it.id == 0 }
        if (isExist) {
            val first = all.first()
            realm.writeBlocking {
                findLatest(first)?.name = name
                findLatest(first)?.email = email
            }
        } else {
            val user = UserEntity().apply {
                this.name = name
                this.id = 0
                this.email = email
            }
            realm.writeBlocking {
                return@writeBlocking copyToRealm(user)
            }
        }
    }

    fun updateAlarm(id: Int, newH: Int, newM: Int) {
        val all = realm.query<AlarmEntity>().find()
        val first = all.first { it.id == id }
        realm.writeBlocking {
            findLatest(first)?.hour = newH
            findLatest(first)?.minute = newM
        }

    }

    fun removeAlarmEntityOlderThan(now: Long) {
        realm.writeBlocking {
            val results = query<AlarmEntity>().find()

            results.forEach {
                if (it.created < now) {
                    delete(it)
                }
            }
        }
    }

    fun addAlarmEntity(
        id: Int,
        name: String,
        created: Long,
        hour: Int,
        minute: Int,
    ): Int {
        val all = realm.query<AlarmEntity>().find()
        val any = all.any { it.id == id }
        if (any) {
            return id;
        }
        val exercise = AlarmEntity().apply {
            this.name = name
            this.id = id
            this.created = created
            this.hour = hour
            this.minute = minute
        }
        realm.writeBlocking {
            return@writeBlocking copyToRealm(exercise)
        }
        return id
    }

    fun addExerciseEntity(
        name: String,
        created: Long,
        hour: Int,
        minute: Int,
        system: Boolean,
        doneAt: Long
    ): Int {
        val all = realm.query<ExerciseEntity>().find()
        val newId = all.size
        val any = all.any { it.name == name }
        if (any) {
            return newId;
        }
        val exercise = ExerciseEntity().apply {
            this.name = name
            this.id = newId
            this.created = created
            this.hour = hour
            this.minute = minute
            this.system = system
            this.doneAt = doneAt
        }
        realm.writeBlocking {
            return@writeBlocking copyToRealm(exercise)
        }
        return newId
    }

    fun removeExercise(exerciseId: Int) {
        realm.writeBlocking {
            val results = query<ExerciseEntity>().find()

            results.forEach {
                if (it.id < exerciseId) {
                    delete(it)
                }
            }
        }
    }


}