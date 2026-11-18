package dev.tinelix.irc.android.legacy.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dev.tinelix.irc.android.legacy.databases.base.CacheDatabase;

public class ChannelsCacheDatabase extends CacheDatabase {

    private static boolean mIsInitialized;
    private static CacheOpenHelper mHelper;
    private static SQLiteDatabase mDatabase;
    private static String prefix = "channels";

    public static class CacheOpenHelper extends SQLiteOpenHelper {

        public CacheOpenHelper(Context ctx, String db_name) {
            super(ctx, db_name, null, 1);
        }

        public CacheOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            CacheDatabaseTables.createChannelTables(database);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
            if (oldVer == 1 && newVer >= oldVer) {
                // TODO: Add database auto-upgrade to new versions
                return;
            }
            onCreate(database);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public SQLiteDatabase getWritableDatabase() {
            while (true) {
                try {
                    SQLiteDatabase db = super.getWritableDatabase();
                    db.setLockingEnabled(false);
                    return db;
                } catch (Exception ex) {
                    try {
                        Thread.sleep(100L);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public SQLiteDatabase getReadableDatabase() {
            while (true) {
                try {
                    SQLiteDatabase db = super.getReadableDatabase();
                    db.setLockingEnabled(false);
                    return db;
                } catch (Exception ex) {
                    try {
                        Thread.sleep(100L);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    public static void initDatabase(Context ctx, String profileId) {
        if(mIsInitialized || (mHelper != null && mDatabase != null && mDatabase.isOpen())) {
            mIsInitialized = true;
            return;
        }

        mHelper = new CacheOpenHelper(ctx, getCurrentDatabaseName(prefix, profileId));
        mDatabase = mHelper.getWritableDatabase();
        mIsInitialized = true;
    }

    public static void freeDatabase() {
        if(mDatabase != null && mDatabase.isOpen() && mIsInitialized) {
            mDatabase.close();
            mDatabase = null;
            mHelper.close();
            mHelper = null;
            mIsInitialized = false;
        }
    }
}
