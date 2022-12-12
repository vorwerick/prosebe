package cz.dzubera.prosebe.repository

import com.soywiz.krypto.md5
import cz.dzubera.prosebe.db.DatabaseInstance
import cz.dzubera.prosebe.http.HttpRequest
import cz.dzubera.prosebe.http.Response
import cz.dzubera.prosebe.repository.model.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import kotlinx.datetime.Clock
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class Repository {

    private var databaseInstance: DatabaseInstance

    init {
        databaseInstance = DatabaseInstance()
        val defaultAlarmList = mutableListOf<AlarmModel>(
            AlarmModel(ALARM_MODEL_MORNING_AFFIRMATION, 7, 30, "Dobré ráno"),
            AlarmModel(ALARM_MODEL_ACHIEVE_REMINDER_1, 12, 30, "Zapiš si co se ti dnes povedlo"),
            AlarmModel(ALARM_MODEL_ACHIEVE_REMINDER_2, 15, 30, "Zapiš si co se ti dnes povedlo"),
            AlarmModel(ALARM_MODEL_ACHIEVE_REMINDER_3, 17, 0, "Zapiš si co se ti dnes povedlo"),
            AlarmModel(ALARM_MODEL_ACHIEVE_REMINDER_4, 20, 30, "Zapiš si co se ti dnes povedlo"),
            AlarmModel(
                ALARM_MODEL_ACHIEVE_REMINDER_5,
                22,
                0,
                "Zapiš si co se ti dnes povedlo."
            ),
        )
        addAlarms(defaultAlarmList)
    }

    private var currentUser: UserModel? = null

    private val exerciseList = mutableListOf<AchieveModel>()

    private val completedExercises = mutableListOf<Int>()

    fun addAchieve(h: Int, m: Int, name: String, system: Boolean) {
        val time = Clock.System.now().toEpochMilliseconds()
        val id = databaseInstance.addExerciseEntity(
            name,
            time,
            h,
            m,
            system,
            0L
        )
        val model = AchieveModel(
            id,
            h,
            m,
            name,
            time,
            0L,
            system
        )
        exerciseList.add(model)
    }

    fun removeAchieve(exerciseId: Int) {
        databaseInstance.removeExercise(exerciseId)
        exerciseList.removeAll { it.id == exerciseId }
    }

    fun saveUser(name: String, email: String) {
        databaseInstance.updateUser(name, email)
    }

    fun getUserName(): String? {
        return databaseInstance.getUser()?.name
    }

    suspend fun login(mail: String, password: String): Response? {
        val response = HttpRequest().loginUser(mail, password)
        val json: JsonObject = response.body()
        json.forEach {
            val email = it.value.jsonObject["email"].toString().replace("\"", "")
            val name = it.value.jsonObject["name"].toString().replace("\"", "")
            val hash = it.value.jsonObject["password"].toString().replace("\"", "")

            if (mail == email && hash.toString() == password.toByteArray()
                    .md5().base64
            ) {
                currentUser = UserModel(0, name, email)
                saveUser(name, email)
                return Response(response.status.value, response.bodyAsText())
            }
        }
        return null;
    }

    suspend fun register(email: String, name: String, password: String): Response {
        val response =
            HttpRequest().registerUser(email, name, password)

        return Response(response.status.value, "")
    }

    fun isLogged(): Boolean {
        return currentUser != null
    }

    fun getUser(): UserModel? {
        return currentUser
    }


    fun getAchieveById(exerciseId: Int): AchieveModel? {
        return exerciseList.firstOrNull { it.id == exerciseId }
    }

    fun getAlarmById(affirmationId: Int): AlarmModel? {
        return getAlarms().firstOrNull { it.id == affirmationId }
    }

    fun logout() {
        currentUser = null
    }

    suspend fun sendFeedback(text: String) {
        currentUser?.let {

            val response = HttpRequest().sendFeedback(it.email, text)
            println(response.status)
        }

    }

    fun addAlarms(list: List<AlarmModel>) {
        list.forEach {
            databaseInstance.addAlarmEntity(
                it.id,
                it.name,
                Clock.System.now().toEpochMilliseconds(),
                it.hour,
                it.minute
            )
        }
    }

    fun getAlarms(): List<AlarmModel> {
        return databaseInstance.getAlarmEntities()
            .map { AlarmModel(it.id, it.hour, it.minute, it.name) }
    }

    fun getAchieves(): List<AchieveModel> {
        val list = mutableListOf<AchieveModel>()
        list.addAll(databaseInstance.getExerciseEntities()
            .map {
                AchieveModel(
                    it.id,
                    it.hour,
                    it.minute,
                    it.name,
                    it.created,
                    it.doneAt,
                    it.system
                )
            })
        return list
    }

    fun updateAlarm(alarmModelMorningAffirmation: Int, h: Int, m: Int) {
        databaseInstance.updateAlarm(alarmModelMorningAffirmation, h, m)
    }
}