package com.fueled.recyclerviewbindings.util

import com.fueled.recyclerviewbindings.entity.User
import com.fueled.recyclerviewbindings.model.UserModel

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 * @author chetansachdeva on 24/09/17
 */

object Mapper {

    /**
     * map user with id
     */
    fun mapToUserModel(user: User): UserModel {
        val userModel = UserModel()
        userModel.id = user.id
        userModel.name = user.name
        return userModel
    }
}