package com.DI.Modules

import android.content.Context
import androidx.room.Room
import com.Constants.ConstantsBD
import com.Controller.BD.DAO.*
import com.Controller.BD.Database.BinesDatabase
import com.DI.Scopes.AppComponentScope
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

    @AppComponentScope
    @Provides
    fun providesRoomDatabase(context: Context): BinesDatabase =
        Room.databaseBuilder(context, BinesDatabase::class.java, ConstantsBD.NAME_DATABASE)
            .allowMainThreadQueries()
            .addMigrations(BinesDatabase.MIGRATION_1_2)
            .addMigrations(BinesDatabase.MIGRATION_2_3)
            .addMigrations(BinesDatabase.MIGRATION_3_4)
            .addMigrations(BinesDatabase.MIGRATION_4_5)
            .build()

    @AppComponentScope
    @Provides
    fun providesCatalogoBinesDao(binesDatabase: BinesDatabase): CatalogoBinesDAO =
        binesDatabase.catalogoBinesDAO()

    @AppComponentScope
    @Provides
    fun providesCatalogoFpgoDao(binesDatabase: BinesDatabase): MFPGODAO =
        binesDatabase.catalogoFpgoDAO()

    @AppComponentScope
    @Provides
    fun providesCuentaCerradaDao(binesDatabase: BinesDatabase): CuentaCerradaDAO =
        binesDatabase.cuentaCerradaDAO()

    @AppComponentScope
    @Provides
    fun providesCuentaCobradaDao(binesDatabase: BinesDatabase): CuentaCobradaDAO =
        binesDatabase.cuentaCobradaDAO()

    @AppComponentScope
    @Provides
    fun providesTicketNoRegistradosDao(binesDatabase: BinesDatabase): TicketsNoRegistradosDAO =
        binesDatabase.ticketsNoRegistradosDAO()

    @AppComponentScope
    @Provides
    fun providesUserDao(binesDatabase: BinesDatabase): UserDAO = binesDatabase.userDAO()


}