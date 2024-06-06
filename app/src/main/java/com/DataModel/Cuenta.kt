package com.DataModel

import android.util.Log
import com.squareup.moshi.JsonClass
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Type: Class.
 * Access: Public.
 * Name: Cuenta.
 *
 * @Description.
 * @EndDescription.
 */
data class Cuenta
/**
 * Type: Method.
 * Parent: Cuenta.
 * Name: Cuenta.
 *
 * @param fecha         @PsiType:long.
 * @param idLocal       @PsiType:long.
 * @param idComa        @PsiType:long.
 * @param idPos         @PsiType:long.
 * @param saldo         @PsiType:double.
 * @param total         @PsiType:double.
 * @param importe       @PsiType:double.
 * @param desc          @PsiType:double.
 * @param descTipo      @PsiType:long.
 * @param descID        @PsiType:long.
 * @param referencia    @PsiType:long.
 * @param cerrada       @PsiType:int.
 * @param puedecerrar   @PsiType:long.
 * @param facturado     @PsiType:int.
 * @param imprime       @PsiType:int.
 * @param folio         @PsiType:String.
 * @param numcomensales @PsiType:String.
 * @Description.
 * @EndDescription.
 */(
    /**
     * The Fecha.
     */
    var fecha: Long,
    /**
     * The Id local.
     */
    var idLocal: Long,
    /**
     * The Id coma.
     */
    var idComa: Long,
    /**
     * The Id pos.
     */
    var idPos: Long,
    /**
     * The Saldo.
     */
    var saldo: Double,
    /**
     * The Total.
     */
    var total: Double,
    /**
     * The Importe.
     */
    var importe: Double,
    /**
     * The Desc.
     */
    var desc: Double,
    /**
     * The Desc tipo.
     */
    var descTipo: Long,
    /**
     * The Desc id.
     */
    var descID: Long,
    /**
     * The Referencia.
     */
    var referencia: Long,
    /**
     * The Cerrada.
     */
    var cerrada: Int,
    /**
     * The Puedecerrar.
     */
    var puedecerrar: Long,
    /**
     * The Facturado.
     */
    var facturado: Int,
    /**
     * The Imprime.
     */
    var imprime: Int,
    /**
     * The Folio.
     */
    var folio: String,
    /**
     * The Numcomensales.
     */
    var numcomensales: String,
    /**
     * Type: Method.
     * Parent: Cuenta.
     * Name: setNumcomensales.
     *
     * @Description.
     * @EndDescription.
     * @param: numcomensales @PsiType:String.
     */
    var acc: Int,
    var productos: ArrayList<Producto>,
    var detalle:Detalle,
    var saldo2:Double
) {

    @JsonClass(generateAdapter = true)
    data class Detalle(
        var id_local: Int,
        var id_term: Int,
        var id_coma: Int,
        var id_pos: Int,
        var productos:HashMap<String,Productos>
    ){
        fun getTotOfPLU(plus:ArrayList<String>):Int{
            var tot=0
            for(plu in plus){
                if(productos.containsKey(plu))
                    tot+=productos[plu]!!.getSize()
            }
            return tot
        }

        fun containPLU(plus:ArrayList<String>):Boolean{
            Log.i("dddd",plus.toString())
            for(plu in plus){
                if (productos.containsKey(plu))
                    return true
            }
            return false
        }

        fun containPLUMIN(plus:ArrayList<String>, cantidad:Int):Boolean{
            for(plu in plus){
                if (productos.containsKey(plu))
                    if(productos[plu]!!.getProductMinCant(cantidad)!=null)
                        return true
            }
            return false
        }

        fun getMinPrice(plus:ArrayList<String>):Producto?{
            var productoMin: Producto? = null
            for(plu in plus){
                try{
                    if(productos.containsKey(plu)){
                        if(productoMin==null)
                            productoMin=productos[plu]!!.getProductMin()
                        if(productos[plu]!!.getProductMin()!!.precio_unit<productoMin!!.precio_unit)
                            productoMin=productos[plu]!!.getProductMin()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
            return productoMin
        }
        fun getMinPriceCANT(plus:ArrayList<String>,cantidad: Int):Producto?{
            var productoMin: Producto? = null
            for(plu in plus){
                if(productos.containsKey(plu)){
                    try {
                        if(productos[plu]!!.getProductMinCant(cantidad)!!.m_total>0){
                            if(productoMin==null )
                                productoMin=productos[plu]!!.getProductMinCant(cantidad)
                            if(productos[plu]!!.getProductMinCant(cantidad)!!.precio_unit<productoMin!!.precio_unit)
                                productoMin=productos[plu]!!.getProductMinCant(cantidad)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }


                }
            }
            return productoMin
        }
    }




    override fun toString(): String {
        return ToStringBuilder(this).append("fECHA", fecha).append("iDLOCAL", idLocal)
            .append("iDCOMA", idComa).append("iDPOS", idPos).append("sALDO", saldo)
            .append("mTOTAL", total).append("mIMPORTE", importe).append("mDESC", desc)
            .append("dESCTIPO", descTipo).append("dESCID", descID).append("rEFERENCIA", referencia)
            .append("cERRADA", cerrada).append("pUEDECERRAR", puedecerrar)
            .append("fACTURADA", facturado).append("iMPRIME", imprime).append("fOLIOCTA", folio)
            .append("nUMCOMENSALES", numcomensales).toString()
    }
}