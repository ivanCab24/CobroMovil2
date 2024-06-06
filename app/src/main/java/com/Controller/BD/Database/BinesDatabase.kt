package com.Controller.BD.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.Constants.ConstantsBD
import com.Controller.BD.DAO.*
import com.Controller.BD.Entity.*

@Database(
    entities = [MBCO2::class, MFPGO::class, CuentaCerrada::class, CuentaCobrada::class, TicketNoInsertados::class, UserEntity::class],
    version = 5
)
abstract class BinesDatabase : RoomDatabase() {

    abstract fun catalogoBinesDAO(): CatalogoBinesDAO
    abstract fun catalogoFpgoDAO(): MFPGODAO
    abstract fun cuentaCerradaDAO(): CuentaCerradaDAO
    abstract fun cuentaCobradaDAO(): CuentaCobradaDAO
    abstract fun ticketsNoRegistradosDAO(): TicketsNoRegistradosDAO
    abstract fun userDAO(): UserDAO

    companion object {

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE " + ConstantsBD.TABLE_CUENTA_CERRADA + " (id INTEGER PRIMARY KEY NOT NULL, DAY INTEGER NOT NULL,  MONTH INTEGER NOT NULL, YEAR INTEGER NOT NULL, HOUR INTEGER NOT NULL, MINUTES INTEGER NOT NULL, SECONDS INTEGER NOT NULL, ESTAFETA TEXT, NOMBRE TEXT, FECHA INTEGER NOT NULL, ID_LOCAL INTEGER NOT NULL, ID_COMA INTEGER NOT NULL, ID_POS INTEGER NOT NULL," +
                            " SALDO REAL NOT NULL, M_TOTAL REAL NOT NULL, M_IMPORTE REAL NOT NULL, M_DESC REAL NOT NULL, DESC_TIPO INTEGER NOT NULL, DESC_ID INTEGER NOT NULL, REFERENCIA INTEGER NOT NULL, CERRADA INTEGER NOT NULL, PUEDECERRAR INTEGER NOT NULL, FACTURADA INTEGER NOT NULL, IMPRIME INTEGER NOT NULL, FOLIOCTA TEXT, NUMCOMENSALES INTEGER NOT NULL, campo32 TEXT)"
                )
                database.execSQL("CREATE TABLE " + ConstantsBD.TABLE_CUENTA_COBRADA + " (id INTEGER PRIMARY KEY NOT NULL, DAY INTEGER NOT NULL,  MONTH INTEGER NOT NULL, YEAR INTEGER NOT NULL, HOUR INTEGER NOT NULL, MINUTES INTEGER NOT NULL, SECONDS INTEGER NOT NULL, NOMBRE TEXT, CONSUMO REAL NOT NULL, PROPINA REAL NOT NULL, ESTAFETA TEXT, FOLIO TEXT, campo32 TEXT, autorizacion TEXT, MESA TEXT)")
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE " + ConstantsBD.TABLE_CUENTA_COBRADA + " ADD COLUMN PAN TEXT")
            }
        }

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE " + ConstantsBD.TABLE_TICKET_NO_INSERTADO + " (id INTEGER PRIMARY KEY NOT NULL, ticket TEXT NOT NULL, monto INTEGER NOT NULL, membresia TEXT NOT NULL, marca TEXT NOT NULL, fechaConsumo TEXT NOT NULL)")
            }
        }

        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE " + ConstantsBD.TABLE_USER + " (ESTAFETA INTEGER PRIMARY KEY, PASSWORD INTEGER, NOMBRE TEXT, APP INTEGER, INTFALLOS INTEGER, MONTH INTEGER NOT NULL)")
            }
        }

    }
}