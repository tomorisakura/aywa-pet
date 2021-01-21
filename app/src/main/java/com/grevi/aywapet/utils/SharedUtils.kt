package com.grevi.aywapet.utils

import android.content.Context

class SharedUtils constructor(context: Context) {

    companion object {
        const val INTRO_KEY = "intro_key"
        const val LOGIN_KEY = "login_key"
        const val ID_USERS = "id_users"
    }

    private val sharedIntro = context.getSharedPreferences(INTRO_KEY, Context.MODE_PRIVATE)
    private val sharedLogin = context.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE)
    private val idUShared = context.getSharedPreferences(ID_USERS, Context.MODE_PRIVATE)

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

    internal fun getIntroShared() : Boolean = sharedIntro.getBoolean(INTRO_KEY, false)
    internal fun getLoginShared() : Boolean = sharedLogin.getBoolean(LOGIN_KEY, false)
    internal fun getUserShared() : String? = idUShared.getString(ID_USERS, null)
}