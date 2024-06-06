package com.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Type: Class.
 * Access: Public.
 * Name: SaleVtol.
 *
 * @Description.
 * @EndDescription.
 */
public class SaleVtol implements Parcelable {

    /**
     * The Codigo.
     */
    private String codigo;
    /**
     * The Campo 22.
     */
    private String campo22;
    /**
     * The Campo 25.
     */
    private String campo25;
    /**
     * The Campo 27.
     */
    private String campo27;
    /**
     * The Campo 32.
     */
    private String campo32;
    /**
     * The Campo 24.
     */
    private String campo24;
    /**
     * The Campo 31.
     */
    private String campo31;
    /**
     * The Campo 33.
     */
    private String campo33;
    /**
     * The Campo 94.
     */
    private String campo94;
    /**
     * The Puntos anterior.
     */
    private String puntosAnterior;
    /**
     * The Puntos disponibles.
     */
    private String puntosDisponibles;
    /**
     * The Puntos redimidos.
     */
    private String puntosRedimidos;
    /**
     * The Qps.
     */
    private String QPS;
    /**
     * The Campo 28.
     */
    private String campo28;
    /**
     * The Campo 58.
     */
    private String campo58;
    /**
     * The Campo 1022.
     */
    private String campo1022;

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: SaleVtol.
     *
     * @param codigo            @PsiType:String.
     * @param campo22           @PsiType:String.
     * @param campo25           @PsiType:String.
     * @param campo27           @PsiType:String.
     * @param campo32           @PsiType:String.
     * @param campo24           @PsiType:String.
     * @param campo31           @PsiType:String.
     * @param campo33           @PsiType:String.
     * @param campo94           @PsiType:String.
     * @param puntosAnterior    @PsiType:String.
     * @param puntosDisponibles @PsiType:String.
     * @param puntosRedimidos   @PsiType:String.
     * @param QPS               @PsiType:String.
     * @param campo28           @PsiType:String.
     * @param campo58           @PsiType:String.
     * @param campo1022         @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public SaleVtol(String codigo, String campo22, String campo25, String campo27, String campo32, String campo24, String campo31, String campo33, String campo94, String puntosAnterior, String puntosDisponibles, String puntosRedimidos, String QPS, String campo28, String campo58, String campo1022) {

        this.codigo = codigo;
        this.campo22 = campo22;
        this.campo25 = campo25;
        this.campo27 = campo27;
        this.campo32 = campo32;
        this.campo24 = campo24;
        this.campo31 = campo31;
        this.campo33 = campo33;
        this.campo94 = campo94;
        this.puntosAnterior = puntosAnterior;
        this.puntosDisponibles = puntosDisponibles;
        this.puntosRedimidos = puntosRedimidos;
        this.QPS = QPS;
        this.campo28 = campo28;
        this.campo58 = campo58;
        this.campo1022 = campo1022;

    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: SaleVtol.
     *
     * @param in @PsiType:Parcel.
     * @Description.
     * @EndDescription.
     */
    protected SaleVtol(Parcel in) {
        codigo = in.readString();
        campo22 = in.readString();
        campo25 = in.readString();
        campo27 = in.readString();
        campo32 = in.readString();
        campo24 = in.readString();
        campo31 = in.readString();
        campo33 = in.readString();
        campo94 = in.readString();
        puntosAnterior = in.readString();
        puntosDisponibles = in.readString();
        puntosRedimidos = in.readString();
        QPS = in.readString();
        campo28 = in.readString();
        campo58 = in.readString();
        campo1022 = in.readString();
    }

    /**
     * The constant CREATOR.
     */
    public static final Creator<SaleVtol> CREATOR = new Creator<SaleVtol>() {
        @Override
        public SaleVtol createFromParcel(Parcel in) {
            return new SaleVtol(in);
        }

        @Override
        public SaleVtol[] newArray(int size) {
            return new SaleVtol[size];
        }
    };

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCodigo.
     *
     * @return the codigo
     * @Description.
     * @EndDescription.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCodigo.
     *
     * @Description.
     * @EndDescription.
     * @param: codigo @PsiType:String.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo22.
     *
     * @return the campo 22
     * @Description.
     * @EndDescription.
     */
    public String getCampo22() {
        return campo22;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo22.
     *
     * @Description.
     * @EndDescription.
     * @param: campo22 @PsiType:String.
     */
    public void setCampo22(String campo22) {
        this.campo22 = campo22;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo25.
     *
     * @return the campo 25
     * @Description.
     * @EndDescription.
     */
    public String getCampo25() {
        return campo25;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo25.
     *
     * @Description.
     * @EndDescription.
     * @param: campo25 @PsiType:String.
     */
    public void setCampo25(String campo25) {
        this.campo25 = campo25;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo27.
     *
     * @return the campo 27
     * @Description.
     * @EndDescription.
     */
    public String getCampo27() {
        return campo27;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo27.
     *
     * @Description.
     * @EndDescription.
     * @param: campo27 @PsiType:String.
     */
    public void setCampo27(String campo27) {
        this.campo27 = campo27;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo32.
     *
     * @return the campo 32
     * @Description.
     * @EndDescription.
     */
    public String getCampo32() {
        return campo32;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo32.
     *
     * @Description.
     * @EndDescription.
     * @param: campo32 @PsiType:String.
     */
    public void setCampo32(String campo32) {
        this.campo32 = campo32;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo24.
     *
     * @return the campo 24
     * @Description.
     * @EndDescription.
     */
    public String getCampo24() {
        return campo24;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo24.
     *
     * @Description.
     * @EndDescription.
     * @param: campo24 @PsiType:String.
     */
    public void setCampo24(String campo24) {
        this.campo24 = campo24;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo31.
     *
     * @return the campo 31
     * @Description.
     * @EndDescription.
     */
    public String getCampo31() {
        return campo31;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo31.
     *
     * @Description.
     * @EndDescription.
     * @param: campo31 @PsiType:String.
     */
    public void setCampo31(String campo31) {
        this.campo31 = campo31;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo33.
     *
     * @return the campo 33
     * @Description.
     * @EndDescription.
     */
    public String getCampo33() {
        return campo33;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo33.
     *
     * @Description.
     * @EndDescription.
     * @param: campo33 @PsiType:String.
     */
    public void setCampo33(String campo33) {
        this.campo33 = campo33;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo94.
     *
     * @return the campo 94
     * @Description.
     * @EndDescription.
     */
    public String getCampo94() {
        return campo94;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo94.
     *
     * @Description.
     * @EndDescription.
     * @param: campo94 @PsiType:String.
     */
    public void setCampo94(String campo94) {
        this.campo94 = campo94;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getPuntosAnterior.
     *
     * @return the puntos anterior
     * @Description.
     * @EndDescription.
     */
    public String getPuntosAnterior() {
        return puntosAnterior;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setPuntosAnterior.
     *
     * @Description.
     * @EndDescription.
     * @param: puntosAnterior @PsiType:String.
     */
    public void setPuntosAnterior(String puntosAnterior) {
        this.puntosAnterior = puntosAnterior;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getPuntosDisponibles.
     *
     * @return the puntos disponibles
     * @Description.
     * @EndDescription.
     */
    public String getPuntosDisponibles() {
        return puntosDisponibles;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setPuntosDisponibles.
     *
     * @Description.
     * @EndDescription.
     * @param: puntosDisponibles @PsiType:String.
     */
    public void setPuntosDisponibles(String puntosDisponibles) {
        this.puntosDisponibles = puntosDisponibles;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getPuntosRedimidos.
     *
     * @return the puntos redimidos
     * @Description.
     * @EndDescription.
     */
    public String getPuntosRedimidos() {
        return puntosRedimidos;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setPuntosRedimidos.
     *
     * @Description.
     * @EndDescription.
     * @param: puntosRedimidos @PsiType:String.
     */
    public void setPuntosRedimidos(String puntosRedimidos) {
        this.puntosRedimidos = puntosRedimidos;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getQPS.
     *
     * @return the qps
     * @Description.
     * @EndDescription.
     */
    public String getQPS() {
        return QPS;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setQPS.
     *
     * @Description.
     * @EndDescription.
     * @param: QPS @PsiType:String.
     */
    public void setQPS(String QPS) {
        this.QPS = QPS;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo28.
     *
     * @return the campo 28
     * @Description.
     * @EndDescription.
     */
    public String getCampo28() {
        return campo28;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo28.
     *
     * @Description.
     * @EndDescription.
     * @param: campo28 @PsiType:String.
     */
    public void setCampo28(String campo28) {
        this.campo28 = campo28;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo58.
     *
     * @return the campo 58
     * @Description.
     * @EndDescription.
     */
    public String getCampo58() {
        return campo58;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo58.
     *
     * @Description.
     * @EndDescription.
     * @param: campo58 @PsiType:String.
     */
    public void setCampo58(String campo58) {
        this.campo58 = campo58;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: getCampo1022.
     *
     * @return the campo 1022
     * @Description.
     * @EndDescription.
     */
    public String getCampo1022() {
        return campo1022;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: setCampo1022.
     *
     * @Description.
     * @EndDescription.
     * @param: campo1022 @PsiType:String.
     */
    public void setCampo1022(String campo1022) {
        this.campo1022 = campo1022;
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
     * Name: toString.
     *
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    @Override
    public String toString() {
        return "SaleVtol{" +
                "campo22='" + campo22 + '\'' +
                ", campo25='" + campo25 + '\'' +
                ", campo27='" + campo27 + '\'' +
                ", campo32='" + campo32 + '\'' +
                ", campo24='" + campo24 + '\'' +
                ", campo31='" + campo31 + '\'' +
                ", campo33='" + campo33 + '\'' +
                ", campo94='" + campo94 + '\'' +
                ", puntosAnterior='" + puntosAnterior + '\'' +
                ", puntosDisponibles='" + puntosDisponibles + '\'' +
                ", puntosRedimidos='" + puntosRedimidos + '\'' +
                ", QPS='" + QPS + '\'' +
                ", campo28='" + campo28 + '\'' +
                ", campo58='" + campo58 + '\'' +
                ", campo1022='" + campo1022 + '\'' +
                '}';
    }

    /**
     * Type: Method.
     * Parent: SaleVtol.
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
     * Parent: SaleVtol.
     * Name: writeToParcel.
     *
     * @param parcel @PsiType:Parcel.
     * @param i      @PsiType:int.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(i);
    }
}
