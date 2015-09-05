package io.ugommirikwe.auctions.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static io.ugommirikwe.auctions.util.AuctionsDBContract.*;

public class AuctionsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Auctions.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DATETIME_TYPE = " DATETIME";
    private static final String COMMA_SEP = ",";

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User._ID + " INTEGER PRIMARY KEY," +
                    User.COLUMN_NAME_FULLNAME + TEXT_TYPE + COMMA_SEP +
                    User.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    User.COLUMN_NAME_PASSWORD + TEXT_TYPE +
                    " )";
    private static final String CREATE_AUCTION_TABLE =
            "CREATE TABLE " + Auction.TABLE_NAME + " (" +
                    Auction._ID + " INTEGER PRIMARY KEY," +
                    Auction.COLUMN_NAME_START_TIME + DATETIME_TYPE + COMMA_SEP +
                    Auction.COLUMN_NAME_STOP_TIME + DATETIME_TYPE + COMMA_SEP +
                    Auction.COLUMN_NAME_SUBMITTED_BY + INTEGER_TYPE + COMMA_SEP +
                    Auction.COLUMN_NAME_ITEM_TITLE + TEXT_TYPE + COMMA_SEP +
                    Auction.COLUMN_NAME_ITEM_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Auction.COLUMN_NAME_ITEM_PHOTO_URI + TEXT_TYPE + COMMA_SEP +
                    Auction.COLUMN_NAME_ITEM_MINIMUM_BID + TEXT_TYPE +
                    " )";
    private static final String CREATE_AUCTION_BID_TABLE =
            "CREATE TABLE " + AuctionBid.TABLE_NAME + " (" +
                    AuctionBid._ID + " INTEGER PRIMARY KEY," +
                    AuctionBid.COLUMN_NAME_AUCTION_ID + TEXT_TYPE +
                    AuctionBid.COLUMN_NAME_BID_TIME + DATETIME_TYPE + COMMA_SEP +
                    AuctionBid.COLUMN_NAME_BID_AMOUNT + TEXT_TYPE +
                    " )";

    //private static final String SQL_DELETE_ENTRIES = "DELETE";

    public AuctionsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_AUCTION_TABLE);
        db.execSQL(CREATE_AUCTION_BID_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply discard the data and start over
        //db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL("DROP TABLE IF EXISTS "+ AuctionBid.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ Auction.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ User.TABLE_NAME);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
