package dev.tinelix.irc.android.legacy.databases.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import dev.tinelix.irc.android.legacy.IRCApplication;
import dev.tinelix.irc.android.legacy.databases.CacheDatabaseTables;

public class CacheDatabase {

    private static String prefix = "";

    public static class CacheOpenHelper extends SQLiteOpenHelper {

        public CacheOpenHelper(
                Context context, String name,
                SQLiteDatabase.CursorFactory factory, int version
        ) {
            super(context, name, factory, version);
        }

        public CacheOpenHelper(
                Context context, String name, int version,
                SQLiteDatabase.OpenParams openParams
        ) {
            super(context, name, version, openParams);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            switch (prefix) {
                case "servers":
                    CacheDatabaseTables.createServerTables(db);
                    break;
                case "users":
                    CacheDatabaseTables.createUserTables(db);
                    break;
                case "channels":
                    CacheDatabaseTables.createChannelTables(db);
                    break;
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public static File getCurrentDatabasePath(Context ctx, String prefix, String profileId) {
        return ctx.getDatabasePath(String.format("%s_%s.db", prefix, profileId));
    }

    public static String getCurrentDatabaseName(String prefix, String profileId) {
        try {
            return String.format("%s_%s.db", prefix, profileId);
        } catch (Exception e) {
            return null;
        }
    }
}
