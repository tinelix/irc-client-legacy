package dev.tinelix.irc.android.legacy.databases;

import android.database.sqlite.SQLiteDatabase;

public class CacheDatabaseTables {

    public static void createServerTables(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS `servers`");

        db.execSQL(
                "CREATE TABLE `servers` (" +
                    "server_id    integer      PRIMARY KEY AUTOINCREMENT, " +
                    "name         varchar(60),                            " +
                    "address      varchar(255) UNIQUE NOT NULL,           " +
                    "protocol     varchar(16)  NOT NULL,                  " +
                    "tls_only     bit          NOT NULL,                  " +
                    "hide_ip      bit          NOT NULL,                  " +
                    "text_in_enc  varchar(25)  NOT NULL,                  " +
                    "text_out_enc varchar(25)  NOT NULL                   " +
                ")"
        );
    }

    public static void createUserTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `contacts`");
        db.execSQL(
                "CREATE TABLE `contacts` (" +
                        "contact_id integer     PRIMARY KEY AUTOINCREMENT, " +
                        "first_name varchar(50),                           " +
                        "last_name  varchar(50),                           " +
                        "sur_name   varchar(50),                           " +
                        "birthday   text                                   " +
                ")"
        );

        db.execSQL("DROP TABLE IF EXISTS `user_relations`");
        db.execSQL(
                "CREATE TABLE `user_relations` (" +
                        "relation_id  bigint PRIMARY KEY NOT NULL, " +
                        "user_id      bigint NOT NULL,             " +
                        "server_id    bigint NOT NULL,             " +
                        "contact_id   bigint NOT NULL              " +
                ")"
        );

        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL(
                "CREATE TABLE `users` (" +
                    "user_id    bigint       NOT NULL,        " +
                    "server_id  bigint       NOT NULL,        " +
                    "nickname   varchar(100) NOT NULL,        " +
                    "realname   varchar(255) NOT NULL,        " +
                    "registered bit          NOT NULL,        " +
                    "PRIMARY KEY (user_id, server_id),        " +
                    "UNIQUE (nickname, server_id)             " +
                 ")"
        );

        db.execSQL("DROP TABLE IF EXISTS `nicknames`");
        db.execSQL(
                "CREATE TABLE `nicknames` (" +
                    "nick_id    integer      PRIMARY KEY AUTOINCREMENT, " +
                    "nickname   varchar(100) UNIQUE,                    " +
                    "is_primary bit                                     " +
                ")"
        );
    }

    public static void createChannelTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `channels`");
        db.execSQL(
                "CREATE TABLE `channels` (" +
                        "channel_id bigint       NOT NULL,  " +
                        "server_id  bigint       NOT NULL,  " +
                        "address    varchar(40)  NOT NULL,  " +
                        "topic      varchar(800) NOT NULL,  " +
                        "registered bit          NOT NULL,  " +
                        "PRIMARY KEY (channel_id, server_id)" +
                ")"
        );
    }
}
