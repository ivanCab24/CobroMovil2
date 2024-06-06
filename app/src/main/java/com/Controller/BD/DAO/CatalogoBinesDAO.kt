package com.Controller.BD.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.Constants.ConstantsBD
import com.Controller.BD.Entity.MBCO2

@Dao
interface CatalogoBinesDAO {

    @Insert
    suspend fun insertarBines(mbcO2: MBCO2)

    @Query("SELECT * FROM " + ConstantsBD.TABLE_MBCO2)
    fun getAllMBCO2(): List<MBCO2>

    @Query("SELECT COUNT(*) FROM " + ConstantsBD.TABLE_MBCO2)
    suspend fun countRecords(): Int

    @Query("SELECT * FROM " + ConstantsBD.TABLE_MBCO2 + " where " + ConstantsBD.COLUMN_TC_PREFIJO + " = :prefijo")
    fun getTcPrefijo(prefijo: Int): MBCO2 // TODO: 03/02/21 Make this fun to suspend fun

    @Query("DELETE FROM " + ConstantsBD.TABLE_MBCO2)
    suspend fun deleteTableMBCO2()

}