package io.ugommirikwe.auctions.util;

import android.provider.BaseColumns;

public final class AuctionsDBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public AuctionsDBContract() {}

    /* Inner class that defines the table contents */
    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_FULLNAME = "fullname";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    public static abstract class Auction implements BaseColumns {
        public static final String TABLE_NAME = "auctions";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_STOP_TIME = "stop_time";
        public static final String COLUMN_NAME_SUBMITTED_BY = "submitted_by";
        public static final String COLUMN_NAME_ITEM_TITLE = "item_title";
        public static final String COLUMN_NAME_ITEM_DESCRIPTION = "item_description";
        public static final String COLUMN_NAME_ITEM_PHOTO_URI = "item_photo_uri";
        public static final String COLUMN_NAME_ITEM_MINIMUM_BID = "item_minimum_bid";
    }

    public static abstract class AuctionBid implements BaseColumns {
        public static final String TABLE_NAME = "auction_bids";
        public static final String COLUMN_NAME_AUCTION_ID = "auction_id";
        public static final String COLUMN_NAME_BID_TIME = "bid_time";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_BID_AMOUNT = "bid_amount";
    }
}
