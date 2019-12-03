package com.mingrisoft.setblacklist.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.mingrisoft.setblacklist.entity.PersonEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PERSON_ENTITY".
*/
public class PersonEntityDao extends AbstractDao<PersonEntity, Long> {

    public static final String TABLENAME = "PERSON_ENTITY";

    /**
     * Properties of entity PersonEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Tag = new Property(1, Long.class, "tag", false, "TAG");
        public final static Property UserName = new Property(2, String.class, "UserName", false, "USER_NAME");
        public final static Property Number = new Property(3, String.class, "Number", false, "NUMBER");
    };


    public PersonEntityDao(DaoConfig config) {
        super(config);
    }
    
    public PersonEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PERSON_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TAG\" INTEGER," + // 1: tag
                "\"USER_NAME\" TEXT," + // 2: UserName
                "\"NUMBER\" TEXT);"); // 3: Number
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PERSON_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PersonEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long tag = entity.getTag();
        if (tag != null) {
            stmt.bindLong(2, tag);
        }
 
        String UserName = entity.getUserName();
        if (UserName != null) {
            stmt.bindString(3, UserName);
        }
 
        String Number = entity.getNumber();
        if (Number != null) {
            stmt.bindString(4, Number);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PersonEntity readEntity(Cursor cursor, int offset) {
        PersonEntity entity = new PersonEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // tag
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // UserName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // Number
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PersonEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTag(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNumber(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PersonEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PersonEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
