package com.Interfaces

/**
 * Type: Interface.
 * Parent: TimeSelectedListener.java.
 * Name: TimeSelectedListener.
 */
interface TimeSelectedListener {
    /**
     * Type: Method.
     * Parent: TimeSelectedListener.
     * Name: onTimeSelected.
     *
     * @param hora @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun onTimeSelected(hora: String?)

    /**
     * Type: Method.
     * Parent: TimeSelectedListener.
     * Name: onTimeCancelSelected.
     *
     * @Description.
     * @EndDescription.
     */
    fun onTimeCancelSelected()
}