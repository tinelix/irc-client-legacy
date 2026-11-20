/*
 * Copyleft © 2022, 2026 Dmitry Tretyakov (aka. Tinelix)
 *
 * This file is part of Tinelix IRC Client for Android.
 *
 * Tinelix IRC Client is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see https://www.gnu.org/licenses/.
 *
 * Source code: https://github.com/tinelix/irc-client-legacy
 */

package dev.tinelix.irc.android.legacy.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.preference.Preference;

import java.util.ArrayList;

import dev.tinelix.irc.android.legacy.Global;
import dev.tinelix.irc.android.legacy.databases.base.CacheDatabase;
import dev.tinelix.irc.android.legacy.utils.AESCipher;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class UsersCacheDatabase extends CacheDatabase {

    private static boolean mIsInitialized;
    private static CacheOpenHelper mHelper;
    private static SQLiteDatabase mDatabase;
    private static String prefix = "users";

    public static class CacheOpenHelper extends SQLiteOpenHelper {

        public CacheOpenHelper(Context ctx, String db_name) {
            super(ctx, db_name, null, 1);
        }

        public CacheOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            CacheDatabaseTables.createUserTables(database);
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

    public static boolean ifExist(String nickname) {
        boolean result = false;

        if(!mIsInitialized)
            return true;

        try {
            String table_name = "nicknames";

            Cursor cursor = mDatabase.query(table_name, new String[]{"count(*)"},
                    "`nickname`=?",
                    new String[]{
                            nickname,
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

    public static void encryptPassword(ServerProfile.Nickname nickname, String plainPassword) {
        try {
            AESCipher cipher = AESCipher.createInstance(nickname.getPrivateKey());

            if(cipher == null) return;

            String encPassword = cipher.encrypt(plainPassword);

            ContentValues values = new ContentValues();
            nickname.setEncryptedPassword(encPassword);
            values.put("enc_password", encPassword);

            mDatabase.execSQL(
                    "UPDATE nicknames SET enc_password = ? WHERE nickname = ?",
                    new String[] {
                            encPassword, nickname.nickname
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decryptPassword(ServerProfile.Nickname nickname) {
        try {
            AESCipher cipher = AESCipher.createInstance(nickname.getPrivateKey());

            String encPasswd = nickname.getEncryptedPassword();

            if(cipher == null || encPasswd == null || encPasswd.length() < 32) return null;

            return cipher.decrypt(encPasswd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ServerProfile.Nickname> getNicknameList() {
        ArrayList<ServerProfile.Nickname> nicknames = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = mDatabase.query(
                    "nicknames", null, null,
                    null, null, null, null
            );

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    ContentValues values = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, values);
                    ServerProfile.Nickname nickname = new ServerProfile.Nickname(
                        values.getAsString("nickname"), values.getAsBoolean("is_primary")
                    );
                    nickname.setPrivateKey(values.getAsString("private_key"));
                    nickname.setEncryptedPassword(values.getAsString("enc_password"));
                    nicknames.add(nickname);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(cursor != null)
            cursor.close();

        return nicknames;
    }

    public static void putNickname(String prefsName, ServerProfile.Nickname nickname) {

        if (ifExist(nickname.nickname))
            return;

        ContentValues values = new ContentValues();
        values.put("nickname", nickname.nickname);
        values.put("is_primary", nickname.isPrimary);
        values.put("private_key", IRCProfileManager.generatePrivateKey(prefsName, nickname.nickname));
        mDatabase.insert(
                "nicknames", null, values
        );
    }

    public static void deleteNickname(ServerProfile.Nickname nickname) {

        if(!ifExist(nickname.nickname))
            return;

        mDatabase.delete(
                "nicknames", "nickname = ?",
                new String[] {nickname.nickname}
        );
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
