package minjarez.oscar.myfeelings.utilities

import android.content.Context
import android.util.Log
import java.io.IOException

class JsonFile {

    val MY_FEELINGS = "data.json"

    constructor() {

    }

    fun saveData(context: Context, json: String) {
        try {
            context.openFileOutput(this.MY_FEELINGS, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            Log.e("SAVE", "Error in Writing: " + e.localizedMessage)
        }
    }

    fun getData(context: Context): String {
        try {
            return context.openFileInput(this.MY_FEELINGS).bufferedReader().readLine()
        } catch (e: IOException) {
            Log.e("GET", "Error in fetching data: " + e.localizedMessage)
            return ""
        }
    }
}