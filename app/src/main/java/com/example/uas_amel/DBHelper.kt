package com.example.uas_amel

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import android.view.View
import java.sql.SQLException
import java.sql.SQLTimeoutException

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        val DATABASE_NAME = "myaps.db"
        val DATABASE_VERSION = 1
        private val SQL_CREATE_USER = "CREATE TABLE " + DBInfo.UserTable.TABLE_NAME + "("+DBInfo.UserTable.COL_EMAIL+" VARCHAR(200) PRIMARY KEY, " + DBInfo.UserTable.COL_PASS + " TEXT, " + DBInfo.UserTable.COL_FULLNAME + " TEXT, " + DBInfo.UserTable.COL_JENKAL + " VARCHAR(200), " + DBInfo.UserTable.COL_ALAMAT + " TEXT)"
        private val SQL_CREATE_ORDER = "CREATE TABLE " + DBInfo.OrderTable.TABLE_NAME + "(" + DBInfo.OrderTable.COL_ID + " VARCHAR(200) PRIMARY KEY, " + DBInfo.OrderTable.COL_CUSNAME + " TEXT, " + DBInfo.OrderTable.COL_ORNAME + " TEXT, " + DBInfo.OrderTable.COL_TASTE +" TEXT, " + DBInfo.OrderTable.COL_QTY + " INT)"
        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBInfo.UserTable.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_USER)
        db?.execSQL(SQL_CREATE_ORDER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun RegisterUser(emailin: String, passin:String, fullnamein: String, jenkalin: String, alamatin: String) {
        val db = writableDatabase
        val namatable = DBInfo.UserTable.TABLE_NAME
        val emailt = DBInfo.UserTable.COL_EMAIL
        val passt = DBInfo.UserTable.COL_PASS
        val fullnamet = DBInfo.UserTable.COL_FULLNAME
        val jenkalt = DBInfo.UserTable.COL_JENKAL
        val alamatt = DBInfo.UserTable.COL_ALAMAT
        var sql = "INSERT INTO " + namatable + " (" + emailt + ", " + passt + ", " + fullnamet + ", " + jenkalt + ", " + alamatt + ") VALUES('" + emailin + "', '" + passin + "', '" + fullnamein + "', '" + jenkalin + "', '" + alamatin + "')"
        db.execSQL(sql)
    }
    fun cekUser(emailin: String): String {
        val db = writableDatabase
        var cursor: Cursor? = null
        var jumlah = ""
        try {
            cursor = db.rawQuery("SELECT COUNT("+ DBInfo.UserTable.COL_EMAIL +") as jumlah FROM "+ DBInfo.UserTable.TABLE_NAME + " WHERE " + DBInfo.UserTable.COL_EMAIL + "=='" + emailin +"'" , null)
        } catch (e: android.database.SQLException) {
            db.execSQL(SQL_CREATE_USER)
            return "Tabel Dibuat"
        }
        if (cursor!!.moveToFirst()){
            jumlah = cursor.getString(cursor.getColumnIndex(DBInfo.UserTable.COL_JUMLAH))
        }
        return jumlah
    }
    fun cekLogin(emailin: String, passin: String): String {
        val db = writableDatabase
        var cursor: Cursor? = null
        var jumlah = ""
        try {
            cursor = db.rawQuery("SELECT COUNT("+ DBInfo.UserTable.COL_EMAIL +") as jumlah FROM "+ DBInfo.UserTable.TABLE_NAME + " WHERE " + DBInfo.UserTable.COL_EMAIL + "=='" + emailin +"' AND " + DBInfo.UserTable.COL_PASS + "=='" + passin + "'" , null)
        } catch (e: android.database.SQLException) {
            db.execSQL(SQL_CREATE_USER)
            return "Tabel Dibuat"
        }
        if (cursor!!.moveToFirst()){
            jumlah = cursor.getString(cursor.getColumnIndex(DBInfo.UserTable.COL_JUMLAH))
        }
        return jumlah
    }
    @Throws(SQLiteConstraintException::class)
    fun inputOrder(idin: String, cusnamein: String, ornamein: String, tastein: String, qty:String) {
        val db = writableDatabase
        val nametable = DBInfo.OrderTable.TABLE_NAME
        val idn = DBInfo.OrderTable.COL_ID
        val cusnamen = DBInfo.OrderTable.COL_CUSNAME
        val ornamen = DBInfo.OrderTable.COL_ORNAME
        val tasten = DBInfo.OrderTable.COL_TASTE
        val qtyn = DBInfo.OrderTable.COL_QTY
        var sql = "INSERT INTO " + nametable + "( "+idn+", "+cusnamen+", "+ornamen+", "+tasten+", "+qtyn+") VALUES('"+idin+"','"+cusnamein+"','"+ornamein+"','"+tastein+"',"+qty+")"
        db.execSQL(sql)
    }
    fun fullDataa():ArrayList<DBModelOrder>{
        //val caker = ArrayList<DBModelCake>()
        val orderr =  arrayListOf<DBModelOrder>()
        val dbb = writableDatabase
        var cursorr: Cursor? = null
        try{
            cursorr = dbb.rawQuery("SELECT * FROM "+ DBInfo.OrderTable.TABLE_NAME, null)
        }catch (e: android.database.SQLException){
            dbb.execSQL(DBHelper.SQL_CREATE_ORDER)
            return ArrayList()
        }
        var idtt: String
        var namacustt: String
        var namatt: String
        var rasatt: String
        var jumlahtt: String
        if (cursorr!!.moveToFirst()){
            while (cursorr.isAfterLast==false){
                idtt = cursorr.getString(cursorr.getColumnIndex(DBInfo.OrderTable.COL_ID))
                namacustt = cursorr.getString(cursorr.getColumnIndex(DBInfo.OrderTable.COL_CUSNAME))
                namatt = cursorr.getString(cursorr.getColumnIndex(DBInfo.OrderTable.COL_ORNAME))
                rasatt = cursorr.getString(cursorr.getColumnIndex(DBInfo.OrderTable.COL_TASTE))
                jumlahtt = cursorr.getString(cursorr.getColumnIndex(DBInfo.OrderTable.COL_QTY))

                orderr.add(DBModelOrder(idtt, namacustt, namatt, rasatt, jumlahtt))
                cursorr.moveToNext()
            }
        }
        return orderr
    }
    fun deleteData(idinn: String){
        val dbb = writableDatabase
        val namatablett = DBInfo.OrderTable.TABLE_NAME
        val idtt = DBInfo.OrderTable.COL_ID
        val sql = "DELETE FROM " +namatablett+ " WHERE " +idtt+"='"+idinn+"'"
        dbb.execSQL(sql)
    }
    fun updateData(idinn: String, namacusinn: String, namainn: String, rasainn: String, jumlahinn: String){
        val dbb = writableDatabase
        val namatablett = DBInfo.OrderTable.TABLE_NAME
        val idtt = DBInfo.OrderTable.COL_ID
        val namacustt = DBInfo.OrderTable.COL_CUSNAME
        val namatt = DBInfo.OrderTable.COL_ORNAME
        val rasatt = DBInfo.OrderTable.COL_TASTE
        val jumlahtt = DBInfo.OrderTable.COL_QTY
        var sql = "UPDATE "+ namatablett + " SET "+
                namacustt+"='"+namacusinn+"', "+namatt+"='"+namainn+"', "+rasatt+"='"+rasainn+"', "+jumlahtt+"='"+jumlahinn+"' "+
                "WHERE "+idtt+"='"+idinn+"'"
        dbb.execSQL(sql)
    }
}