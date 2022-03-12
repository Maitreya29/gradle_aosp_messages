package com.android.messaging.datamodel.action;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.android.messaging.datamodel.BugleDatabaseOperations;
import com.android.messaging.datamodel.DataModel;
import com.android.messaging.datamodel.DatabaseWrapper;
import com.android.messaging.datamodel.MessagingContentProvider;
import com.android.messaging.datamodel.data.MessageData;
import com.android.messaging.datamodel.data.MessagePartData;
import com.android.messaging.datamodel.data.ParticipantData;
import com.android.messaging.util.Assert;
import com.android.messaging.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Action used to search for a message.
 */

public class SearchAction extends Action implements Parcelable {

    private static final String TAG = LogUtil.BUGLE_DATAMODEL_TAG;


    /**
     * Add message to database in pending state and queue actual sending
     */
    @Override
    protected Object executeAction() {
        String query = actionParameters.getString(KEY_QUERY);
        return doSearch(query);
    }

    /**
     * Search for a message.
     */
    private MessageData[] doSearch(final String query) {
        final DatabaseWrapper db = DataModel.get().getDatabase();
        db.beginTransaction();
        ArrayList<MessageData> al = new ArrayList<MessageData>();
        final List<MessagePartData> attachmentsUpdated = new ArrayList<>();
        try {
            Cursor c = BugleDatabaseOperations.searchMessages(db, query, 12);
            final int count = c.getCount();
            for (int i = 0; i < count; i++) {
                c.moveToNext();
                MessageData messageData = new MessageData();
                messageData.bind(c);
                Log.d(TAG, messageData.getMessageText());
                al.add(messageData);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return (MessageData[]) al.toArray();
    }

    protected SearchAction(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchAction> CREATOR = new Creator<SearchAction>() {
        @Override
        public SearchAction createFromParcel(Parcel in) {
            return new SearchAction(in);
        }

        @Override
        public SearchAction[] newArray(int size) {
            return new SearchAction[size];
        }
    };

    @Override
    public void writeToParcel(final Parcel parcel, final int flags) {
        writeActionToParcel(parcel, flags);
    }

    private static final String KEY_QUERY = "query";

    private SearchAction(final String query) {
        super();
        actionParameters.putString(KEY_QUERY, query);
    }


    public static void search(final String query) {
        final SearchAction action = new SearchAction(query);
        action.start();
    }
}
