package com.Controller.BD.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.Constants.ConstantsBD;
import com.Controller.BD.Entity.TicketNoInsertados;

import java.util.List;

/**
 * Type: Interface.
 * Parent: TicketsNoRegistradosDAO.java.
 * Name: TicketsNoRegistradosDAO.
 */
@Dao
public interface TicketsNoRegistradosDAO {

    /**
     * Type: Method.
     * Parent: TicketsNoRegistradosDAO.
     * Name: insertarTicketNoRegistrado.
     *
     * @param ticketNoInsertados @PsiType:TicketNoInsertados.
     * @Description.
     * @EndDescription.
     */
    @Insert
    void insertarTicketNoRegistrado(TicketNoInsertados ticketNoInsertados);

    /**
     * Type: Method.
     * Parent: TicketsNoRegistradosDAO.
     * Name: getAllTicketsNoInsertados.
     *
     * @return the all tickets no insertados
     * @Description.
     * @EndDescription.
     */
    @Query("SELECT * FROM " + ConstantsBD.TABLE_TICKET_NO_INSERTADO)
    List<TicketNoInsertados> getAllTicketsNoInsertados();

    /**
     * Type: Method.
     * Parent: TicketsNoRegistradosDAO.
     * Name: countRecords.
     *
     * @return int
     * @Description.
     * @EndDescription. int.
     */
    @Query("SELECT COUNT(*) FROM " + ConstantsBD.TABLE_TICKET_NO_INSERTADO)
    int countRecords();

    /**
     * Type: Method.
     * Parent: TicketsNoRegistradosDAO.
     * Name: deleteTableTicketsNoInsertados.
     *
     * @Description.
     * @EndDescription.
     */
    @Query("DELETE FROM " + ConstantsBD.TABLE_TICKET_NO_INSERTADO)
    void deleteTableTicketsNoInsertados();

    /**
     * Type: Method.
     * Parent: TicketsNoRegistradosDAO.
     * Name: deleteTicket.
     *
     * @param ticketNoInsertados @PsiType:TicketNoInsertados.
     * @Description.
     * @EndDescription.
     */
    @Delete
    void deleteTicket(TicketNoInsertados ticketNoInsertados);

}
