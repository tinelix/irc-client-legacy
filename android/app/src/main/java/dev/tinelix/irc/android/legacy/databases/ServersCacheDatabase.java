package dev.tinelix.irc.android.legacy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dev.tinelix.irc.android.legacy.databases.base.CacheDatabase;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;
import dev.tinelix.irc.android.support.models.IRCServer;

public class ServersCacheDatabase extends CacheDatabase {

    private static boolean mIsInitialized;
    private static CacheOpenHelper mHelper;
    private static SQLiteDatabase mDatabase;
    private static String prefix = "servers";

    public static class CacheOpenHelper extends SQLiteOpenHelper {

        public CacheOpenHelper(Context ctx, String db_name) {
            super(ctx, db_name, null, 1);
        }

        public CacheOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            CacheDatabaseTables.createServerTables(database);
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

    public static boolean isInitialized() {
        return mIsInitialized;
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

    public static boolean ifExist(String serverAddr) {
        boolean result = false;

        if(!mIsInitialized)
            return true;

        try {
            String table_name = "servers";

            Cursor cursor = mDatabase.query(table_name, new String[]{"count(*)"},
                    "`address`=?",
                    new String[]{
                            serverAddr,
                    }, null, null, null
            );

            int count = 0;

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            result = count > 0;
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static IRCServer getServer(String address) {
        IRCServer server = null;
        Cursor cursor = null;

        try {
            cursor = mDatabase.query(
                    "servers", null, "address = ?",
                    new String[]{
                            address
                    }, null, null, null
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (cursor != null && cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            cursor.moveToFirst();
            DatabaseUtils.cursorRowToContentValues(cursor, values);

            server = new IRCServer(address);
            server.name = values.getAsString("name");
            server.setInputTextEncoding(values.getAsString("text_in_enc"));
            server.setOutputTextEncoding(values.getAsString("text_out_enc"));
            server.tlsEnabled = values.getAsBoolean("tls_only");
            server.hideIP = values.getAsBoolean("hide_ip");

            cursor.close();
        }

        return server;
    }

    public static void putServer(IRCServer server) {

        if(ifExist(server.address + ":" + Integer.toString(server.port)))
            return;

        String inputEncoding = IRCProfileManager.getTextEncodingCurrentString(server.inputEncoding);
        String outputEncoding = IRCProfileManager.getTextEncodingCurrentString(server.outputEncoding);
        ContentValues values = new ContentValues();

        if(server.name != null)
            values.put("name", server.name);
        else
            values.put("name", server.address);

        values.put("address", server.address + ":" + Integer.toString(server.port));
        values.put("protocol", "irc");

        switch (server.inputEncoding) {
            case IRCServer.TEXT_ENCODING_DOS_ASCII:
                inputEncoding = "cp437";
                break;
            case IRCServer.TEXT_ENCODING_DOS_CYRILLIC:
                inputEncoding = "cp866";
                break;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_BE:
                inputEncoding = "utf-16be";
                break;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_LE:
                inputEncoding = "utf-16le";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_CYRILLIC:
                inputEncoding = "windows-1251";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_WESTERN_EUROPE:
                inputEncoding = "windows-1252";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_GREEK:
                inputEncoding = "windows-1253";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_TURKISH:
                inputEncoding = "windows-1254";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_HEBREW:
                inputEncoding = "windows-1255";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_ARABIC:
                inputEncoding = "windows-1256";
                break;
        }

        switch (server.outputEncoding) {
            case IRCServer.TEXT_ENCODING_DOS_ASCII:
                outputEncoding = "cp437";
                break;
            case IRCServer.TEXT_ENCODING_DOS_CYRILLIC:
                outputEncoding = "cp866";
                break;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_BE:
                outputEncoding = "utf-16be";
                break;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_LE:
                outputEncoding = "utf-16le";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_CYRILLIC:
                outputEncoding = "windows-1251";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_WESTERN_EUROPE:
                outputEncoding = "windows-1252";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_GREEK:
                outputEncoding = "windows-1253";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_TURKISH:
                outputEncoding = "windows-1254";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_HEBREW:
                outputEncoding = "windows-1255";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_ARABIC:
                outputEncoding = "windows-1256";
                break;
        }

        values.put("text_in_enc", inputEncoding);
        values.put("text_out_enc", outputEncoding);
        values.put("tls_only", server.tlsEnabled);
        values.put("hide_ip", server.hideIP);
        mDatabase.insert("servers", null, values);
    }

    public static void updateServer(IRCServer server) {

        if(!ifExist(server.address + ":" + Integer.toString(server.port)))
            return;

        String inputEncoding = "utf-8";
        String outputEncoding = "utf-8";
        ContentValues values = new ContentValues();

        if(server.name != null)
            values.put("name", server.name);
        else
            values.put("name", server.address);

        values.put("address", server.address + ":" + Integer.toString(server.port));
        values.put("protocol", "irc");

        switch (server.inputEncoding) {
            case IRCServer.TEXT_ENCODING_DOS_ASCII:
                inputEncoding = "cp437";
                break;
            case IRCServer.TEXT_ENCODING_DOS_CYRILLIC:
                inputEncoding = "cp866";
                break;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_BE:
                inputEncoding = "utf-16be";
                break;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_LE:
                inputEncoding = "utf-16le";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_CYRILLIC:
                inputEncoding = "windows-1251";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_WESTERN_EUROPE:
                inputEncoding = "windows-1252";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_GREEK:
                inputEncoding = "windows-1253";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_TURKISH:
                inputEncoding = "windows-1254";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_HEBREW:
                inputEncoding = "windows-1255";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_ARABIC:
                inputEncoding = "windows-1256";
                break;
        }

        switch (server.outputEncoding) {
            case IRCServer.TEXT_ENCODING_DOS_ASCII:
                outputEncoding = "cp437";
                break;
            case IRCServer.TEXT_ENCODING_DOS_CYRILLIC:
                outputEncoding = "cp866";
                break;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_BE:
                outputEncoding = "utf-16be";
                break;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_LE:
                outputEncoding = "utf-16le";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_CYRILLIC:
                outputEncoding = "windows-1251";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_WESTERN_EUROPE:
                outputEncoding = "windows-1252";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_GREEK:
                outputEncoding = "windows-1253";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_TURKISH:
                outputEncoding = "windows-1254";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_HEBREW:
                outputEncoding = "windows-1255";
                break;
            case IRCServer.TEXT_ENCODING_WINDOWS_ARABIC:
                outputEncoding = "windows-1256";
                break;
        }

        values.put("text_in_enc", inputEncoding);
        values.put("text_out_enc", outputEncoding);

        values.put("tls_only", server.tlsEnabled);
        values.put("hide_ip", server.hideIP);

        mDatabase.update(
                "servers", values, "address = ?",
                new String[]{
                        server.address + ":" + Integer.toString(server.port)
                }
        );
    }

    public static void putMOTD(String motd, long time, String address) {

        ContentValues serverValues = new ContentValues();

        if(ifMOTDExist(motd, address)) {
            return;
        }

        Cursor cursor = mDatabase.query(
                "servers",
                new String[]{"server_id"},
                "address = ?",
                new String[]{
                        address
                },
                null, null, null, null
        );

        cursor.moveToFirst();
        DatabaseUtils.cursorRowToContentValues(cursor, serverValues);

        ContentValues motdValues = new ContentValues();

        motdValues.put("server_id", serverValues.getAsInteger("server_id"));
        motdValues.put("content", motd);
        motdValues.put("time", time);

        mDatabase.insert("motd", null, motdValues);
    }

    private static boolean ifMOTDExist(String motd, String address) {
        boolean result = false;

        if(!mIsInitialized)
            return true;

        try {
            Cursor cursor = mDatabase.rawQuery(
                    "SELECT count(*) FROM `motd` " +
                    "JOIN servers ON motd.server_id = servers.server_id " +
                    "WHERE `content` = ? and `address` = ?",
                    new String[] {
                            motd, address
                    }
            );

            int count = 0;

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            result = count > 0;
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
