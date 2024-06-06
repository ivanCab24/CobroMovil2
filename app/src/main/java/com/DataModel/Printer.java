package com.DataModel;

/**
 * Type: Class.
 * Access: Public.
 * Name: Printer.
 *
 * @Description.
 * @EndDescription.
 */
public class Printer {

    /**
     * The Printer name.
     */
    private String printerName;
    /**
     * The Printer bt address.
     */
    private String printerBTAddress;

    /**
     * Type: Method.
     * Parent: Printer.
     * Name: Printer.
     *
     * @param printerName      @PsiType:String.
     * @param printerBTAddress @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public Printer(String printerName, String printerBTAddress) {
        this.printerName = printerName;
        this.printerBTAddress = printerBTAddress;
    }

    /**
     * Type: Method.
     * Parent: Printer.
     * Name: getPrinterName.
     *
     * @return the printer name
     * @Description.
     * @EndDescription.
     */
    public String getPrinterName() {
        return printerName;
    }

    /**
     * Type: Method.
     * Parent: Printer.
     * Name: setPrinterName.
     *
     * @Description.
     * @EndDescription.
     * @param: printerName @PsiType:String.
     */
    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    /**
     * Type: Method.
     * Parent: Printer.
     * Name: getPrinterBTAddress.
     *
     * @return the printer bt address
     * @Description.
     * @EndDescription.
     */
    public String getPrinterBTAddress() {
        return printerBTAddress;
    }

    /**
     * Type: Method.
     * Parent: Printer.
     * Name: setPrinterBTAddress.
     *
     * @Description.
     * @EndDescription.
     * @param: printerBTAddress @PsiType:String.
     */
    public void setPrinterBTAddress(String printerBTAddress) {
        this.printerBTAddress = printerBTAddress;
    }

    /**
     * Type: Method.
     * Parent: Printer.
     * Name: toString.
     *
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    @Override
    public String toString() {
        return "Printer{" +
                "printerName='" + printerName + '\'' +
                ", printerBTAddress='" + printerBTAddress + '\'' +
                '}';
    }
}
