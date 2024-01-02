package com.clothly.ecommerce.data.data.helper.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;

public abstract class AppDatabase extends RoomDatabase {
    protected static <T extends RoomDatabase> T createDb(Context context, String dbName,
                                                         Class<T> dbService,
                                                         String... migrationScripts) {
        Builder<T> builder = Room.databaseBuilder(context, dbService, dbName);

        for (Migration migration : getMigrations(migrationScripts)) {
            builder.addMigrations(migration);
        }

        return builder.allowMainThreadQueries().build();
    }

    private static List<Migration> getMigrations(String... migrationScripts) {
        List<Migration> migrationList = new ArrayList<>();

        int startVersion = 1;
        int endVersion = 2;

        Migration migration;

        for (final String migrationSchema : migrationScripts) {
            migration = new Migration(startVersion, endVersion) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL(migrationSchema);
                }
            };

            startVersion++;
            endVersion++;

            migrationList.add(migration);
        }

        return migrationList;
    }
}
