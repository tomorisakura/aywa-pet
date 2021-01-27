package com.grevi.aywapet.repository.mapper

import com.grevi.aywapet.db.entity.Users
import com.grevi.aywapet.model.User

interface EntityMapper {
    fun mapToEntity(model : User, token : String) : Users
}