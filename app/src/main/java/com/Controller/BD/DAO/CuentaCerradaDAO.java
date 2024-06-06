/*
 * *
 *  * Created by Gerardo Ruiz on 12/17/20 4:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/17/20 4:21 PM
 *
 */

package com.Controller.BD.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.Constants.ConstantsBD;
import com.Controller.BD.Entity.CuentaCerrada;

import java.util.List;

/**
 * Type: Interface.
 * Parent: CuentaCerradaDAO.java.
 * Name: CuentaCerradaDAO.
 */
@Dao
public interface CuentaCerradaDAO {

    /**
     * Type: Method.
     * Parent: CuentaCerradaDAO.
     * Name: insertaCuentaCerrada.
     *
     * @param cuentaCerrada @PsiType:CuentaCerrada.
     * @Description.
     * @EndDescription.
     */
    @Insert
    void insertaCuentaCerrada(CuentaCerrada cuentaCerrada);

    /**
     * Type: Method.
     * Parent: CuentaCerradaDAO.
     * Name: getAllCuentasCerradas.
     *
     * @return the all cuentas cerradas
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT * FROM " + ConstantsBD.TABLE_CUENTA_CERRADA + " ORDER BY " + ConstantsBD.COLUM_DAY + " ASC")
    List<CuentaCerrada> getAllCuentasCerradas();

    /**
     * Type: Method.
     * Parent: CuentaCerradaDAO.
     * Name: getCountRecords.
     *
     * @return the count records
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT COUNT(*) FROM " + ConstantsBD.TABLE_CUENTA_CERRADA)
    int getCountRecords();

    /**
     * Type: Method.
     * Parent: CuentaCerradaDAO.
     * Name: deleteRecords.
     *
     * @param month @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Query("DELETE FROM " + ConstantsBD.TABLE_CUENTA_CERRADA + " WHERE MONTH != :month")
    void deleteRecords(int month);

}
