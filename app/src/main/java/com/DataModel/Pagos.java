package com.DataModel;

/**
 * Type: Class.
 * Access: Public.
 * Name: Pagos.
 *
 * @Description.
 * @EndDescription.
 */
public class Pagos {

    /**
     * The Fecha.
     */
    private int fecha;
    /**
     * The Idlocal.
     */
    private int idlocal;
    /**
     * The Id term.
     */
    private int idTerm;
    /**
     * The Id coma.
     */
    private int idComa;
    /**
     * The Id pos.
     */
    private int idPos;
    /**
     * The Id fpgo.
     */
    private int idFpgo;
    /**
     * The Forma pago.
     */
    private String formaPago;
    /**
     * The Abono propina.
     */
    private double abonoPropina;
    /**
     * The Abono mn.
     */
    private double abonoMn;
    /**
     * The Contador.
     */
    private int contador;
    /**
     * The Cancelado.
     */
    private boolean cancelado;
    /**
     * The Ticket.
     */
    private String ticket;
    /**
     * The Tc autorizacion.
     */
    private String tc_autorizacion;
    /**
     * The Referencia.
     */
    private String referencia;

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: Pagos.
     *
     * @param fecha           @PsiType:int.
     * @param idlocal         @PsiType:int.
     * @param idTerm          @PsiType:int.
     * @param idComa          @PsiType:int.
     * @param idPos           @PsiType:int.
     * @param idFpgo          @PsiType:int.
     * @param formaPago       @PsiType:String.
     * @param abonoPropina    @PsiType:double.
     * @param abonoMn         @PsiType:double.
     * @param contador        @PsiType:int.
     * @param cancelado       @PsiType:boolean.
     * @param ticket          @PsiType:String.
     * @param tc_autorizacion @PsiType:String.
     * @param referencia      @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public Pagos(int fecha, int idlocal, int idTerm, int idComa, int idPos, int idFpgo, String formaPago, double abonoPropina, double abonoMn, int contador, boolean cancelado, String ticket, String tc_autorizacion, String referencia) {
        this.fecha = fecha;
        this.idlocal = idlocal;
        this.idTerm = idTerm;
        this.idComa = idComa;
        this.idPos = idPos;
        this.idFpgo = idFpgo;
        this.formaPago = formaPago;
        this.abonoPropina = abonoPropina;
        this.abonoMn = abonoMn;
        this.contador = contador;
        this.cancelado = cancelado;
        this.ticket = ticket;
        this.tc_autorizacion = tc_autorizacion;
        this.referencia = referencia;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getFecha.
     *
     * @return the fecha
     * @Description.
     * @EndDescription.
     */
    public int getFecha() {
        return fecha;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setFecha.
     *
     * @Description.
     * @EndDescription.
     * @param: fecha @PsiType:int.
     */
    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getIdlocal.
     *
     * @return the idlocal
     * @Description.
     * @EndDescription.
     */
    public int getIdlocal() {
        return idlocal;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setIdlocal.
     *
     * @Description.
     * @EndDescription.
     * @param: idlocal @PsiType:int.
     */
    public void setIdlocal(int idlocal) {
        this.idlocal = idlocal;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getIdTerm.
     *
     * @return the id term
     * @Description.
     * @EndDescription.
     */
    public int getIdTerm() {
        return idTerm;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setIdTerm.
     *
     * @Description.
     * @EndDescription.
     * @param: idTerm @PsiType:int.
     */
    public void setIdTerm(int idTerm) {
        this.idTerm = idTerm;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getIdComa.
     *
     * @return the id coma
     * @Description.
     * @EndDescription.
     */
    public int getIdComa() {
        return idComa;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setIdComa.
     *
     * @Description.
     * @EndDescription.
     * @param: idComa @PsiType:int.
     */
    public void setIdComa(int idComa) {
        this.idComa = idComa;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getIdPos.
     *
     * @return the id pos
     * @Description.
     * @EndDescription.
     */
    public int getIdPos() {
        return idPos;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setIdPos.
     *
     * @Description.
     * @EndDescription.
     * @param: idPos @PsiType:int.
     */
    public void setIdPos(int idPos) {
        this.idPos = idPos;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getIdFpgo.
     *
     * @return the id fpgo
     * @Description.
     * @EndDescription.
     */
    public int getIdFpgo() {
        return idFpgo;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setIdFpgo.
     *
     * @Description.
     * @EndDescription.
     * @param: idFpgo @PsiType:int.
     */
    public void setIdFpgo(int idFpgo) {
        this.idFpgo = idFpgo;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getFormaPago.
     *
     * @return the forma pago
     * @Description.
     * @EndDescription.
     */
    public String getFormaPago() {
        return formaPago;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setFormaPago.
     *
     * @Description.
     * @EndDescription.
     * @param: formaPago @PsiType:String.
     */
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getAbonoPropina.
     *
     * @return the abono propina
     * @Description.
     * @EndDescription.
     */
    public double getAbonoPropina() {
        return abonoPropina;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setAbonoPropina.
     *
     * @Description.
     * @EndDescription.
     * @param: abonoPropina @PsiType:double.
     */
    public void setAbonoPropina(double abonoPropina) {
        this.abonoPropina = abonoPropina;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getAbonoMn.
     *
     * @return the abono mn
     * @Description.
     * @EndDescription.
     */
    public double getAbonoMn() {
        return abonoMn;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setAbonoMn.
     *
     * @Description.
     * @EndDescription.
     * @param: abonoMn @PsiType:double.
     */
    public void setAbonoMn(double abonoMn) {
        this.abonoMn = abonoMn;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getContador.
     *
     * @return the contador
     * @Description.
     * @EndDescription.
     */
    public int getContador() {
        return contador;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setContador.
     *
     * @Description.
     * @EndDescription.
     * @param: contador @PsiType:int.
     */
    public void setContador(int contador) {
        this.contador = contador;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: isCancelado.
     *
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean isCancelado() {
        return cancelado;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setCancelado.
     *
     * @Description.
     * @EndDescription.
     * @param: cancelado @PsiType:boolean.
     */
    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getTicket.
     *
     * @return the ticket
     * @Description.
     * @EndDescription.
     */
    public String getTicket() {
        return ticket;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setTicket.
     *
     * @Description.
     * @EndDescription.
     * @param: ticket @PsiType:String.
     */
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getTc_autorizacion.
     *
     * @return the tc autorizacion
     * @Description.
     * @EndDescription.
     */
    public String getTc_autorizacion() {
        return tc_autorizacion;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setTc_autorizacion.
     *
     * @Description.
     * @EndDescription.
     * @param: tc_autorizacion @PsiType:String.
     */
    public void setTc_autorizacion(String tc_autorizacion) {
        this.tc_autorizacion = tc_autorizacion;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: getReferencia.
     *
     * @return the referencia
     * @Description.
     * @EndDescription.
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: setReferencia.
     *
     * @Description.
     * @EndDescription.
     * @param: referencia @PsiType:String.
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * Type: Method.
     * Parent: Pagos.
     * Name: toString.
     *
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    @Override
    public String toString() {
        return "Pagos{" +
                "fecha=" + fecha +
                ", idlocal=" + idlocal +
                ", idTerm=" + idTerm +
                ", idComa=" + idComa +
                ", idPos=" + idPos +
                ", idFpgo=" + idFpgo +
                ", formaPago='" + formaPago + '\'' +
                ", abonoPropina=" + abonoPropina +
                ", abonoMn=" + abonoMn +
                ", contador=" + contador +
                ", cancelado=" + cancelado +
                ", ticket='" + ticket + '\'' +
                ", tc_autorizacion='" + tc_autorizacion + '\'' +
                ", referencia='" + referencia + '\'' +
                '}';
    }
}
