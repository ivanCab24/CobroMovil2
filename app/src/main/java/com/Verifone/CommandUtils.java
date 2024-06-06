/*
 * *
 *  * Created by Gerardo Ruiz on 12/16/20 4:41 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/10/20 12:25 AM
 *
 */

package com.Verifone;

import static mx.com.adquira.blueadquira.pockdata.PostDefine.STX;

import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.util.HashMap;


/**
 * Type: Class.
 * Access: Public.
 * Name: CommandUtils.
 *
 * @Description.
 * @EndDescription.
 */
public class CommandUtils extends AppCompatActivity implements VerifonePinpadInterface {

    /**
     * The constant TAG.
     */
    private static final String TAG = "CommandUtils";
    /**
     * The Retries.
     */
    private int retries = 1;
    /**
     * The constant transactionType.
     */
    private static String transactionType = "";
    /**
     * The constant response.
     */
    private static EMVResponse response = null;


    /**
     * Type: Method.
     * Parent: CommandUtils.
     * Name: parceC53.
     *
     * @param inputHex   @PsiType:String.
     * @param inputAscii @PsiType:String.
     * @return emv response
     * @Description.
     * @EndDescription. emv response.
     */
    public static EMVResponse parceC53(String inputHex, String inputAscii) {

        Log.i(TAG, "parceC53: Entra a parseC53");
        EMVResponse response = null;

        //byte[] tlvBytes = ArrayUtils.subarray(input,7, input.length);
        if (inputAscii.length() >= 6) {

            String command = inputAscii.substring(1, 4);
            String status = inputAscii.substring(4, 6);
            Log.i(TAG, "parceC53: Command: " + command);
            Log.i(TAG, "parceC53: status: " + status);

            String[] tlvTokens = inputHex.split("c1");
            String e1e2 = tlvTokens[tlvTokens.length - 1]/*"9f37041a71165e9f4104000000839f53015221204553303030363020564658313020202020202020202020202020202020202020202020202056463430313436363231383530303030303030303030303030303030303030212052313030303030202120455a3030303938203031313030303030303032383131303030303239303030303034303030313037333741303030413732323846444231414645323942333844363030423935383145393634383837423443344646323144373342363533333935354439384230334535212045593030303030202120435a30303034302030314331323230303030303020202020202020202020202020202020202020202020202020202020"*/;
            Log.i(TAG, "parceC53: e1e2: " + e1e2);

            if (command.equals("C53")) {

                Log.i(TAG, "parceC53: Status: " + status);

                switch (status) {

                    case "00":
                        transactionType = "CHIP";

                        if (e1e2.substring(0, 4).equals("0230")) {

                            Log.i(TAG, "parceC53: ELE2 OK");

                            response = lecturaHex(inputHex, status);

                            break;

                        } else {

                            Log.i(TAG, "parceC53: ELE2 ERROR");

                            response = new EMVResponse();
                            response.setMessage(String.valueOf(63));

                            break;
                        }


                    case "22":
                        transactionType = "CONTACTLESS";

                        if (e1e2.substring(0, 4).equals("0230")) {

                            Log.i(TAG, "parceC53: ELE2 OK");

                            response = lecturaHex(inputHex, status);

                            break;

                        } else {

                            Log.i(TAG, "parceC53: ELE2 ERROR");

                            response = new EMVResponse();
                            response.setMessage(String.valueOf(63));

                            break;
                        }

                    case "21":
                        transactionType = "BANDA";

                        if (e1e2.substring(0, 4).equals("0230") || e1e2.substring(0, 4).equals("0239")) {

                            Log.i(TAG, "parceC53: ELE2 OK");

                            response = lecturaHex(inputHex, status);

                            break;

                        } else {

                            Log.i(TAG, "parceC53: ELE2 ERROR");

                            response = new EMVResponse();
                            response.setMessage(String.valueOf(63));

                            break;
                        }
                    default:
                        transactionType = "ERROR";
                        Log.i(TAG, "parceC53: Status: " + catalogStatus().get(status));
                        response = new EMVResponse();
                        response.setMessage(status);
                        return response;

                }

                if (response != null) {
                    response.setTransactionType(transactionType);
                    if (response.getTrackII() == null || response.trackII.equals("00")) {
                        response.setTrackII(response.getPan() + "=2611");
                    }
                }

            }

        } else {

            response = new EMVResponse();
            response.setMessage(String.valueOf(98));

        }

        return response;
    }

    /**
     * Type: Method.
     * Parent: CommandUtils.
     * Name: lecturaHex.
     *
     * @param inputHex @PsiType:String.
     * @param status   @PsiType:String.
     * @return emv response
     * @Description.
     * @EndDescription. emv response.
     */
    public static EMVResponse lecturaHex(String inputHex, String status) {

        String[] tlvTokensContactless = inputHex.split("c1");
        String e1e2Contactless = tlvTokensContactless[tlvTokensContactless.length - 1];
        Log.i(TAG, "lecturaHex: ele2: " + e1e2Contactless);

        for (int i = 1; i < tlvTokensContactless.length; i++) {
            //tlvTokens[i] = tlvTokens[i].replaceAll("\\s","");
            if (!tlvTokensContactless[i].equals("00")) {
                tlvTokensContactless[i] = tlvTokensContactless[i].substring(2, tlvTokensContactless[i].length());
            }
        }

        tlvTokensContactless[6] = tlvTokensContactless[6].substring(0, 4);


        String tarjetaHab1Contactless = new String(hexStringToByteArray(tlvTokensContactless[2]));

        String tarjetaHabWiouthSlashContactlees = tarjetaHab1Contactless.replace("/", " ");


        response = getE1E2(e1e2Contactless, Integer.parseInt(status));
        if (response != null) {
            response.setPan(tlvTokensContactless[1]);
            Log.i(TAG, "lecturaHex: Tarjeta" + byteArrayToHexString(tarjetaHabWiouthSlashContactlees.getBytes()));
            response.setTarjetahabiente(byteArrayToHexString(tarjetaHabWiouthSlashContactlees.getBytes()).contains("00") ? "" : tarjetaHabWiouthSlashContactlees);
            //response.setTarjetahabiente(tlvTokens[2]);
            response.setTrackII(tlvTokensContactless[3]);
            response.setTrackII(tlvTokensContactless[4]);
            response.setCodigoSeguridad(tlvTokensContactless[5]);
            response.setModoLectura(tlvTokensContactless[6]);

            response.setChipTokens(response.getCtES() + response.getCtEZ());
            response.setMessage(catalogStatus().get(status));

            if (transactionType.equals("BANDA")) {

                response.setChipTokens("!" + response.getCtES() + "!" + response.getCtEZ() + "!" + response.getCtEY());

            } else {

                response.setChipTokens(response.getCtES() + response.getCtEZ() + response.getCtCZ());
            }

            Log.i(TAG, "lecturaHex: " + response.toString());

        }
        return response;
    }

    /**
     * Type: Method.
     * Parent: CommandUtils.
     * Name: getE1E2.
     *
     * @param input @PsiType:String.
     * @param stat  @PsiType:int.
     * @return the e 1 e 2
     * @Description.
     * @EndDescription.
     */
    public static EMVResponse getE1E2(String input, int stat) {
        EMVResponse emvResponse;
        input = input.substring(8);

        byte[] b = hexStringToByteArray(input);
        int indexE1 = 0;

        int sizeE1 = b[indexE1] + (b[indexE1] < 0 ? 256 : 0);

        int indexE2 = sizeE1 + 2;
        int sizeE2 = b[indexE2] + (b[indexE2] < 0 ? 256 : 0);


        // para status 21 el tamanio de  E1 y E2 es cero
        if (stat != 21) {
            String e1String = input.substring(2, 2 + (sizeE1 * 2));
            Log.i(TAG, "getE1E2: e1String: " + e1String);
            String e2String = input.substring((indexE2 * 2) + 2, (indexE2 * 2) + 2 + (sizeE2 * 2));
            Log.i(TAG, "getE1E2: e2String: " + e2String);

            /* Obtiene los nobres de los tags y sus valores para la cadena E1 */
            final int[] e1TagSizes = {2, 4, 2, 4, 4, 4, 2, 2, 4, 4, 2, 4, 2, 2, 4};
            int tagSize;
            String tag;
            String paramSize;
            String param;
            String[] tagArray = new String[15];
            int i = 0;
            int j = 0;

            while (i < e1String.length()) {
                tagSize = e1TagSizes[j];
                tag = e1String.substring(i, i + tagSize);
                if (i + tagSize + 2 > e1String.length()) {
                    Log.i(TAG, "getE1E2: " + tag + ": NULL");
                    break;
                }
                paramSize = e1String.substring(i + tagSize, i + tagSize + 2);
                Log.i("Size Param", paramSize);
                if (paramSize.equals("00")) {
                    Log.i(TAG, "getE1E2: " + tag + ": NULL");
                    i = i + tagSize + 2;
                } else {
                    param = e1String.substring(i + tagSize + 2, i + tagSize + 2 + (Integer.parseInt(paramSize, 16) * 2));
                    i = i + tagSize + 2 + (Integer.parseInt(paramSize, 16) * 2);
                    Log.i(TAG, "getE1E2: " + tag + ": " + param);
                    tagArray[j] = param;
                }
                j++;
            }


           /* String tag9F = tagArray[tagArray.length];
            appLog("Mensaje " + String.valueOf(tag9F));*/

            String chipTokens = input.substring(6 + ((sizeE1 + sizeE2) * 2), input.length());
            chipTokens = new String(hexStringToByteArray(chipTokens));
            Log.i(TAG, "getE1E2: " + chipTokens);
            //String[] chipTokensArray = chipTokens.split("!");
            String[] chipTokensArray = chipTokens.split("(?=!)");
            for (String token : chipTokensArray) {
                if (token.length() > 3)
                    token = token.substring(5, token.length());
                Log.i(TAG, "getE1E2: " + token);
            }


            Log.i(TAG, "getE1E2: ChipTokens" + chipTokensArray.length);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                if (chipTokensArray.length == 5) {
                    emvResponse = new EMVResponse(tagArray, chipTokensArray, e2String);
                } else {
                    emvResponse = null;
                }

            } else {

                if (chipTokensArray.length == 6) {
                    emvResponse = new EMVResponse(tagArray, chipTokensArray, e2String);
                } else {
                    emvResponse = null;
                }

            }

        } else {
            String chipTokens = input.substring(6, input.length());
            chipTokens = new String(hexStringToByteArray(chipTokens));
            Log.i(TAG, "getE1E2: " + chipTokens);
            String[] chipTokensArray = chipTokens.split("!");
            for (String token : chipTokensArray) {
                if (token.length() > 3)
                    token = token.substring(5, token.length());
                Log.i(TAG, "getE1E2: " + token);
            }

            emvResponse = new EMVResponse(chipTokensArray);
        }
        return emvResponse;
    }

    /**
     * Type: Method.
     * Parent: CommandUtils.
     * Name: toHex.
     *
     * @param arg @PsiType:String.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    public static String toHex(String arg) {
        return String.format("%x", new BigInteger(arg.getBytes()));
    }

    /**
     * Type: Method.
     * Parent: CommandUtils.
     * Name: byteArrayToHexString.
     *
     * @param byteArray @PsiType:byte[].
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    public static String byteArrayToHexString(byte[] byteArray) {

        StringBuilder result = new StringBuilder();
        for (byte aByteArray : byteArray) {
            result.append(String.format("%02X ", aByteArray));
        }
        return result.toString();

    }

    /**
     * Type: Method.
     * Parent: CommandUtils.
     * Name: calculateLRC.
     *
     * @param message @PsiType:String.
     * @return byte
     * @Description.
     * @EndDescription. byte.
     */
    public static byte calculateLRC(String message) {
        int index = 0;
        int LCR;
        int bytemessage[] = new int[message.length()];


        for (int i = 0; i < message.length(); i++) {
            bytemessage[i] = message.charAt(i);
        }

        if (bytemessage[0] == STX) {
            index = 1;
        }
        LCR = bytemessage[0 + index];

        for (int i = 1 + index; i < bytemessage.length; i++) {
            LCR ^= bytemessage[i];
        }

        return (byte) LCR;
    }

    /**
     * Type: Method.
     * Parent: CommandUtils.
     * Name: hexToAscii.
     *
     * @param hex @PsiType:String.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    public static String hexToAscii(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        Log.i("INFO hexToAscii ", hex);
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }


    /**
     * Type: Method.
     * Parent: CommandUtils.
     * Name: hexStringToByteArray.
     *
     * @param s @PsiType:String.
     * @return byte [ ]
     * @Description.
     * @EndDescription. byte [ ].
     */
    public static byte[] hexStringToByteArray(String s) {

        if (s != null) {

            int len = s.length();
            byte[] data = new byte[len / 2];

            Log.i("TamañoHex", String.valueOf(data.length));

            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i + 1), 16));
            }

            return data;
        } else {
            return new byte[0];
        }

    }

    /**
     * Type: Method.
     * Parent: CommandUtils.
     * Name: catalogStatus.
     *
     * @return hash map
     * @Description.
     * @EndDescription. hash map.
     */
    public static HashMap<String, String> catalogStatus() {

        HashMap<String, String> StatusCatalog = new HashMap<>();

        StatusCatalog.put("00", "Operación exitosa");
        StatusCatalog.put("01", "Mensaje inválido");
        StatusCatalog.put("02", "Formato de datos inválido");
        StatusCatalog.put("06", "Tiempo de espera excedido");
        StatusCatalog.put("10", "Falla en la lectura del chip");
        StatusCatalog.put("20", "Tarjeta Digitada");
        StatusCatalog.put("21", "Use banda magnética");
        StatusCatalog.put("22", "Contacless");
        StatusCatalog.put("23", "Tarjeta removida");
        StatusCatalog.put("25", "Tarjeta no soportada");
        StatusCatalog.put("26", "Aplicación inválida");
        StatusCatalog.put("27", "Tarjeta Operador inválida");
        StatusCatalog.put("29", "Tarj con fecha vencimiento expirada");
        StatusCatalog.put("30", "Fecha Inválida");
        StatusCatalog.put("50", "CRC no coincide. Proceso falló llame a soporte");
        StatusCatalog.put("51", "Check Value incorrecto");
        StatusCatalog.put("52", "Llave Inexistente");
        StatusCatalog.put("53", "Problemas de cripto HSM");
        StatusCatalog.put("54", "Límite de cifrado excedido. Inicialice llave");
        StatusCatalog.put("55", "Firma incorrecta. Proceso falló llame a soporte");
        StatusCatalog.put("56", "Error de longitud en la Telecarga. Proceso falló llame a soporte.");
        StatusCatalog.put("60", "Comando no permitido");
        StatusCatalog.put("61", "Llaves inicializadas. Comando NO Reconocido");
        StatusCatalog.put("62", "No se ha realizado inicialización de llaves");
        StatusCatalog.put("63", "Error de Lectura");
        StatusCatalog.put("98", "de Lectura.\nSi el problema persiste reinicie la terminal.");
        StatusCatalog.put("99", "Otra falla. Cancelar desde pinpad");


        return StatusCatalog;

    }
}
