package com.Presenter

import android.app.Activity
import android.text.Html
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Controller.Adapters.CuponAdapter
import com.Controller.Adapters.RecyclerItemTouchHelper
import com.Controller.Adapters.RecyclerItemTouchHelperListener
import com.Controller.Adapters.SeleccionAdapter
import com.DataModel.Miembro
import com.DataModel.Producto
import com.Interfaces.CuponesDescuentosMVP
import com.Utilities.Utilities
import com.View.Dialogs.DialogCuponesDescuentos
import com.View.Dialogs.DialogCuponesDescuentos.Companion.cuponesAplicados
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.DialogCuponesDescuentosBinding
import javax.inject.Inject

class CuponesDescuentosPresenter @Inject constructor(private val model: CuponesDescuentosMVP.Model):CuponesDescuentosMVP.Presenter {
    var cuenta = ContentFragment.cuenta
    var carritoAdapter: CuponAdapter? = null
    var seleccionAdapter: SeleccionAdapter?=null
    var binding: DialogCuponesDescuentosBinding? = null
    var activity:Activity?=null
    var recyclerViewLista: RecyclerView? = null
    var view: View? = null
    var dialog:DialogCuponesDescuentos?=null

    override fun checkCupones():Boolean{
        var desc = 0
        for(cupon in DialogCuponesDescuentos.items){
            if(cupon.restricciones.tipoCupon=="LOY_TDP_DESC_PORCENTAJE" || cupon.restricciones.tipoCupon=="LOY_TDP_DESC_PESOS")
                desc++
        }
        return desc>=2
    }

    override fun generaDescuento(cupon: Miembro.Response.Cupones){
        Log.i("Productos:", cuenta.detalle.toString())
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_PRECIO_PREFERENCIAL"){
            val productoDes = cuenta.detalle.getMinPrice(cupon.restricciones.plusParticipantes.toWords())
            cupon.descuentoM = productoDes!!.precio_unit-cupon.restricciones.precioPreferencial
            DialogCuponesDescuentos.cuponesAplicados[cupon]=productoDes
            //cupon.producto = productoDes
        }
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_2_X_1"){
            val productoDes = cuenta.detalle.getMinPrice(cupon.restricciones.plusParticipantes.toWords())
            cupon.descuentoM = productoDes!!.m_total/productoDes!!.porciones

            DialogCuponesDescuentos.cuponesAplicados[cupon]=productoDes

            //cupon.producto = productoDes
        }
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_DESC_PRODUCTO"){
            val productoDes = cuenta.detalle.getMinPriceCANT(cupon.restricciones.plusParticipantes.toWords(),cupon.restricciones.cantidadProductos)
            if(cupon.restricciones.descuentoPorcentaje>0)
                cupon.descuentoM =  productoDes!!.precio_unit*cupon.restricciones.cantidadProductos * (cupon.restricciones.descuentoPorcentaje/100)
            if(cupon.restricciones.precioPreferencial>0)
                cupon.descuentoM =  productoDes!!.precio_unit - cupon.restricciones.precioPreferencial
            //cupon.producto = productoDes!!
            DialogCuponesDescuentos.cuponesAplicados[cupon]=productoDes!!
        }
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_DESC_CUENTA"){
            if (cupon.restricciones.tipoCupon=="LOY_TDP_DESC_PORCENTAJE"){
                cupon.descuentoM = cuenta.saldo2* ((cupon.restricciones.descuentoPorcentaje/100))
                DialogCuponesDescuentos.cuponesAplicados[cupon]= Producto("0","dummy",0.0,0,0.0,0,0,"","","")
            }
            if (cupon.restricciones.tipoCupon=="LOY_TDP_DESC_PESOS"){
                cupon.descuentoM = cupon.restricciones.descuentoMonto.toDouble()
                DialogCuponesDescuentos.cuponesAplicados[cupon]= Producto("0","dummy",0.0,0,0.0,0,0,"","","")
            }
        }
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_3_X_2"){
            val productoDes = cuenta.detalle.getMinPrice(cupon.restricciones.plusParticipantes.toWords())
            cupon.descuentoM = productoDes!!.precio_unit
            //cupon.producto = productoDes
            DialogCuponesDescuentos.cuponesAplicados[cupon]=productoDes
        }

        cupon.descuentoM=cupon.descuentoM.round(2)
        cuenta.saldo2-=cupon.descuentoM
        cuenta.saldo2=cuenta.saldo2.round(2)
        DialogCuponesDescuentos.items.add(cupon)
    }

    override fun genMontoDesc(cupon: Miembro.Response.Cupones){
        cupon.descuentoM = 0.0
        try{
            if(cupon.restricciones.subTipoCupon=="LOY_SDP_PRECIO_PREFERENCIAL"){
                val productoDes = cuenta.detalle.getMinPrice(cupon.restricciones.plusParticipantes.toWords())
                cupon.descuentoM = productoDes!!.precio_unit-cupon.restricciones.precioPreferencial
            }

            if(cupon.restricciones.subTipoCupon=="LOY_SDP_2_X_1" && cuenta.detalle.getTotOfPLU(cupon.restricciones.plusParticipantes.toWords())>=2){
                val productoDes = cuenta.detalle.getMinPrice(cupon.restricciones.plusParticipantes.toWords())
                cupon.descuentoM = productoDes!!.precio_unit
            }
            if(cupon.restricciones.subTipoCupon=="LOY_SDP_DESC_PRODUCTO"){
                val productoDes = cuenta.detalle.getMinPriceCANT(cupon.restricciones.plusParticipantes.toWords(), cupon.restricciones.cantidadProductos)

                if(cupon.restricciones.descuentoPorcentaje>0)
                    cupon.descuentoM =  productoDes!!.precio_unit * cupon.restricciones.cantidadProductos * (cupon.restricciones.descuentoPorcentaje/100)
                if(cupon.restricciones.precioPreferencial>0)
                    cupon.descuentoM =  productoDes!!.precio_unit - cupon.restricciones.precioPreferencial
            }
            if(cupon.restricciones.subTipoCupon=="LOY_SDP_DESC_CUENTA"){
                if (cupon.restricciones.tipoCupon=="LOY_TDP_DESC_PORCENTAJE")
                    cupon.descuentoM = cuenta.saldo2*(cupon.restricciones.descuentoPorcentaje/100)
                if (cupon.restricciones.tipoCupon=="LOY_TDP_DESC_PESOS")
                    cupon.descuentoM = cupon.restricciones.descuentoMonto.toDouble()
            }
            if(cupon.restricciones.subTipoCupon=="LOY_SDP_3_X_2"){
                val productoDes = cuenta.detalle.getMinPrice(cupon.restricciones.plusParticipantes.toWords())
                cupon.descuentoM = productoDes!!.precio_unit
            }
            if(cupon.restricciones.subTipoCupon=="LOY_SDP_3_X_2"){
                val productoDes = cuenta.detalle.getMinPrice(cupon.restricciones.plusParticipantes.toWords())
                cupon.descuentoM = productoDes!!.precio_unit
            }
            if(cupon.restricciones.compraMinima>=cuenta.saldo2){
                cupon.descuentoM = 0.0
            }

            cupon.descuentoM=cupon.descuentoM.round(2)
        }catch(e:Exception){
            cupon.descuentoM = 0.0
        }
    }



    override fun validaCupon(cupon: Miembro.Response.Cupones?):Boolean{
        if(DialogCuponesDescuentos.items.size>1 ){
            UserInteraction.snackyWarning(null,view,"Solo se pueden redimir 2 cupones")
            return false
        }

        if(DialogCuponesDescuentos.items.filter { it!!.restricciones.subTipoCupon == "LOY_SDP_DESC_CUENTA" }
                .isNotEmpty()){
            UserInteraction.snackyWarning(null,view,"Ya hay un cupón de descuento en cuenta seleccionado")
            return false
        }
        if(cupon!!.restricciones.canalAplicacion!="LOY_CANAL_TODOS"){
            UserInteraction.snackyWarning(null,view,"Este cupón no se puede aplicar por este canal")
            return false
        }
        if(checkCupones()){
            UserInteraction.snackyWarning(null,view,"Solo se pueede añadir un cupon de descuento")
            return false
        }
        if(cupon!!.restricciones.compraMinima>cuenta.saldo2){
            UserInteraction.snackyWarning(null,view,"El cupon seleccionado no cumple con la compra minima")
            return false
        }
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_PRECIO_PREFERENCIAL"&&!cuenta.detalle.containPLU(cupon.restricciones.plusParticipantes.toWords())){
            UserInteraction.snackyWarning(null,view,"La cuenta no tiene el producto del cupón o los productos se encuentran separados")
            return false
        }
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_2_X_1"&&cuenta.detalle.getTotOfPLU(cupon.restricciones.plusParticipantes.toWords())<2){
            UserInteraction.snackyWarning(null,view,"La cuenta no contiene los productos para aplicar el cupón")
            return false
        }
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_DESC_PRODUCTO"&&!cuenta.detalle.containPLUMIN(cupon.restricciones.plusParticipantes.toWords(),cupon.restricciones.cantidadProductos)){
            UserInteraction.snackyWarning(null,view,"La cuenta no tiene el producto del cupón o los productos se encuentran separados")
            return false
        }
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_DESC_CUENTA"){

        }
        if(cupon.restricciones.subTipoCupon=="LOY_SDP_3_X_2"&&cuenta.detalle.getTotOfPLU(cupon.restricciones.plusParticipantes.toWords())<3){
            UserInteraction.snackyWarning(null,view,"La cuenta no contiene los productos para aplicar el cupón")
            return false
        }
        return true
    }

    override fun showMessage(message: String) {
        UserInteraction.snackyWarning(null,view,message)
    }

    override fun enviaCupones() {
        UserInteraction.showWaitingDialog(ContentFragment.contentFragment!!.fragmentManager2, "Aplicando Descuento")
        try{
            for(i in 0 until cuponesAplicados.keys.size){
                var cup:Miembro.Response.Cupones = cuponesAplicados.keys.elementAt(i)
                var prod:Producto? = cuponesAplicados.values.elementAt(i)
                if(cup.restricciones.subTipoCupon!="LOY_SDP_DESC_CUENTA"){
                    try{
                        Log.i("ssss",prod.toString())
                        model.executeSendCupones(cup, prod!!)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                else{
                    if(cup.numeroCupon!= "corporativo" && cup.numeroCupon!= "inapam"){
                        if (cup.restricciones.tipoCupon=="LOY_TDP_DESC_PORCENTAJE")
                            ContentFragment.descuentoPorcentaje.porcentaje = cup.restricciones.descuentoPorcentaje.toDouble()
                        if (cup.restricciones.tipoCupon=="LOY_TDP_DESC_PESOS") {
                            ContentFragment.descuentoPorcentaje.porcentaje = cup.restricciones.descuentoMonto.toDouble()*100/ContentFragment.cuenta.saldo
                        }
                        ContentFragment.descuentoPorcentaje.textReferencia = "${ContentFragment.miembro!!.response.membresia!!.numeroMembresia}-${cup.numeroCupon}"
                        ContentFragment.descuentoPorcentaje.descuento = cup.restricciones.plusParticipantes
                        ContentFragment.descuentoPorcentaje.idDesc = 996
                        ContentFragment.descuentoPorcentaje.descuento = ContentFragment.nipA+"-"+ ContentFragment.descuentoPorcentaje.descuento
                        ContentFragment.descuentoPorcentaje.descripcion = "CUPON A COMER"
                        ContentFragment.getDescuento = ContentFragment.descuentoPorcentaje
                        ContentFragment.contentFragment!!.redeem(ContentFragment.getDescuento!!.textReferencia)
                    }else{
                        var descuentos_obj = ContentFragment.catalogoDescuentosSelect!!.filter { it!!.idDesc==cup.tipoBeneficio.toInt() && it.tipo==cup.eventoBeneficio.toInt() }
                        for(des in descuentos_obj){
                            ContentFragment.getDescuento = des
                            ContentFragment.getDescuento!!.idDesc=des!!.idDesc
                            if(des.tipo==4)
                                ContentFragment.contentFragment!!.redeem(ContentFragment.miembro!!.response.membresia!!.inapam)
                            else
                                ContentFragment.contentFragment!!.redeem(ContentFragment.miembro!!.response.membresia!!.estafeta)
                        }
                    }

                }
            }
        }catch (e:Exception){
            UserInteraction.snackyFail(activity,null,e.message)
        }
        dialog!!.dismiss()
        UserInteraction.getDialogWaiting.dismiss()
        ContentFragment.contentFragment!!.presenter!!.buscarMesa()
    }

    override fun procesaSeleccion(){
        for(cupon in DialogCuponesDescuentos.cuponesArray!!){
            genMontoDesc(cupon!!)
        }
        for(cupon in DialogCuponesDescuentos.descuentosArray!!){
            genMontoDesc(cupon!!)
        }
        sort(DialogCuponesDescuentos.cuponesArray!!)
        sort(DialogCuponesDescuentos.descuentosArray!!)

        generaCuenta()

        binding!!.recyclerViewDescuentos.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding!!.recyclerViewDescuentos.setHasFixedSize(true)
        carritoAdapter = CuponAdapter(activity, DialogCuponesDescuentos.descuentosArray,"0",null,dialog,null)
        carritoAdapter!!.notifyDataSetChanged()
        binding!!.recyclerViewDescuentos.adapter = carritoAdapter

        binding!!.recyclerViewCupones.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding!!.recyclerViewCupones.setHasFixedSize(true)
        carritoAdapter = CuponAdapter(activity, DialogCuponesDescuentos.cuponesArray,"0",null,dialog,null)
        carritoAdapter!!.notifyDataSetChanged()
        binding!!.recyclerViewCupones.adapter = carritoAdapter

        if(DialogCuponesDescuentos.descuentosArray!!.isEmpty())
            binding!!.messageDescuentos.visibility= View.VISIBLE
        else
            binding!!.messageDescuentos.visibility= View.GONE
        if(DialogCuponesDescuentos.cuponesArray!!.isEmpty())
            binding!!.messageCupones.visibility= View.VISIBLE
        else
            binding!!.messageCupones.visibility= View.GONE

        seleccionAdapter = SeleccionAdapter(activity, DialogCuponesDescuentos.items,"0", Utilities.preferenceHelper)

        recyclerViewLista = view!!.findViewById(R.id.recyclerViewAplicados)
        recyclerViewLista!!.layoutManager = LinearLayoutManager(activity,
            RecyclerView.VERTICAL,false)
        recyclerViewLista!!.itemAnimator= DefaultItemAnimator()
        recyclerViewLista!!.adapter = seleccionAdapter
        if(DialogCuponesDescuentos.items.isEmpty())
            binding!!.messageSeleccion.visibility= View.VISIBLE
        else
            binding!!.messageSeleccion.visibility= View.GONE
        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT
            ,object: RecyclerItemTouchHelperListener {
                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder?,
                    direction: Int,
                    position: Int
                ) {
                    var cuponS = DialogCuponesDescuentos.items[position]
                    if(cuponS.restricciones.tipoCupon=="LOY_TDP_PRODUCTO") {
                        DialogCuponesDescuentos.cuponesArray!!.add(cuponS)
                        cuenta.saldo2+=cuponS.descuentoM
                        DialogCuponesDescuentos.cuponesAplicados.remove(cuponS)
                        DialogCuponesDescuentos.items.removeAt(position)
                        sort(DialogCuponesDescuentos.cuponesArray!!)
                        sort(DialogCuponesDescuentos.descuentosArray!!)
                        procesaSeleccion()
                    }

                    else{
                        if((cuponS.numeroCupon== "corporativo" || cuponS.numeroCupon== "inapam")&&DialogCuponesDescuentos.descuentosArray!!.filter { it!!.descuentoM > cuponS.descuentoM }
                                .isNotEmpty()){
                            DialogCuponesDescuentos.descuentosArray!!.add(cuponS)
                            cuenta.saldo2+=cuponS.descuentoM
                            DialogCuponesDescuentos.cuponesAplicados.remove(cuponS)
                            DialogCuponesDescuentos.items.removeAt(position)
                            sort(DialogCuponesDescuentos.cuponesArray!!)
                            sort(DialogCuponesDescuentos.descuentosArray!!)
                            procesaSeleccion()
                        }
                        else{
                            DialogCuponesDescuentos.descuentosArray!!.add(cuponS)
                            cuenta.saldo2+=cuponS.descuentoM
                            DialogCuponesDescuentos.cuponesAplicados.remove(cuponS)
                            DialogCuponesDescuentos.items.removeAt(position)
                            sort(DialogCuponesDescuentos.cuponesArray!!)
                            sort(DialogCuponesDescuentos.descuentosArray!!)
                            procesaSeleccion()
                        }
                    }


                }
            })
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewLista)
    }

    override fun generaCuenta(){
        val ticket = StringBuilder()
        var espacios = 10
        for(producto in ContentFragment.cuenta.productos){
            if("${producto.porciones} ${producto.producto}".length>espacios)
                espacios = "${producto.porciones} ${producto.producto}".length
        }
        espacios+=7
        for(producto in ContentFragment.cuenta.productos){
            ticket.append("<p>${producto.porciones} ${producto.producto}")
            for (i in 1..(espacios-"${producto.porciones} ${producto.producto}".length)) {
                ticket.append("&nbsp;")
            }
            ticket.append("${producto.m_total} </p>")
        }
        ticket.append("<p>")
        for (i in 1..espacios) {
            ticket.append("-")
        }
        ticket.append("</p>")
        for (cupon in DialogCuponesDescuentos.items) {
            ticket.append("<p>1 ${if(cupon.descripcion.length>espacios) {
                "${cupon.descripcion.substring(0, espacios-5)}..."
            }else cupon.descripcion}")
            for (i in 1..(espacios-"1 ${cupon.descripcion}".length)) {
                ticket.append("&nbsp;")
            }
            ticket.append("${cupon.descuentoM} </p>")
        }
        binding!!.detalleCuenta.text= Html.fromHtml(ticket.toString())
        binding!!.totalCuenta.text="Total: $${cuenta.saldo2}"

        //UserInteraction.snackyWarning(null,view,"La cuenta no contiene los productos para aplicar el cupón")

        binding!!.textView16.text = ContentFragment.miembro!!.response.membresia!!.nombre+" "+ ContentFragment.miembro!!.exceptionMessage

    }

    override fun setViewV(view: View) {
        this.view=view
        this.cuenta = ContentFragment.cuenta
    }

    override fun  getViewV():View {
        return this.view!!

    }

    override fun setDialogV(dialog: DialogCuponesDescuentos) {
        this.dialog=dialog
    }

    override fun setBindingV(binding: DialogCuponesDescuentosBinding) {
        this.binding=binding
    }

    override fun setActivityV(activity: Activity) {
        this.activity = activity
    }


    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

    fun sort(list: ArrayList<Miembro.Response.Cupones?>) {
        list.sortWith(Comparator { o1, o2 ->
            (o2!!.descuentoM).compareTo(
                o1!!.descuentoM
            )
        })
    }

    inline fun String.toWords(): ArrayList<String> {
        val words = ArrayList<String>()
        for (w in this.trim(' ').split("/")) {
            if (w.isNotEmpty()) {
                words.add(w)
            }
        }
        return words
    }
}