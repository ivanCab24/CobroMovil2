/*
 * *
 *  * Created by Gerardo Ruiz on 12/17/20 4:36 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/17/20 4:36 PM
 *
 */

package com.Controller.BD.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.Constants.ConstantsBD;
import com.Controller.BD.Entity.CuentaCobrada;

import java.util.List;

/**
 * Type: Interface.
 * Parent: CuentaCobradaDAO.java.
 * Name: CuentaCobradaDAO.
 */
@Dao
public interface CuentaCobradaDAO {

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: insertaCuentaCobrada.
     *
     * @param cuentaCobrada @PsiType:CuentaCobrada.
     * @Description.
     * @EndDescription.
     */
    @Insert
    void insertaCuentaCobrada(CuentaCobrada cuentaCobrada);

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: getAllCuentaCobrada.
     *
     * @param estafeta @PsiType:String.
     * @param day      @PsiType:int.
     * @return the all cuenta cobrada
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT * FROM " + ConstantsBD.TABLE_CUENTA_COBRADA + " WHERE ESTAFETA = :estafeta AND DAY = :day ORDER BY id ASC")
    List<CuentaCobrada> getAllCuentaCobrada(String estafeta, int day);

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: getAllCuentaCobrada.
     *
     * @return the all cuenta cobrada
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT * FROM " + ConstantsBD.TABLE_CUENTA_COBRADA + " ORDER BY id ASC")
    List<CuentaCobrada> getAllCuentaCobrada();

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: getEstafetas.
     *
     * @return the estafetas
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT ESTAFETA FROM " + ConstantsBD.TABLE_CUENTA_COBRADA + " GROUP BY ESTAFETA ORDER BY id ASC")
    List<String> getEstafetas();

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: getSumas.
     *
     * @return the sumas
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT SUM(CONSUMO) FROM " + ConstantsBD.TABLE_CUENTA_COBRADA + " GROUP BY ESTAFETA ORDER BY id ASC")
    List<Double> getSumas();

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: getPropinas.
     *
     * @return the propinas
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT SUM(PROPINA) FROM " + ConstantsBD.TABLE_CUENTA_COBRADA + " GROUP BY ESTAFETA ORDER BY id ASC")
    List<Double> getPropinas();

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: getTotalPropinas.
     *
     * @return the total propinas
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT SUM(COALESCE(PROPINA, 0)) FROM " + ConstantsBD.TABLE_CUENTA_COBRADA)
    double getTotalPropinas();

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: getTotalGlobal.
     *
     * @return the total global
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT SUM(COALESCE(CONSUMO, 0) + COALESCE(PROPINA, 0)) FROM " + ConstantsBD.TABLE_CUENTA_COBRADA)
    double getTotalGlobal();

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: countRecords.
     *
     * @return int
     * @Description.
     * @EndDescription. int.
     */
    @Query("SELECT COUNT(*) FROM " + ConstantsBD.TABLE_CUENTA_COBRADA)
    int countRecords();

    /**
     * Type: Method.
     * Parent: CuentaCobradaDAO.
     * Name: deleteRecords.
     *
     * @param month @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Query("DELETE FROM " + ConstantsBD.TABLE_CUENTA_COBRADA + " WHERE MONTH != :month")
    void deleteRecords(int month);

}
