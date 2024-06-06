package com.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Type: Class.
 * Access: Public.
 * Name: FormasDePago.
 *
 * @Description.
 * @EndDescription.
 */
public class FormasDePago implements Parcelable {

    /**
     * The Tipo fpgo.
     */
    private int tipoFpgo;
    /**
     * The Id fpgo.
     */
    private int IdFpgo;
    /**
     * The Forma pago.
     */
    private String formaPago;
    /**
     * The Tipo cambio.
     */
    private int tipoCambio;
    /**
     * The Comision.
     */
    private String comision;
    /**
     * The Tipo comision.
     */
    private String tipoComision;
    /**
     * The Propina.
     */
    private String propina;

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: FormasDePago.
     *
     * @param tipoFpgo     @PsiType:int.
     * @param idFpgo       @PsiType:int.
     * @param formaPago    @PsiType:String.
     * @param tipoCambio   @PsiType:int.
     * @param comision     @PsiType:String.
     * @param tipoComision @PsiType:String.
     * @param propina      @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public FormasDePago(int tipoFpgo, int idFpgo, String formaPago, int tipoCambio, String comision, String tipoComision, String propina) {
        this.tipoFpgo = tipoFpgo;
        IdFpgo = idFpgo;
        this.formaPago = formaPago;
        this.tipoCambio = tipoCambio;
        this.comision = comision;
        this.tipoComision = tipoComision;
        this.propina = propina;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: FormasDePago.
     *
     * @param in @PsiType:Parcel.
     * @Description.
     * @EndDescription.
     */
    protected FormasDePago(Parcel in) {
        tipoFpgo = in.readInt();
        IdFpgo = in.readInt();
        formaPago = in.readString();
        tipoCambio = in.readInt();
        comision = in.readString();
        tipoComision = in.readString();
        propina = in.readString();
    }

    /**
     * The constant CREATOR.
     */
    public static final Creator<FormasDePago> CREATOR = new Creator<FormasDePago>() {
        @Override
        public FormasDePago createFromParcel(Parcel in) {
            return new FormasDePago(in);
        }

        @Override
        public FormasDePago[] newArray(int size) {
            return new FormasDePago[size];
        }
    };

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: getTipoFpgo.
     *
     * @return the tipo fpgo
     * @Description.
     * @EndDescription.
     */
    public int getTipoFpgo() {
        return tipoFpgo;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: setTipoFpgo.
     *
     * @Description.
     * @EndDescription.
     * @param: tipoFpgo @PsiType:int.
     */
    public void setTipoFpgo(int tipoFpgo) {
        this.tipoFpgo = tipoFpgo;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: getIdFpgo.
     *
     * @return the id fpgo
     * @Description.
     * @EndDescription.
     */
    public int getIdFpgo() {
        return IdFpgo;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: setIdFpgo.
     *
     * @Description.
     * @EndDescription.
     * @param: idFpgo @PsiType:int.
     */
    public void setIdFpgo(int idFpgo) {
        IdFpgo = idFpgo;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
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
     * Parent: FormasDePago.
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
     * Parent: FormasDePago.
     * Name: getTipoCambio.
     *
     * @return the tipo cambio
     * @Description.
     * @EndDescription.
     */
    public int getTipoCambio() {
        return tipoCambio;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: setTipoCambio.
     *
     * @Description.
     * @EndDescription.
     * @param: tipoCambio @PsiType:int.
     */
    public void setTipoCambio(int tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: getComision.
     *
     * @return the comision
     * @Description.
     * @EndDescription.
     */
    public String getComision() {
        return comision;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: setComision.
     *
     * @Description.
     * @EndDescription.
     * @param: comision @PsiType:String.
     */
    public void setComision(String comision) {
        this.comision = comision;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: getTipoComision.
     *
     * @return the tipo comision
     * @Description.
     * @EndDescription.
     */
    public String getTipoComision() {
        return tipoComision;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: setTipoComision.
     *
     * @Description.
     * @EndDescription.
     * @param: tipoComision @PsiType:String.
     */
    public void setTipoComision(String tipoComision) {
        this.tipoComision = tipoComision;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: getPropina.
     *
     * @return the propina
     * @Description.
     * @EndDescription.
     */
    public String getPropina() {
        return propina;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: setPropina.
     *
     * @Description.
     * @EndDescription.
     * @param: propina @PsiType:String.
     */
    public void setPropina(String propina) {
        this.propina = propina;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: toString.
     *
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    @Override
    public String toString() {
        return "FormasDePago{" +
                "tipoFpgo=" + tipoFpgo +
                ", IdFpgo=" + IdFpgo +
                ", formaPago='" + formaPago + '\'' +
                ", tipoCambio=" + tipoCambio +
                ", comision='" + comision + '\'' +
                ", tipoComision='" + tipoComision + '\'' +
                ", propina='" + propina + '\'' +
                '}';
    }


    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: describeContents.
     *
     * @return int
     * @Description.
     * @EndDescription. int.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Type: Method.
     * Parent: FormasDePago.
     * Name: writeToParcel.
     *
     * @param dest  @PsiType:Parcel.
     * @param flags @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(flags);
    }

}
