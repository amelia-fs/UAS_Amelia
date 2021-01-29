package com.example.uas_amel

import android.provider.BaseColumns

object DBInfo {
    class UserTable: BaseColumns {
        companion object {
//            Tabel User
            val TABLE_NAME = "user"
            val COL_EMAIL = "email"
            val COL_PASS = "pass"
            val COL_FULLNAME = "fullname"
            val COL_ALAMAT = "alamat"
            val COL_JENKAL = "jeniskelamin"
            val COL_JUMLAH = "jumlah"

        }
    }
    class OrderTable: BaseColumns {
        companion object{
            val TABLE_NAME = "orderan"
            val COL_ID = "idorder"
            val COL_CUSNAME = "cusname"
            val COL_ORNAME = "orname"
            val COL_TASTE = "taste"
            val COL_QTY = "qty"
        }
    }
}