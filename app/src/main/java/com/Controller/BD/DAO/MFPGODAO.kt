package com.Controller.BD.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.Constants.ConstantsBD
import com.Controller.BD.Entity.MFPGO

@Dao
interface MFPGODAO {

    @Insert
    suspend fun insertarMFPGO(mfpgo: MFPGO)

    @Query("SELECT * FROM " + ConstantsBD.TABLE_MFPGO)
    fun getAllMFPGO(): List<MFPGO>

    @Query("SELECT COUNT(*) FROM " + ConstantsBD.TABLE_MFPGO)
    suspend fun countRecords(): Int

    @Query("DELETE FROM " + ConstantsBD.TABLE_MFPGO)
    suspend fun deleteTableMFPGO()


}