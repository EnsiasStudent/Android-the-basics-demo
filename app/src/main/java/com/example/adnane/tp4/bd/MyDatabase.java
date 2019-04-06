package com.example.adnane.tp4.bd;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class, Etablissement.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract Usr_dao mydao();
    public abstract Etab_dao mydao2();
}
