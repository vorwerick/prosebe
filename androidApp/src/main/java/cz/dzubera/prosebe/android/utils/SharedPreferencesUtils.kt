package cz.dzubera.prosebe.android.utils

import android.content.Context
import cz.dzubera.prosebe.android.MainActivity

object SharedPreferencesUtils {

    private const val SHARED_PREFERENCE_NAME = "prosebe"

    fun get(context: Context, key: String, default: String = ""): String {
        return activity(context).getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getString(key, default) ?: default
    }

    fun set(context: Context, key: String, value: String) {
        activity(context).getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
            .edit().putString(key, value).apply()
    }

    private fun activity(context: Context): MainActivity {
        return context as MainActivity
    }
}