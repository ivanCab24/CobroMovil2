package com.DataModel

data class Productos(
    var productos: ArrayList<Producto>
) {
    fun getSize():Int{
        var cantidad = 0
        for(producto in productos){
            cantidad+=producto.porciones
        }
        return cantidad
    }
    fun getProductMin():Producto?{
        var productoMin:Producto?=null
        for (producto in productos ){
           if(producto.m_total>0){
               if(productoMin==null)
                   productoMin=producto
               if(producto.m_total<productoMin.m_total)
                   productoMin = producto
           }
        }
        return productoMin
    }
    fun getProductMinCant(cantidad:Int):Producto?{
        var productoMin:Producto?=null
        for (producto in productos ){
            if(producto.m_total>0){
                if(productoMin==null && producto.porciones>=cantidad)
                    productoMin=producto
                try{
                    if(producto.m_total<productoMin!!.m_total && producto.porciones>=cantidad)
                        productoMin = producto
                }catch (e:Exception){

                }
            }


        }
        return productoMin
    }
}