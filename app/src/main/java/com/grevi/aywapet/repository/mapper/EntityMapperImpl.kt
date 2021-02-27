package com.grevi.aywapet.repository.mapper

import com.grevi.aywapet.db.entity.Users
import com.grevi.aywapet.model.User
import javax.inject.Inject

class EntityMapperImpl @Inject constructor() : EntityMapper {
    override fun mapToEntity(model: User, token : String): Users {
        return Users(
                id = model.id,
                username = model.username,
                name = model.name,
                email = model.email,
                address = model.address,
                uid = model.uid,
                token = token
        )
    }
}