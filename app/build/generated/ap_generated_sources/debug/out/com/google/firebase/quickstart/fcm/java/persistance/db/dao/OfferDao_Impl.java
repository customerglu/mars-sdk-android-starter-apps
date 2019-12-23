package com.google.firebase.quickstart.fcm.java.persistance.db.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.google.firebase.quickstart.fcm.java.persistance.db.entity.Offer;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class OfferDao_Impl implements OfferDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Offer> __insertionAdapterOfOffer;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public OfferDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOffer = new EntityInsertionAdapter<Offer>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `offer_table` (`status`,`userId`,`offerId`,`createdAt`,`updatedAt`,`templateUrl`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Offer value) {
        if (value.getStatus() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getStatus());
        }
        if (value.getUserId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUserId());
        }
        if (value.getOfferId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getOfferId());
        }
        if (value.getCreatedAt() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getCreatedAt());
        }
        if (value.getUpdatedAt() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getUpdatedAt());
        }
        if (value.getTemplateUrl() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getTemplateUrl());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM offer_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Offer offer) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfOffer.insert(offer);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Offer>> getAllOffers() {
    final String _sql = "SELECT * from offer_table ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"offer_table"}, false, new Callable<List<Offer>>() {
      @Override
      public List<Offer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOfferId = CursorUtil.getColumnIndexOrThrow(_cursor, "offerId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfTemplateUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "templateUrl");
          final List<Offer> _result = new ArrayList<Offer>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Offer _item;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpOfferId;
            _tmpOfferId = _cursor.getString(_cursorIndexOfOfferId);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            final String _tmpTemplateUrl;
            _tmpTemplateUrl = _cursor.getString(_cursorIndexOfTemplateUrl);
            _item = new Offer(_tmpStatus,_tmpUserId,_tmpOfferId,_tmpCreatedAt,_tmpUpdatedAt,_tmpTemplateUrl);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
