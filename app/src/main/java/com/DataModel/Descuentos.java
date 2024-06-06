package com.DataModel;

/**
 * Type: Class.
 * Access: Public.
 * Name: Descuentos.
 *
 * @Description.
 * @EndDescription.
 */
public class Descuentos {

    /**
     * The Tipo.
     */
    private int tipo;
    /**
     * The Id desc.
     */
    private int idDesc;
    /**
     * The Descuento.
     */
    private String descuento;
    /**
     * The Descripcion.
     */
    private String descripcion;
    /**
     * The Porcentaje.
     */
    private double porcentaje;
    /**
     * The Text referencia.
     */
    private String textReferencia;
    /**
     * The Favorito.
     */
    private int favorito;

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: Descuentos.
     *
     * @param tipo           @PsiType:int.
     * @param idDesc         @PsiType:int.
     * @param descuento      @PsiType:String.
     * @param descripcion    @PsiType:String.
     * @param porcentaje     @PsiType:double.
     * @param textReferencia @PsiType:String.
     * @param favorito       @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    public Descuentos(int tipo, int idDesc, String descuento, String descripcion, double porcentaje, String textReferencia, int favorito) {
        this.tipo = tipo;
        this.idDesc = idDesc;
        this.descuento = descuento;
        this.descripcion = descripcion;
        this.porcentaje = porcentaje;
        this.textReferencia = textReferencia;
        this.favorito = favorito;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: getTipo.
     *
     * @return the tipo
     * @Description.
     * @EndDescription.
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: setTipo.
     *
     * @Description.
     * @EndDescription.
     * @param: tipo @PsiType:int.
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: getIdDesc.
     *
     * @return the id desc
     * @Description.
     * @EndDescription.
     */
    public int getIdDesc() {
        return idDesc;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: setIdDesc.
     *
     * @Description.
     * @EndDescription.
     * @param: idDesc @PsiType:int.
     */
    public void setIdDesc(int idDesc) {
        this.idDesc = idDesc;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: getDescuento.
     *
     * @return the descuento
     * @Description.
     * @EndDescription.
     */
    public String getDescuento() {
        return descuento;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: setDescuento.
     *
     * @Description.
     * @EndDescription.
     * @param: descuento @PsiType:String.
     */
    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: getDescripcion.
     *
     * @return the descripcion
     * @Description.
     * @EndDescription.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: setDescripcion.
     *
     * @Description.
     * @EndDescription.
     * @param: descripcion @PsiType:String.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: getPorcentaje.
     *
     * @return the porcentaje
     * @Description.
     * @EndDescription.
     */
    public double getPorcentaje() {
        return porcentaje;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: setPorcentaje.
     *
     * @Description.
     * @EndDescription.
     * @param: porcentaje @PsiType:double.
     */
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: getTextReferencia.
     *
     * @return the text referencia
     * @Description.
     * @EndDescription.
     */
    public String getTextReferencia() {
        return textReferencia;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: setTextReferencia.
     *
     * @Description.
     * @EndDescription.
     * @param: textReferencia @PsiType:String.
     */
    public void setTextReferencia(String textReferencia) {
        this.textReferencia = textReferencia;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: getFavorito.
     *
     * @return the favorito
     * @Description.
     * @EndDescription.
     */
    public int getFavorito() {
        return favorito;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: setFavorito.
     *
     * @Description.
     * @EndDescription.
     * @param: favorito @PsiType:int.
     */
    public void setFavorito(int favorito) {
        this.favorito = favorito;
    }

    /**
     * Type: Method.
     * Parent: Descuentos.
     * Name: toString.
     *
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    @Override
    public String toString() {
        return "Descuentos{" +
                "tipo=" + tipo +
                ", idDesc=" + idDesc +
                ", descuento='" + descuento + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", porcentaje=" + porcentaje +
                ", textReferencia='" + textReferencia + '\'' +
                ", favorito=" + favorito +
                '}';
    }
}
