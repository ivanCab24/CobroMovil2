package com.Utilities

import android.util.Log
import com.Constants.ConstantsPreferences
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class Curl{
    companion object{
        fun sendPostRequest(params:Map<String,String>, method:String,preferenceHelper: PreferenceHelper) {
            var reqParam = ""
            for (key in params.keys){
                reqParam+="$key=${params[key]}&&"
            }
            println(reqParam)
            val mURL = URL("http://${preferenceHelper.getString(ConstantsPreferences.PREF_SERVER, null)}/ws_pagomovil/ws_pagomovil.asmx/$method")
            with(mURL.openConnection() as HttpURLConnection) {
                // optional default is GET
                requestMethod = "POST"
                val wr = OutputStreamWriter(getOutputStream());
                wr.write(reqParam);
                wr.flush();
                println("URL : $url")
                println("Response Code : $responseCode")
                Log.i("dsdsd", responseCode.toString())
                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    println("Response : $response")
                    Log.i("dsdsd", response.toString())
                }
            }
        }

        fun sendPostRequest(params:Map<String,String>, method:String,preferenceHelper: PreferenceHelper,bandera:Int):String {
            var reqParam = ""
            for (key in params.keys){
                reqParam+="$key=${params[key]}&&"
            }
            Log.i(method,reqParam);
            val mURL = URL("http://${preferenceHelper.getString(ConstantsPreferences.PREF_SERVER, null)}/ws_pagomovil/ws_pagomovil.asmx/$method")
            with(mURL.openConnection() as HttpURLConnection) {
                // optional default is GET
                requestMethod = "POST"
                val wr = OutputStreamWriter(outputStream);
                wr.write(reqParam);
                wr.flush();
                println("URL : $url")
                println("Response Code : $responseCode")
                Log.i("dsdsd", responseCode.toString())
                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    println("Response : $response")
                    return response.substring(response.indexOf("["),response.indexOf("]")+1)
                }
            }
        }

        fun sendPostRequest(params:Map<String,String>, method:String,preferenceHelper: PreferenceHelper,ws:String): Document? {
            Log.i(method,params.toString())
            var reqParam = ""
            for (key in params.keys){
                reqParam+="$key=${params[key]}&&"
            }
            try{
                val mURL = URL("http://${preferenceHelper.getString(ConstantsPreferences.PREF_SERVER, null)}/$ws/$method")
                Log.i("URL",mURL.toString())
                with(mURL.openConnection() as HttpURLConnection) {
                    // optional default is GET
                    requestMethod = "POST"
                    val wr = OutputStreamWriter(outputStream);

                    wr.write(reqParam);
                    wr.flush();
                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()
                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        val doc: Document = convertStringToXMLDocument(response.toString())!!
                        Log.i(method,doc.firstChild.firstChild.textContent)
                        return doc
                        //Log.i(method,doc.firstChild.firstChild.textContent)
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
                return null
            }

        }
        private fun convertStringToXMLDocument(xmlString: String): Document? {
            //Parser that produces DOM object trees from XML content
            val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()

            //API to obtain DOM Document instance
            var builder: DocumentBuilder? = null
            try {
                //Create DocumentBuilder with default configuration
                builder = factory.newDocumentBuilder()

                //Parse the content to Document object
                return builder.parse(InputSource(StringReader(xmlString)))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }



        fun sendPostRequest(params:Map<String,String>, method:String,preferenceHelper: PreferenceHelper,ws:String,output:Boolean): Document? {
            Log.i(method,params.toString())
            var reqParam = ""
            for (key in params.keys){
                reqParam+="$key=${params[key]}&&"
            }
            try{
                val mURL = URL("http://${preferenceHelper.getString(ConstantsPreferences.PREF_SERVER, null)}/$ws/$method")
                Log.i("URL",mURL.toString())
                with(mURL.openConnection() as HttpURLConnection) {
                    // optional default is GET
                    requestMethod = "POST"
                    val wr = OutputStreamWriter(outputStream);
                    //doOutput=false
                    connect()
                    wr.write(reqParam);
                    wr.flush()
                    println("URL : $url")
                    println("Response Code : $responseCode")
                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()
                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        val doc: Document = convertStringToXMLDocument(response.toString())!!
                        Log.i(method,doc.firstChild.firstChild.textContent)
                        return doc
                        //Log.i(method,doc.firstChild.firstChild.textContent)
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
                return null
            }

        }
    }
}