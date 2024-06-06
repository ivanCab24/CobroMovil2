package com.Controller.BD.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.Constants.ConstantsBD
import com.Controller.BD.Entity.UserEntity

@Dao
interface UserDAO {

    @Insert
    suspend fun insertarUsuario(userEntity: UserEntity)

    @Query("SELECT * FROM ${ConstantsBD.TABLE_USER} WHERE ESTAFETA == :estafeta")
    suspend fun getUsuario(estafeta: Int): UserEntity?

    @Query("DELETE FROM ${ConstantsBD.TABLE_USER} WHERE MONTH != :month")
    suspend fun deleteUsers(month: Int)


}