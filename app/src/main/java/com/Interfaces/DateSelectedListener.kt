package com.Interfaces

/**
 * Type: Interface.
 * Parent: DateSelectedListener.java.
 * Name: DateSelectedListener.
 */
interface DateSelectedListener {
    /**
     * Type: Method.
     * Parent: DateSelectedListener.
     * Name: selectedDate.
     *
     * @param date @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun selectedDate(date: String?)

    /**
     * Type: Method.
     * Parent: DateSelectedListener.
     * Name: selectedDateCancel.
     *
     * @Description.
     * @EndDescription.
     */
    fun selectedDateCancel()
}