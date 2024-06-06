package com.Utilities

import com.Controller.BD.Entity.CuentaCerrada
import com.Controller.BD.Entity.CuentaCobrada

/**
 * Type: Class.
 * Access: Public.
 * Name: Filters.
 *
 * @Description.
 * @EndDescription.
 */
object Filters {
    /**
     * Type: Method.
     * Parent: Filters.
     * Name: filterByFolio.
     *
     * @param query             @PsiType:String.
     * @param cuentaCerradaList @PsiType:List<CuentaCerrada>.
     * @return list
     * @Description.
     * @EndDescription. list.
    </CuentaCerrada> */
    fun filterByFolio(query: String?, cuentaCerradaList: List<CuentaCerrada>): List<CuentaCerrada> {
        val filteredList: MutableList<CuentaCerrada> = ArrayList()
        for (item in cuentaCerradaList) {
            val folioItem = item.FOLIOCTA
            if (folioItem.contains(query!!)) filteredList.add(item)
        }
        return filteredList
    }

    /**
     * Type: Method.
     * Parent: Filters.
     * Name: filterByFolioCuentaCobrada.
     *
     * @param query             @PsiType:String.
     * @param cuentaCobradaList @PsiType:List<CuentaCobrada>.
     * @return list
     * @Description.
     * @EndDescription. list.
    </CuentaCobrada> */
    fun filterByFolioCuentaCobrada(
        query: String?,
        cuentaCobradaList: List<CuentaCobrada>
    ): List<CuentaCobrada> {
        val filteredList: MutableList<CuentaCobrada> = ArrayList()
        for (item in cuentaCobradaList) {
            val folioItem = item.FOLIO
            if (folioItem.contains(query!!)) filteredList.add(item)
        }
        return filteredList
    }

    /**
     * Type: Method.
     * Parent: Filters.
     * Name: filterByEstafeta.
     *
     * @param query             @PsiType:String.
     * @param cuentaCerradaList @PsiType:List<CuentaCerrada>.
     * @return list
     * @Description.
     * @EndDescription. list.
    </CuentaCerrada> */
    fun filterByEstafeta(
        query: String?,
        cuentaCerradaList: List<CuentaCerrada>
    ): List<CuentaCerrada> {
        val filteredList: MutableList<CuentaCerrada> = ArrayList()
        for (item in cuentaCerradaList) {
            val estafetaItem = item.ESTAFETA
            if (estafetaItem.contains(query!!)) filteredList.add(item)
        }
        return filteredList
    }

    /**
     * Type: Method.
     * Parent: Filters.
     * Name: filterByEstafetaCuentaCobrada.
     *
     * @param query             @PsiType:String.
     * @param cuentaCobradaList @PsiType:List<CuentaCobrada>.
     * @return list
     * @Description.
     * @EndDescription. list.
    </CuentaCobrada> */
    fun filterByEstafetaCuentaCobrada(
        query: String?,
        cuentaCobradaList: List<CuentaCobrada>
    ): List<CuentaCobrada> {
        val filteredList: MutableList<CuentaCobrada> = ArrayList()
        for (item in cuentaCobradaList) {
            val estafetaItem = item.ESTAFETA
            if (estafetaItem.contains(query!!)) filteredList.add(item)
        }
        return filteredList
    }

    /**
     * Type: Method.
     * Parent: Filters.
     * Name: filterByVendedor.
     *
     * @param query             @PsiType:String.
     * @param cuentaCerradaList @PsiType:List<CuentaCerrada>.
     * @return list
     * @Description.
     * @EndDescription. list.
    </CuentaCerrada> */
    fun filterByVendedor(
        query: String?,
        cuentaCerradaList: List<CuentaCerrada>
    ): List<CuentaCerrada> {
        val filteredList: MutableList<CuentaCerrada> = ArrayList()
        for (item in cuentaCerradaList) {
            val vendedorItem = item.NOMBRE
            if (vendedorItem.contains(query!!)) filteredList.add(item)
        }
        return filteredList
    }

    /**
     * Type: Method.
     * Parent: Filters.
     * Name: filterByVendedorCuentaCobrada.
     *
     * @param query             @PsiType:String.
     * @param cuentaCobradaList @PsiType:List<CuentaCobrada>.
     * @return list
     * @Description.
     * @EndDescription. list.
    </CuentaCobrada> */
    fun filterByVendedorCuentaCobrada(
        query: String?,
        cuentaCobradaList: List<CuentaCobrada>
    ): List<CuentaCobrada> {
        val filteredList: MutableList<CuentaCobrada> = ArrayList()
        for (item in cuentaCobradaList) {
            val vendedorItem = item.NOMBRE
            if (vendedorItem.contains(query!!)) filteredList.add(item)
        }
        return filteredList
    }

    /**
     * Type: Method.
     * Parent: Filters.
     * Name: filterByDate.
     *
     * @param query             @PsiType:String.
     * @param cuentaCerradaList @PsiType:List<CuentaCerrada>.
     * @param hour              @PsiType:String.
     * @return list
     * @Description.
     * @EndDescription. list.
    </CuentaCerrada> */
    fun filterByDate(
        query: String?,
        cuentaCerradaList: List<CuentaCerrada>,
        hour: String
    ): List<CuentaCerrada> {
        val filteredList: MutableList<CuentaCerrada> = ArrayList()
        for (item in cuentaCerradaList) {
            val dia = item.DAY.toString()
            val year = item.YEAR.toString()
            val month = item.MONTH.toString()
            val fecha = "$year/$month/$dia"
            if (hour.isEmpty()) {
                if (fecha.contains(query!!)) filteredList.add(item)
            } else {
                val hora = item.HOUR.toString()
                val horaAuxiliar = " $hora"
                val fechaHora = fecha + horaAuxiliar
                if (fechaHora.contains(query!!)) filteredList.add(item)
            }
        }
        return filteredList
    }

    /**
     * Type: Method.
     * Parent: Filters.
     * Name: filterByDateCuentaCobrada.
     *
     * @param query             @PsiType:String.
     * @param cuentaCobradaList @PsiType:List<CuentaCobrada>.
     * @param hour              @PsiType:String.
     * @return list
     * @Description.
     * @EndDescription. list.
    </CuentaCobrada> */
    fun filterByDateCuentaCobrada(
        query: String?,
        cuentaCobradaList: List<CuentaCobrada>,
        hour: String
    ): List<CuentaCobrada> {
        val filteredList: MutableList<CuentaCobrada> = ArrayList()
        for (item in cuentaCobradaList) {
            val dia = item.DAY.toString()
            val year = item.YEAR.toString()
            val month = item.MONTH.toString()
            val fecha = "$year/$month/$dia"
            if (hour.isEmpty()) {
                if (fecha.contains(query!!)) filteredList.add(item)
            } else {
                val hora = item.HOUR.toString()
                val horaAuxiliar = " $hora"
                val fechaHora = fecha + horaAuxiliar
                if (fechaHora.contains(query!!)) filteredList.add(item)
            }
        }
        return filteredList
    }
}