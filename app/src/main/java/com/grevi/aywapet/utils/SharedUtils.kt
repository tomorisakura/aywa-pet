package com.grevi.aywapet.utils

import android.content.Context

class SharedUtils constructor(context: Context) {

    companion object {
        const val INTRO_KEY = "intro_key"
        const val LOGIN_KEY = "login_key"
        const val ID_USERS = "id_users"
        const val COUNTDOWN_TIME_KEY = "countdown_time_key"
        const val UNIQUE_KEY = "unique"
    }

    private val sharedIntro = context.getSharedPreferences(INTRO_KEY, Context.MODE_PRIVATE)
    private val sharedLogin = context.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE)
    private val idUShared = context.getSharedPreferences(ID_USERS, Context.MODE_PRIVATE)
    private val ctShared = context.getSharedPreferences(COUNTDOWN_TIME_KEY, Context.MODE_PRIVATE)
    private val unique = context.getSharedPreferences(UNIQUE_KEY, Context.MODE_PRIVATE)

    internal fun setCtShared(value : String) {
        val sp = ctShared.edit()
        sp.apply {
            putString(COUNTDOWN_TIME_KEY, value)
            apply()
        }
    }

    internal fun setIntroShared() {
        val sp = sharedIntro.edit()
        sp.apply {
            putBoolean(INTRO_KEY, true)
            apply()
        }
    }

    internal fun setLoginKey() {
        val sp = sharedLogin.edit()
        sp.apply {
            putBoolean(LOGIN_KEY, true)
            apply()
        }
    }

    internal fun setLoginKeyDestroy() {
        val sp = sharedLogin.edit()
        sp.apply {
            putBoolean(LOGIN_KEY, false)
            apply()
        }
    }

    internal fun setUserKey(id : String) {
        val sp = idUShared.edit()
        sp.apply {
            putString(ID_USERS, id)
            apply()
        }
    }

    internal fun setUniqueKey(id: String) = unique.edit().apply { putString(UNIQUE_KEY, id).apply() }

    internal fun getIntroShared() : Boolean = sharedIntro.getBoolean(INTRO_KEY, false)
    internal fun getLoginShared() : Boolean = sharedLogin.getBoolean(LOGIN_KEY, false)
    internal fun getUserShared() : String? = idUShared.getString(ID_USERS, null)
    internal fun getCountDownShared() : String? = ctShared.getString(COUNTDOWN_TIME_KEY, "24 Jam Dari Sekarangg")
    internal fun getUniqueKey() : String? = unique.getString(UNIQUE_KEY, null)
}