/*
 * *
 *  * Created by Gerardo Ruiz on 12/16/20 4:41 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/10/20 12:25 AM
 *
 */

package com.Verifone;

import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;


/**
 * Type: Class.
 * Access: Public.
 * Name: EMVResponse.
 *
 * @Description.
 * @EndDescription.
 */
public class EMVResponse {
    /**
     * The Status.
     */
    int status;
    /**
     * The Message.
     */
    String message;
    /**
     * The Transaction type.
     */
    String transactionType;
    /**
     * The Chip tokens.
     */
    String chipTokens;

    /**
     * The Pan.
     */
    String pan;
    /**
     * The Tarjetahabiente.
     */
    String tarjetahabiente;
    /**
     * The Track ii.
     */
    String trackII;
    /**
     * The Track i.
     */
    String trackI;
    /**
     * The Codigo seguridad.
     */
    String codigoSeguridad;
    /**
     * The Modo lectura.
     */
    String modoLectura;
    /**
     * The Param e 2.
     */
    String paramE2;
    /**
     * The Tag 4 f.
     */
    String tag4F;
    /**
     * The Tag 9 f 12.
     */
    String tag9F12;
    /**
     * The Tag 50.
     */
    String tag50;
    /**
     * The Tag 5 f 30.
     */
    String tag5F30;
    /**
     * The Tag 5 f 34.
     */
    String tag5F34;
    /**
     * The Tag 9 f 34.
     */
    String tag9F34;
    /**
     * The Tag c 2.
     */
    String tagC2;
    /**
     * The Tag 95.
     */
    String tag95;
    /**
     * The Tag 9 f 27.
     */
    String tag9F27;
    /**
     * The Tag 9 f 26.
     */
    String tag9F26;
    /**
     * The Tag 9 b.
     */
    String tag9B;
    /**
     * The Tag 9 f 39.
     */
    String tag9F39;
    /**
     * The Tag 8 a.
     */
    String tag8A;
    /**
     * The Tag 99.
     */
    String tag99;
    /**
     * The Ct es.
     */
    String ctES;
    /**
     * The Ct r 1.
     */
    String ctR1;
    /**
     * The Ct ez.
     */
    String ctEZ;
    /**
     * The Ct ey.
     */
    String ctEY;
    /**
     * The Ct cz.
     */
    String ctCZ;

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: EMVResponse.
     *
     * @Description.
     * @EndDescription.
     */
    public EMVResponse() {
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: EMVResponse.
     *
     * @param tagFields  @PsiType:String[].
     * @param chipTokens @PsiType:String[].
     * @param e2         @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public EMVResponse(String[] tagFields, String[] chipTokens, String e2) {
        status = 0;
        paramE2 = e2;
        tag4F = tagFields[0];
        tag9F12 = tagFields[1];
        tag50 = new String(CommandUtils.hexStringToByteArray(tagFields[2]));//tagFields[2];
        tag5F30 = tagFields[3];
        tag5F34 = tagFields[4];
        tag9F34 = tagFields[5];
        tagC2 = tagFields[6];
        tag95 = tagFields[7];
        tag9F27 = tagFields[8];
        tag9F26 = tagFields[9];
        tag9B = tagFields[10];
        tag9F39 = tagFields[11];
        tag8A = tagFields[12];
        tag99 = tagFields[13];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.i("parce53", "EMVResponse: " + chipTokens.length);
            ctES = chipTokens[0];
            ctR1 = chipTokens[1];
            ctEZ = chipTokens[2];
            ctEY = chipTokens[3];
            ctCZ = chipTokens.length == 5 ? chipTokens[4] : "";
        } else {
            ctES = chipTokens[1];
            ctR1 = chipTokens[2];
            ctEZ = chipTokens[3];
            ctEY = chipTokens[4];
            ctCZ = chipTokens.length == 6 ? chipTokens[5] : "";
        }
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: EMVResponse.
     *
     * @param chipTokens @PsiType:String[].
     * @Description.
     * @EndDescription.
     */
    public EMVResponse(String[] chipTokens) {
        status = 0;
        paramE2 = "";
        ctES = chipTokens[1];
        ctR1 = chipTokens[2];
        ctEZ = chipTokens[3];
        ctEY = chipTokens[4];
        ctCZ = chipTokens.length == 6 ? chipTokens[5] : "";
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getStatus.
     *
     * @return the status
     * @Description.
     * @EndDescription.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setStatus.
     *
     * @Description.
     * @EndDescription.
     * @param: status @PsiType:int.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getMessage.
     *
     * @return the message
     * @Description.
     * @EndDescription.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setMessage.
     *
     * @Description.
     * @EndDescription.
     * @param: message @PsiType:String.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getParamE2.
     *
     * @return the param e 2
     * @Description.
     * @EndDescription.
     */
    public String getParamE2() {
        return paramE2;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setParamE2.
     *
     * @Description.
     * @EndDescription.
     * @param: paramE2 @PsiType:String.
     */
    public void setParamE2(String paramE2) {
        this.paramE2 = paramE2;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getPan.
     *
     * @return the pan
     * @Description.
     * @EndDescription.
     */
    public String getPan() {
        return pan;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setPan.
     *
     * @Description.
     * @EndDescription.
     * @param: pan @PsiType:String.
     */
    public void setPan(String pan) {
        this.pan = pan;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTarjetahabiente.
     *
     * @return the tarjetahabiente
     * @Description.
     * @EndDescription.
     */
    public String getTarjetahabiente() {
        return tarjetahabiente;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTarjetahabiente.
     *
     * @Description.
     * @EndDescription.
     * @param: tarjetahabiente @PsiType:String.
     */
    public void setTarjetahabiente(String tarjetahabiente) {
        this.tarjetahabiente = tarjetahabiente;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTrackII.
     *
     * @return the track ii
     * @Description.
     * @EndDescription.
     */
    public String getTrackII() {
        return trackII;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTrackII.
     *
     * @Description.
     * @EndDescription.
     * @param: trackII @PsiType:String.
     */
    public void setTrackII(String trackII) {
        this.trackII = trackII;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTrackI.
     *
     * @return the track i
     * @Description.
     * @EndDescription.
     */
    public String getTrackI() {
        return trackI;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTrackI.
     *
     * @Description.
     * @EndDescription.
     * @param: trackI @PsiType:String.
     */
    public void setTrackI(String trackI) {
        this.trackI = trackI;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getCodigoSeguridad.
     *
     * @return the codigo seguridad
     * @Description.
     * @EndDescription.
     */
    public String getCodigoSeguridad() {
        return codigoSeguridad;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setCodigoSeguridad.
     *
     * @Description.
     * @EndDescription.
     * @param: codigoSeguridad @PsiType:String.
     */
    public void setCodigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getModoLectura.
     *
     * @return the modo lectura
     * @Description.
     * @EndDescription.
     */
    public String getModoLectura() {
        return modoLectura;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setModoLectura.
     *
     * @Description.
     * @EndDescription.
     * @param: modoLectura @PsiType:String.
     */
    public void setModoLectura(String modoLectura) {
        this.modoLectura = modoLectura;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag4F.
     *
     * @return the tag 4 f
     * @Description.
     * @EndDescription.
     */
    public String getTag4F() {
        return tag4F;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag4F.
     *
     * @Description.
     * @EndDescription.
     * @param: tag4F @PsiType:String.
     */
    public void setTag4F(String tag4F) {
        this.tag4F = tag4F;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag9F12.
     *
     * @return the tag 9 f 12
     * @Description.
     * @EndDescription.
     */
    public String getTag9F12() {
        return tag9F12;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag9F12.
     *
     * @Description.
     * @EndDescription.
     * @param: tag9F12 @PsiType:String.
     */
    public void setTag9F12(String tag9F12) {
        this.tag9F12 = tag9F12;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag50.
     *
     * @return the tag 50
     * @Description.
     * @EndDescription.
     */
    public String getTag50() {
        return tag50;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag50.
     *
     * @Description.
     * @EndDescription.
     * @param: tag50 @PsiType:String.
     */
    public void setTag50(String tag50) {
        this.tag50 = tag50;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag5F30.
     *
     * @return the tag 5 f 30
     * @Description.
     * @EndDescription.
     */
    public String getTag5F30() {
        return tag5F30;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag5F30.
     *
     * @Description.
     * @EndDescription.
     * @param: tag5F30 @PsiType:String.
     */
    public void setTag5F30(String tag5F30) {
        this.tag5F30 = tag5F30;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag5F34.
     *
     * @return the tag 5 f 34
     * @Description.
     * @EndDescription.
     */
    public String getTag5F34() {
        return tag5F34;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag5F34.
     *
     * @Description.
     * @EndDescription.
     * @param: tag5F34 @PsiType:String.
     */
    public void setTag5F34(String tag5F34) {
        this.tag5F34 = tag5F34;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag9F34.
     *
     * @return the tag 9 f 34
     * @Description.
     * @EndDescription.
     */
    public String getTag9F34() {
        return tag9F34;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag9F34.
     *
     * @Description.
     * @EndDescription.
     * @param: tag9F34 @PsiType:String.
     */
    public void setTag9F34(String tag9F34) {
        this.tag9F34 = tag9F34;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTagC2.
     *
     * @return the tag c 2
     * @Description.
     * @EndDescription.
     */
    public String getTagC2() {
        return tagC2;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTagC2.
     *
     * @Description.
     * @EndDescription.
     * @param: tagC2 @PsiType:String.
     */
    public void setTagC2(String tagC2) {
        this.tagC2 = tagC2;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag95.
     *
     * @return the tag 95
     * @Description.
     * @EndDescription.
     */
    public String getTag95() {
        return tag95;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag95.
     *
     * @Description.
     * @EndDescription.
     * @param: tag95 @PsiType:String.
     */
    public void setTag95(String tag95) {
        this.tag95 = tag95;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag9F27.
     *
     * @return the tag 9 f 27
     * @Description.
     * @EndDescription.
     */
    public String getTag9F27() {
        return tag9F27;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag9F27.
     *
     * @Description.
     * @EndDescription.
     * @param: tag9F27 @PsiType:String.
     */
    public void setTag9F27(String tag9F27) {
        this.tag9F27 = tag9F27;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag9F26.
     *
     * @return the tag 9 f 26
     * @Description.
     * @EndDescription.
     */
    public String getTag9F26() {
        return tag9F26;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag9F26.
     *
     * @Description.
     * @EndDescription.
     * @param: tag9F26 @PsiType:String.
     */
    public void setTag9F26(String tag9F26) {
        this.tag9F26 = tag9F26;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag9B.
     *
     * @return the tag 9 b
     * @Description.
     * @EndDescription.
     */
    public String getTag9B() {
        return tag9B;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag9B.
     *
     * @Description.
     * @EndDescription.
     * @param: tag9B @PsiType:String.
     */
    public void setTag9B(String tag9B) {
        this.tag9B = tag9B;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag9F39.
     *
     * @return the tag 9 f 39
     * @Description.
     * @EndDescription.
     */
    public String getTag9F39() {
        return tag9F39;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag9F39.
     *
     * @Description.
     * @EndDescription.
     * @param: tag9F39 @PsiType:String.
     */
    public void setTag9F39(String tag9F39) {
        this.tag9F39 = tag9F39;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag8A.
     *
     * @return the tag 8 a
     * @Description.
     * @EndDescription.
     */
    public String getTag8A() {
        return tag8A;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag8A.
     *
     * @Description.
     * @EndDescription.
     * @param: tag8A @PsiType:String.
     */
    public void setTag8A(String tag8A) {
        this.tag8A = tag8A;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTag99.
     *
     * @return the tag 99
     * @Description.
     * @EndDescription.
     */
    public String getTag99() {
        return tag99;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTag99.
     *
     * @Description.
     * @EndDescription.
     * @param: tag99 @PsiType:String.
     */
    public void setTag99(String tag99) {
        this.tag99 = tag99;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getCtES.
     *
     * @return the ct es
     * @Description.
     * @EndDescription.
     */
    public String getCtES() {
        return ctES;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setCtES.
     *
     * @Description.
     * @EndDescription.
     * @param: ctES @PsiType:String.
     */
    public void setCtES(String ctES) {
        this.ctES = ctES;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getCtR1.
     *
     * @return the ct r 1
     * @Description.
     * @EndDescription.
     */
    public String getCtR1() {
        return ctR1;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setCtR1.
     *
     * @Description.
     * @EndDescription.
     * @param: ctR1 @PsiType:String.
     */
    public void setCtR1(String ctR1) {
        this.ctR1 = ctR1;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getCtEZ.
     *
     * @return the ct ez
     * @Description.
     * @EndDescription.
     */
    public String getCtEZ() {
        return ctEZ;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setCtEZ.
     *
     * @Description.
     * @EndDescription.
     * @param: ctEZ @PsiType:String.
     */
    public void setCtEZ(String ctEZ) {
        this.ctEZ = ctEZ;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getCtEY.
     *
     * @return the ct ey
     * @Description.
     * @EndDescription.
     */
    public String getCtEY() {
        return ctEY;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setCtEY.
     *
     * @Description.
     * @EndDescription.
     * @param: ctEY @PsiType:String.
     */
    public void setCtEY(String ctEY) {
        this.ctEY = ctEY;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getCtCZ.
     *
     * @return the ct cz
     * @Description.
     * @EndDescription.
     */
    public String getCtCZ() {
        return ctCZ;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setCtCZ.
     *
     * @Description.
     * @EndDescription.
     * @param: ctCZ @PsiType:String.
     */
    public void setCtCZ(String ctCZ) {
        this.ctCZ = ctCZ;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getTransactionType.
     *
     * @return the transaction type
     * @Description.
     * @EndDescription.
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setTransactionType.
     *
     * @Description.
     * @EndDescription.
     * @param: transactionType @PsiType:String.
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: getChipTokens.
     *
     * @return the chip tokens
     * @Description.
     * @EndDescription.
     */
    public String getChipTokens() {
        return chipTokens;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: setChipTokens.
     *
     * @Description.
     * @EndDescription.
     * @param: chipTokens @PsiType:String.
     */
    public void setChipTokens(String chipTokens) {
        this.chipTokens = chipTokens;
    }

    /**
     * Type: Method.
     * Parent: EMVResponse.
     * Name: toString.
     *
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

}
