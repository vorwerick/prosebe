package cz.dzubera.prosebe.android

import android.app.Application
import cz.dzubera.prosebe.repository.Repository

class App : Application() {

    companion object {
        val repository: Repository by lazy { Repository() }
    }


}