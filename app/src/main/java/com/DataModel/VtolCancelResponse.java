package com.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Type: Class.
 * Access: Public.
 * Name: VtolCancelResponse.
 *
 * @Description.
 * @EndDescription.
 */
public class VtolCancelResponse implements Parcelable {

    /**
     * The Codigo.
     */
    String codigo;
    /**
     * The Campo 24.
     */
    String campo24;
    /**
     * The Campo 22.
     */
    String campo22;
    /**
     * The Campo 25.
     */
    String campo25;
    /**
     * The Campo 27.
     */
    String campo27;
    /**
     * The Campo 32.
     */
    String campo32;
    /**
     * The Campo 28.
     */
    String campo28;

    /**
     * Type: Method.
     * Parent: VtolCancelResponse.
     * Name: VtolCancelResponse.
     *
     * @param codigo  @PsiType:String.
     * @param campo24 @PsiType:String.
     * @param campo22 @PsiType:String.
     * @param campo25 @PsiType:String.
     * @param campo27 @PsiType:String.
     * @param campo32 @PsiType:String.
     * @param campo28 @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public VtolCancelResponse(String codigo, String campo24, String campo22, String campo25, String campo27, String campo32, String campo28) {
        this.codigo = codigo;
        this.campo24 = campo24;
        this.campo22 = campo22;
        this.campo25 = campo25;
        this.campo27 = campo27;
        this.campo32 = campo32;
        this.campo28 = campo28;
    }

    /**
     * Type: Method.
     * Parent: VtolCancelResponse.
     * Name: VtolCancelResponse.
     *
     * @param in @PsiType:Parcel.
     * @Description.
     * @EndDescription.
     */
    protected VtolCancelResponse(Parcel in) {
        codigo = in.readString();
        campo24 = in.readString();
        campo22 = in.readString();
        campo25 = in.readString();
        campo27 = in.readString();
        campo32 = in.readString();
        campo28 = in.readString();
    }

    /**
     * The constant CREATOR.
     */
    public static final Creator<VtolCancelResponse> CREATOR = new Creator<VtolCancelResponse>() {
        @Override
        public VtolCancelResponse createFromParcel(Parcel in) {
            return new VtolCancelResponse(in);
        }

        @Override
        public VtolCancelResponse[] newArray(int size) {
            return new VtolCancelResponse[size];
        }
    };

    /**
     * Type: Method.
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
     * Name: toString.
     *
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    @Override
    public String toString() {
        return "VtolCancelResponse{" +
                "codigo='" + codigo + '\'' +
                ", campo24='" + campo24 + '\'' +
                ", campo22='" + campo22 + '\'' +
                ", campo25='" + campo25 + '\'' +
                ", campo27='" + campo27 + '\'' +
                ", campo32='" + campo32 + '\'' +
                ", campo28='" + campo28 + '\'' +
                '}';
    }

    /**
     * Type: Method.
     * Parent: VtolCancelResponse.
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
     * Parent: VtolCancelResponse.
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
