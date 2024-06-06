package com.Utilities.Printer.PrinterControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.View.Fragments.ContentFragment;
import com.View.UserInteraction;
import com.bixolon.commonlib.BXLCommonConst;
import com.bixolon.commonlib.log.LogService;
import com.bxl.config.editor.BXLConfigLoader;

import java.nio.ByteBuffer;

import jpos.CashDrawer;
import jpos.JposConst;
import jpos.JposException;
import jpos.LocalSmartCardRW;
import jpos.MSR;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.SmartCardRW;
import jpos.config.JposEntry;
import jpos.events.ErrorEvent;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteEvent;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;

/**
 * Type: Class.
 * Access: Public.
 * Name: BixolonPrinter.
 *
 * @Description.
 * @EndDescription.
 */
public class BixolonPrinter implements ErrorListener, OutputCompleteListener, StatusUpdateListener {
    /**
     * The constant ALIGNMENT_LEFT.
     */
// ------------------- alignment ------------------- //
    public static int ALIGNMENT_LEFT = 1;
    /**
     * The constant ALIGNMENT_CENTER.
     */
    public static int ALIGNMENT_CENTER = 2;
    /**
     * The constant ALIGNMENT_RIGHT.
     */
    public static int ALIGNMENT_RIGHT = 4;

    /**
     * The constant ATTRIBUTE_NORMAL.
     */
// ------------------- Text attribute ------------------- //
    public static int ATTRIBUTE_NORMAL = 0;
    /**
     * The constant ATTRIBUTE_FONT_A.
     */
    public static int ATTRIBUTE_FONT_A = 1;
    /**
     * The constant ATTRIBUTE_FONT_B.
     */
    public static int ATTRIBUTE_FONT_B = 2;
    /**
     * The constant ATTRIBUTE_FONT_C.
     */
    public static int ATTRIBUTE_FONT_C = 4;
    /**
     * The constant ATTRIBUTE_BOLD.
     */
    public static int ATTRIBUTE_BOLD = 8;
    /**
     * The constant ATTRIBUTE_UNDERLINE.
     */
    public static int ATTRIBUTE_UNDERLINE = 16;
    /**
     * The constant ATTRIBUTE_REVERSE.
     */
    public static int ATTRIBUTE_REVERSE = 32;
    /**
     * The constant ATTRIBUTE_FONT_D.
     */
    public static int ATTRIBUTE_FONT_D = 64;

    /**
     * The constant BARCODE_TYPE_QRCODE.
     */
// ------------------- Barcode Symbology ------------------- //
    public static int BARCODE_TYPE_QRCODE = POSPrinterConst.PTR_BCS_QRCODE;

    /**
     * The constant BARCODE_HRI_NONE.
     */
// ------------------- Barcode HRI ------------------- //
    public static int BARCODE_HRI_NONE = POSPrinterConst.PTR_BC_TEXT_NONE;

    /**
     * The Context.
     */
    private Context context = null;

    /**
     * The Bxl config loader.
     */
    private BXLConfigLoader bxlConfigLoader = null;
    /**
     * The Pos printer.
     */
    private POSPrinter posPrinter = null;
    /**
     * The Msr.
     */
    private MSR msr = null;
    /**
     * The Smart card rw.
     */
    private SmartCardRW smartCardRW = null;
    /**
     * The Local smart card rw.
     */
    private LocalSmartCardRW localSmartCardRW;
    /**
     * The Cash drawer.
     */
    private CashDrawer cashDrawer = null;

    /**
     * The M port type.
     */
    private int mPortType;
    /**
     * The M address.
     */
    private String mAddress;

    /**
     * The Dialog imprime voucher interface.
     */
    private dialogImprimeVoucher dialogImprimeVoucherInterface;
    /**
     * The Dialog imprime ticket interface.
     */
    private dialogImprimeTicket dialogImprimeTicketInterface;
    /**
     * The Dialog imprime copia interface.
     */
    private dialogImprimeCopia dialogImprimeCopiaInterface;
    /**
     * The Dialog error respuesta interface.
     */
    private dialogErrorRespuesta dialogErrorRespuestaInterface;

    /**
     * Type: Interface.
     * Parent: BixolonPrinter.
     * Name: dialogImprimeVoucher.
     */
    public interface dialogImprimeVoucher {
        /**
         * Type: Method.
         * Parent: dialogImprimeVoucher.
         * Name: onFinishedVoucherBixolon.
         *
         * @Description.
         * @EndDescription.
         */
//void onExceptionVoucherBixolon(String error);
        void onFinishedVoucherBixolon();
    }

    /**
     * Type: Interface.
     * Parent: BixolonPrinter.
     * Name: dialogImprimeTicket.
     */
    public interface dialogImprimeTicket {
        /**
         * Type: Method.
         * Parent: dialogImprimeTicket.
         * Name: onFinishedTicketBixolon.
         *
         * @Description.
         * @EndDescription.
         */
        void onFinishedTicketBixolon();
    }

    /**
     * Type: Interface.
     * Parent: BixolonPrinter.
     * Name: dialogImprimeCopia.
     */
    public interface dialogImprimeCopia {
        /**
         * Type: Method.
         * Parent: dialogImprimeCopia.
         * Name: onFinishedCopiaBixolon.
         *
         * @Description.
         * @EndDescription.
         */
//void onExceptionCopiBixolon(String error);
        void onFinishedCopiaBixolon();

        /**
         * Type: Method.
         * Parent: dialogImprimeCopia.
         * Name: onPrintedQR.
         *
         * @Description.
         * @EndDescription.
         */
        void onPrintedQR();
    }

    /**
     * Type: Interface.
     * Parent: BixolonPrinter.
     * Name: dialogErrorRespuesta.
     */
    public interface dialogErrorRespuesta {

        /**
         * Type: Method.
         * Parent: dialogErrorRespuesta.
         * Name: onFinishedComprobanteBixolon.
         *
         * @Description.
         * @EndDescription.
         */
        void onFinishedComprobanteBixolon();

    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: BixolonPrinter.
     *
     * @param context              @PsiType:Context.
     * @param dialogImprimeVoucher @PsiType:dialogImprimeVoucher.
     * @param dialogImprimeTicket  @PsiType:dialogImprimeTicket.
     * @param dialogImprimeCopia   @PsiType:dialogImprimeCopia.
     * @param dialogErrorRespuesta @PsiType:dialogErrorRespuesta.
     * @Description.
     * @EndDescription.
     */
    public BixolonPrinter(Context context, dialogImprimeVoucher dialogImprimeVoucher, dialogImprimeTicket dialogImprimeTicket, dialogImprimeCopia dialogImprimeCopia, dialogErrorRespuesta dialogErrorRespuesta) {
        this.context = context;

        dialogImprimeVoucherInterface = dialogImprimeVoucher;
        dialogImprimeTicketInterface = dialogImprimeTicket;
        dialogImprimeCopiaInterface = dialogImprimeCopia;
        dialogErrorRespuestaInterface = dialogErrorRespuesta;

        posPrinter = new POSPrinter(this.context);
        posPrinter.addStatusUpdateListener(this);
        posPrinter.addErrorListener(this);
        posPrinter.addOutputCompleteListener(this);

        bxlConfigLoader = new BXLConfigLoader(this.context);
        try {
            bxlConfigLoader.openFile();
        } catch (Exception e) {
            bxlConfigLoader.newFile();
        }

        LogService.InitDebugLog(true, true, BXLCommonConst._LOG_LEVEL_HIGH);

    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: printerOpen.
     *
     * @param portType    @PsiType:int.
     * @param logicalName @PsiType:String.
     * @param address     @PsiType:String.
     * @param isAsyncMode @PsiType:boolean.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean printerOpen(int portType, String logicalName, String address, boolean isAsyncMode) {
        if (setTargetDevice(portType, logicalName, BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER, address)) {
            try {
                posPrinter.open(logicalName);
                posPrinter.claim(5000 * 2);
                posPrinter.setDeviceEnabled(true);
                posPrinter.setAsyncMode(isAsyncMode);

                mPortType = portType;
                mAddress = address;
            } catch (JposException e) {
                e.printStackTrace();

                /*if (dialogImprimeVoucherInterface != null){
                    dialogImprimeVoucherInterface.onExceptionVoucherBixolon(e.getMessage());
                }*/

                try {
                    posPrinter.close();
                } catch (JposException e1) {
                    e1.printStackTrace();
                    /*if (dialogImprimeVoucherInterface != null){
                        dialogImprimeVoucherInterface.onExceptionVoucherBixolon(e1.getMessage());
                    }*/
                }

                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: printerClose.
     *
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean printerClose() {
        try {
            if (posPrinter.getClaimed()) {
                posPrinter.setDeviceEnabled(false);
                posPrinter.close();
            }
        } catch (JposException e) {
            e.printStackTrace();
            /*if (dialogImprimeVoucherInterface != null){
                dialogImprimeVoucherInterface.onExceptionVoucherBixolon(e.getMessage());
            }*/
        }

        return true;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: setTargetDevice.
     *
     * @return
     * @Description.
     * @EndDescription.
     * @param: portType @PsiType:int.
     * @param: logicalName @PsiType:String.
     * @param: deviceCategory @PsiType:int.
     * @param: address @PsiType:String.
     */
    private boolean setTargetDevice(int portType, String logicalName, int deviceCategory, String address) {
        try {
            for (Object entry : bxlConfigLoader.getEntries()) {
                JposEntry jposEntry = (JposEntry) entry;
                if (jposEntry.getLogicalName().equals(logicalName)) {
                    bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
                }
            }

            bxlConfigLoader.addEntry(logicalName, deviceCategory, getProductName(logicalName), portType, address);

            bxlConfigLoader.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
            /*if (dialogImprimeVoucherInterface != null){
                dialogImprimeVoucherInterface.onExceptionVoucherBixolon(e.getMessage());
            }*/
            return false;
        }

        return true;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: getProductName.
     *
     * @param name @PsiType:String.
     * @return the product name
     * @Description.
     * @EndDescription.
     */
    private String getProductName(String name) {
        String productName = BXLConfigLoader.PRODUCT_NAME_SPP_R200II;

        if ((name.equals("SPP-R200III"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R200III;
        } else if ((name.equals("SPP-R210"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R210;
        } else if ((name.equals("SPP-R215"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R215;
        } else if ((name.equals("SPP-R220"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R220;
        } else if ((name.equals("SPP-R300"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R300;
        } else if ((name.equals("SPP-R310"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R310;
        } else if ((name.equals("SPP-R318"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R318;
        } else if ((name.equals("SPP-R400"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R400;
        } else if ((name.equals("SPP-R410"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R410;
        } else if ((name.equals("SPP-R418"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_R418;
        } else if ((name.equals("SPP-100II"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_100II;
        } else if ((name.equals("SRP-350IIOBE"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_350IIOBE;
        } else if ((name.equals("SRP-350III"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_350III;
        } else if ((name.equals("SRP-352III"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_352III;
        } else if ((name.equals("SRP-350plusIII"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_350PLUSIII;
        } else if ((name.equals("SRP-352plusIII"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_352PLUSIII;
        } else if ((name.equals("SRP-380"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_380;
        } else if ((name.equals("SRP-382"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_382;
        } else if ((name.equals("SRP-383"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_383;
        } else if ((name.equals("SRP-340II"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_340II;
        } else if ((name.equals("SRP-342II"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_342II;
        } else if ((name.equals("SRP-Q200"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_Q200;
        } else if ((name.equals("SRP-Q300"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_Q300;
        } else if ((name.equals("SRP-Q302"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_Q302;
        } else if ((name.equals("SRP-QE300"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_QE300;
        } else if ((name.equals("SRP-QE302"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_QE302;
        } else if ((name.equals("SRP-E300"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_E300;
        } else if ((name.equals("SRP-E302"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_E302;
        } else if ((name.equals("SRP-B300"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_B300;
        } else if ((name.equals("SRP-330II"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_330II;
        } else if ((name.equals("SRP-332II"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_332II;
        } else if ((name.equals("SRP-S200"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_S200;
        } else if ((name.equals("SRP-S300"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_S300;
        } else if ((name.equals("SRP-S320"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_S320;
        } else if ((name.equals("SRP-S3000"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_S3000;
        } else if ((name.equals("SRP-F310"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_F310;
        } else if ((name.equals("SRP-F312"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_F312;
        } else if ((name.equals("SRP-F310II"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_F310II;
        } else if ((name.equals("SRP-F312II"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_F312II;
        } else if ((name.equals("SRP-F313II"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_F313II;
        } else if ((name.equals("SRP-275III"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SRP_275III;
//        } else if ((name.equals("BK3-2"))) {
//            productName = BXLConfigLoader.PRODUCT_NAME_BK3_2;
        } else if ((name.equals("BK3-3"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_BK3_3;
        } else if ((name.equals("SLP X-Series"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SLP_X_SERIES;
        } else if ((name.equals("SLP-DX420"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SLP_DX420;
        } else if ((name.equals("SPP-L410II"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SPP_L410II;
        } else if ((name.equals("MSR"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_MSR;
        } else if ((name.equals("CashDrawer"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_CASH_DRAWER;
        } else if ((name.equals("LocalSmartCardRW"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_LOCAL_SMART_CARD_RW;
        } else if ((name.equals("SmartCardRW"))) {
            productName = BXLConfigLoader.PRODUCT_NAME_SMART_CARD_RW;
        }

        return productName;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: printText.
     *
     * @param data      @PsiType:String.
     * @param alignment @PsiType:int.
     * @param attribute @PsiType:int.
     * @param textSize  @PsiType:int.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean printText(String data, int alignment, int attribute, int textSize) {
        boolean ret = true;
        attribute = BixolonPrinter.ATTRIBUTE_FONT_D;
        try {
            if (!posPrinter.getDeviceEnabled()) {
                return false;
            }

            String strOption = EscapeSequence.getString(0);

            if ((alignment & ALIGNMENT_LEFT) == ALIGNMENT_LEFT) {
                strOption += EscapeSequence.getString(4);
            }

            if ((alignment & ALIGNMENT_CENTER) == ALIGNMENT_CENTER) {
                strOption += EscapeSequence.getString(5);
            }

            if ((alignment & ALIGNMENT_RIGHT) == ALIGNMENT_RIGHT) {
                strOption += EscapeSequence.getString(6);
            }

            if ((attribute & ATTRIBUTE_FONT_A) == ATTRIBUTE_FONT_A) {
                strOption += EscapeSequence.getString(1);
            }

            if ((attribute & ATTRIBUTE_FONT_B) == ATTRIBUTE_FONT_B) {
                strOption += EscapeSequence.getString(2);
            }

            if ((attribute & ATTRIBUTE_FONT_C) == ATTRIBUTE_FONT_C) {
                strOption += EscapeSequence.getString(3);
            }

            if ((attribute & ATTRIBUTE_FONT_D) == ATTRIBUTE_FONT_D) {
                strOption += EscapeSequence.getString(33);
            }

            if ((attribute & ATTRIBUTE_BOLD) == ATTRIBUTE_BOLD) {
                strOption += EscapeSequence.getString(7);
            }

            if ((attribute & ATTRIBUTE_UNDERLINE) == ATTRIBUTE_UNDERLINE) {
                strOption += EscapeSequence.getString(9);
            }

            if ((attribute & ATTRIBUTE_REVERSE) == ATTRIBUTE_REVERSE) {
                strOption += EscapeSequence.getString(11);
            }
            textSize = 0;

            switch (textSize) {
                case 1:
                    strOption += EscapeSequence.getString(17);
                    strOption += EscapeSequence.getString(26);
//                    strOption += EscapeSequence.getString(25);
                    break;
                case 2:
                    strOption += EscapeSequence.getString(18);
//                    strOption += EscapeSequence.getString(26);
                    strOption += EscapeSequence.getString(25);
                    break;
                case 3:
                    strOption += EscapeSequence.getString(19);
                    strOption += EscapeSequence.getString(27);
                    break;
                case 4:
                    strOption += EscapeSequence.getString(20);
                    strOption += EscapeSequence.getString(28);
                    break;
                case 5:
                    strOption += EscapeSequence.getString(21);
                    strOption += EscapeSequence.getString(29);
                    break;
                case 6:
                    strOption += EscapeSequence.getString(22);
                    strOption += EscapeSequence.getString(30);
                    break;
                case 7:
                    strOption += EscapeSequence.getString(23);
                    strOption += EscapeSequence.getString(31);
                    break;
                case 8:
                    strOption += EscapeSequence.getString(24);
                    strOption += EscapeSequence.getString(32);
                    break;
                default:
                    strOption += EscapeSequence.getString(17);
                    strOption += EscapeSequence.getString(25);
                    break;

            }
            Log.i("Print: ", textSize + "");
            Log.i("Print: ", strOption + "");
            posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, strOption + data);
        } catch (JposException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            UserInteraction.snackyWarning(ContentFragment.activity,null,"Error de comunicación con la impresora");


            /*if (dialogImprimeVoucherInterface != null){
                dialogImprimeVoucherInterface.onExceptionVoucherBixolon(e.getMessage());
            }*/

            ret = false;
        }

        return ret;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: printImage.
     *
     * @param path       @PsiType:String.
     * @param width      @PsiType:int.
     * @param alignment  @PsiType:int.
     * @param brightness @PsiType:int.
     * @param dither     @PsiType:int.
     * @param compress   @PsiType:int.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean printImage(String path, int width, int alignment, int brightness, int dither, int compress) {
        boolean ret = true;

        try {
            if (!posPrinter.getDeviceEnabled()) {
                return false;
            }

            if (alignment == ALIGNMENT_LEFT) {
                alignment = POSPrinterConst.PTR_BM_LEFT;
            } else if (alignment == ALIGNMENT_CENTER) {
                alignment = POSPrinterConst.PTR_BM_CENTER;
            } else {
                alignment = POSPrinterConst.PTR_BM_RIGHT;
            }

            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put((byte) POSPrinterConst.PTR_S_RECEIPT);
            buffer.put((byte) brightness); // brightness
            buffer.put((byte) compress); // compress
            buffer.put((byte) dither); // dither

            posPrinter.printBitmap(buffer.getInt(0), path, width, alignment);
        } catch (JposException e) {
            e.printStackTrace();

            ret = false;
        }

        return ret;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: printImage.
     *
     * @param bitmap     @PsiType:Bitmap.
     * @param width      @PsiType:int.
     * @param alignment  @PsiType:int.
     * @param brightness @PsiType:int.
     * @param dither     @PsiType:int.
     * @param compress   @PsiType:int.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean printImage(Bitmap bitmap, int width, int alignment, int brightness, int dither, int compress) {
        boolean ret = true;

        try {
            if (!posPrinter.getDeviceEnabled()) {
                return false;
            }

            if (alignment == ALIGNMENT_LEFT) {
                alignment = POSPrinterConst.PTR_BM_LEFT;
            } else if (alignment == ALIGNMENT_CENTER) {
                alignment = POSPrinterConst.PTR_BM_CENTER;
            } else {
                alignment = POSPrinterConst.PTR_BM_RIGHT;
            }

            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put((byte) POSPrinterConst.PTR_S_RECEIPT);
            buffer.put((byte) brightness); // brightness
            buffer.put((byte) compress); // compress
            buffer.put((byte) dither); // dither

            posPrinter.printBitmap(buffer.getInt(0), bitmap, width, alignment);
        } catch (JposException e) {
            e.printStackTrace();

            ret = false;
        }

        return ret;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: printBarcode.
     *
     * @param data      @PsiType:String.
     * @param symbology @PsiType:int.
     * @param width     @PsiType:int.
     * @param height    @PsiType:int.
     * @param alignment @PsiType:int.
     * @param hri       @PsiType:int.
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean printBarcode(String data, int symbology, int width, int height, int alignment, int hri) {
        boolean ret = true;

        try {
            if (!posPrinter.getDeviceEnabled()) {
                return false;
            }

            if (alignment == ALIGNMENT_LEFT) {
                alignment = POSPrinterConst.PTR_BC_LEFT;
            } else if (alignment == ALIGNMENT_CENTER) {
                alignment = POSPrinterConst.PTR_BC_CENTER;
            } else {
                alignment = POSPrinterConst.PTR_BC_RIGHT;
            }

            posPrinter.printBarCode(POSPrinterConst.PTR_S_RECEIPT, data, symbology, height, width, alignment, hri);

            dialogImprimeCopiaInterface.onPrintedQR();

        } catch (JposException e) {
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: getPosPrinterInfo.
     *
     * @return the pos printer info
     * @Description.
     * @EndDescription.
     */
    public String getPosPrinterInfo() {
        String info;
        try {
            if (!posPrinter.getDeviceEnabled()) {
                return null;
            }

            info = "deviceServiceDescription: " + posPrinter.getDeviceServiceDescription()
                    + "\ndeviceServiceVersion: " + posPrinter.getDeviceServiceVersion()
                    + "\nphysicalDeviceDescription: " + posPrinter.getPhysicalDeviceDescription()
                    + "\nphysicalDeviceName: " + posPrinter.getPhysicalDeviceName()
                    + "\npowerState: " + getPowerStateString(posPrinter.getPowerState())
                    + "\ncapRecNearEndSensor: " + posPrinter.getCapRecNearEndSensor()
                    + "\nRecPapercut: " + posPrinter.getCapRecPapercut()
                    + "\ncapRecMarkFeed: " + getMarkFeedString(posPrinter.getCapRecMarkFeed())
                    + "\ncharacterSet: " + posPrinter.getCharacterSet()
                    + "\ncharacterSetList: " + posPrinter.getCharacterSetList()
                    + "\nfontTypefaceList: " + posPrinter.getFontTypefaceList()
                    + "\nrecLineChars: " + posPrinter.getRecLineChars()
                    + "\nrecLineCharsList: " + posPrinter.getRecLineCharsList()
                    + "\nrecLineSpacing: " + posPrinter.getRecLineSpacing()
                    + "\nrecLineWidth: " + posPrinter.getRecLineWidth();

            return info;
        } catch (JposException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: StringToHex.
     *
     * @param strScr @PsiType:String.
     * @return byte [ ]
     * @Description.
     * @EndDescription. byte [ ].
     */
    public byte[] StringToHex(String strScr) {
        byte[] src = strScr.getBytes();
        byte[] dst = null;
        int nLength = src.length;

        dst = ConvertHexaToInteger(src, nLength);

        return dst;
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: ConvertHexaToInteger.
     *
     * @param szHexa @PsiType:byte[].
     * @param nSize  @PsiType:int.
     * @return byte [ ]
     * @Description.
     * @EndDescription. byte [ ].
     */
    private byte[] ConvertHexaToInteger(byte[] szHexa, int nSize) {
        ByteBuffer hex = ByteBuffer.allocate(nSize / 2);
        int ch1, ch2, bData;
        int j = 0;

        while (j < nSize) {
            ch1 = ConvertINT(szHexa[j]);
            bData = (byte) (ch1 << 4);
            j++;
            ch2 = ConvertINT(szHexa[j]);
            bData += (ch2 & 0x0f);
            j++;
            hex.put((byte) bData);
        }

        return hex.array();
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: ConvertINT.
     *
     * @param h @PsiType:int.
     * @return byte
     * @Description.
     * @EndDescription. byte.
     */
    private byte ConvertINT(int h) {
        int n = h < 0 ? h + 255 : h;
        return (byte) ((n > '9') ? 10 + (n - 'A') : (n - '0'));
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: errorOccurred.
     *
     * @param errorEvent @PsiType:ErrorEvent.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void errorOccurred(ErrorEvent errorEvent) {

        /*if (dialogImprimeVoucherInterface != null){
            dialogImprimeVoucherInterface.onExceptionVoucherBixolon("Error " + errorEvent);
        }


        if (dialogImprimeCopiaInterface != null){
            dialogImprimeCopiaInterface.onExceptionCopiBixolon("Error " + errorEvent);
        }*/

    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: outputCompleteOccurred.
     *
     * @param outputCompleteEvent @PsiType:OutputCompleteEvent.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void outputCompleteOccurred(OutputCompleteEvent outputCompleteEvent) {

        if (dialogImprimeVoucherInterface != null) {
            dialogImprimeVoucherInterface.onFinishedVoucherBixolon();
        }

        if (dialogImprimeTicketInterface != null) {
            dialogImprimeTicketInterface.onFinishedTicketBixolon();
        }

        if (dialogImprimeCopiaInterface != null) {
            dialogImprimeCopiaInterface.onFinishedCopiaBixolon();
        }

        if (dialogErrorRespuestaInterface != null) {
            dialogErrorRespuestaInterface.onFinishedComprobanteBixolon();
        }


    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: statusUpdateOccurred.
     *
     * @param statusUpdateEvent @PsiType:StatusUpdateEvent.
     * @Description.
     * @EndDescription.
     */
    @Override
    public void statusUpdateOccurred(StatusUpdateEvent statusUpdateEvent) {

        /*if (dialogImprimeVoucherInterface != null){
            dialogImprimeVoucherInterface.onExceptionVoucherBixolon(getSUEMessage(statusUpdateEvent.getStatus()));
        }*/

    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: getSUEMessage.
     *
     * @param status @PsiType:int.
     * @return the sue message
     * @Description.
     * @EndDescription.
     */
    private String getSUEMessage(int status) {
        switch (status) {
            case JposConst.JPOS_SUE_POWER_ONLINE:
                return "StatusUpdate : Power on";

            case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
                printerClose();
                return "StatusUpdate : Power off";

            case POSPrinterConst.PTR_SUE_COVER_OPEN:
                return "StatusUpdate : Cover Open";

            case POSPrinterConst.PTR_SUE_COVER_OK:
                return "StatusUpdate : Cover OK";

            case POSPrinterConst.PTR_SUE_BAT_LOW:
                return "StatusUpdate : Battery-Low";

            case POSPrinterConst.PTR_SUE_BAT_OK:
                return "StatusUpdate : Battery-OK";

            case POSPrinterConst.PTR_SUE_REC_EMPTY:
                return "StatusUpdate : Receipt Paper Empty";

            case POSPrinterConst.PTR_SUE_REC_NEAREMPTY:
                return "StatusUpdate : Receipt Paper Near Empty";

            case POSPrinterConst.PTR_SUE_REC_PAPEROK:
                return "StatusUpdate : Receipt Paper OK";

            case POSPrinterConst.PTR_SUE_IDLE:
                return "StatusUpdate : Printer Idle";

            case POSPrinterConst.PTR_SUE_OFF_LINE:
                return "StatusUpdate : Printer off line";

            case POSPrinterConst.PTR_SUE_ON_LINE:
                return "StatusUpdate : Printer on line";

            default:
                return "StatusUpdate : Unknown";
        }
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: getBatterStatusString.
     *
     * @param status @PsiType:int.
     * @return the batter status string
     * @Description.
     * @EndDescription.
     */
    private String getBatterStatusString(int status) {
        switch (status) {
            case 0x30:
                return "BatterStatus : Full";

            case 0x31:
                return "BatterStatus : High";

            case 0x32:
                return "BatterStatus : Middle";

            case 0x33:
                return "BatterStatus : Low";

            default:
                return "BatterStatus : Unknwon";
        }
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: getPowerStateString.
     *
     * @param powerState @PsiType:int.
     * @return the power state string
     * @Description.
     * @EndDescription.
     */
    private String getPowerStateString(int powerState) {
        switch (powerState) {
            case JposConst.JPOS_PS_OFF_OFFLINE:
                return "OFFLINE";

            case JposConst.JPOS_PS_ONLINE:
                return "ONLINE";

            default:
                return "Unknown";
        }
    }

    /**
     * Type: Method.
     * Parent: BixolonPrinter.
     * Name: getMarkFeedString.
     *
     * @param markFeed @PsiType:int.
     * @return the mark feed string
     * @Description.
     * @EndDescription.
     */
    private String getMarkFeedString(int markFeed) {
        switch (markFeed) {
            case POSPrinterConst.PTR_MF_TO_TAKEUP:
                return "TAKEUP";

            case POSPrinterConst.PTR_MF_TO_CUTTER:
                return "CUTTER";

            case POSPrinterConst.PTR_MF_TO_CURRENT_TOF:
                return "CURRENT TOF";

            case POSPrinterConst.PTR_MF_TO_NEXT_TOF:
                return "NEXT TOF";

            default:
                return "Not support";
        }
    }

}