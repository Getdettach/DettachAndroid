package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.CallerModel;
import io.realm.RealmObject;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnType;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CallerModelRealmProxy extends CallerModel
    implements RealmObjectProxy {

    private static long INDEX_ID;
    private static long INDEX_NUMBER;
    private static long INDEX_NAME;
    private static long INDEX_HASAPP;
    private static long INDEX_HASPERMISSION;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("number");
        fieldNames.add("name");
        fieldNames.add("hasApp");
        fieldNames.add("haspermission");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    @Override
    public int getId() {
        realm.checkIfValid();
        return (int) row.getLong(INDEX_ID);
    }

    @Override
    public void setId(int value) {
        realm.checkIfValid();
        row.setLong(INDEX_ID, (long) value);
    }

    @Override
    public String getNumber() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_NUMBER);
    }

    @Override
    public void setNumber(String value) {
        realm.checkIfValid();
        row.setString(INDEX_NUMBER, (String) value);
    }

    @Override
    public String getName() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_NAME);
    }

    @Override
    public void setName(String value) {
        realm.checkIfValid();
        row.setString(INDEX_NAME, (String) value);
    }

    @Override
    public int getHasApp() {
        realm.checkIfValid();
        return (int) row.getLong(INDEX_HASAPP);
    }

    @Override
    public void setHasApp(int value) {
        realm.checkIfValid();
        row.setLong(INDEX_HASAPP, (long) value);
    }

    @Override
    public String getHaspermission() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_HASPERMISSION);
    }

    @Override
    public void setHaspermission(String value) {
        realm.checkIfValid();
        row.setString(INDEX_HASPERMISSION, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_CallerModel")) {
            Table table = transaction.getTable("class_CallerModel");
            table.addColumn(ColumnType.INTEGER, "id");
            table.addColumn(ColumnType.STRING, "number");
            table.addColumn(ColumnType.STRING, "name");
            table.addColumn(ColumnType.INTEGER, "hasApp");
            table.addColumn(ColumnType.STRING, "haspermission");
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_CallerModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_CallerModel")) {
            Table table = transaction.getTable("class_CallerModel");
            if (table.getColumnCount() != 5) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 5 but was " + table.getColumnCount());
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for (long i = 0; i < 5; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type CallerModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_ID = table.getColumnIndex("id");
            INDEX_NUMBER = table.getColumnIndex("number");
            INDEX_NAME = table.getColumnIndex("name");
            INDEX_HASAPP = table.getColumnIndex("hasApp");
            INDEX_HASPERMISSION = table.getColumnIndex("haspermission");

            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'id'");
            }
            if (columnTypes.get("id") != ColumnType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'id'");
            }
            if (table.getPrimaryKey() != table.getColumnIndex("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Primary key not defined for field 'id'");
            }
            if (!table.hasSearchIndex(table.getColumnIndex("id"))) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Index not defined for field 'id'");
            }
            if (!columnTypes.containsKey("number")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'number'");
            }
            if (columnTypes.get("number") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'number'");
            }
            if (!columnTypes.containsKey("name")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'name'");
            }
            if (columnTypes.get("name") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'name'");
            }
            if (!columnTypes.containsKey("hasApp")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'hasApp'");
            }
            if (columnTypes.get("hasApp") != ColumnType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'hasApp'");
            }
            if (!columnTypes.containsKey("haspermission")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'haspermission'");
            }
            if (columnTypes.get("haspermission") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'haspermission'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The CallerModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_CallerModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static CallerModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        CallerModel obj = null;
        if (update) {
            Table table = realm.getTable(CallerModel.class);
            long pkColumnIndex = table.getPrimaryKey();
            if (!json.isNull("id")) {
                long rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
                if (rowIndex != TableOrView.NO_MATCH) {
                    obj = new CallerModelRealmProxy();
                    obj.realm = realm;
                    obj.row = table.getUncheckedRow(rowIndex);
                }
            }
        }
        if (obj == null) {
            obj = realm.createObject(CallerModel.class);
        }
        if (!json.isNull("id")) {
            obj.setId((int) json.getInt("id"));
        }
        if (json.has("number")) {
            if (json.isNull("number")) {
                obj.setNumber("");
            } else {
                obj.setNumber((String) json.getString("number"));
            }
        }
        if (json.has("name")) {
            if (json.isNull("name")) {
                obj.setName("");
            } else {
                obj.setName((String) json.getString("name"));
            }
        }
        if (!json.isNull("hasApp")) {
            obj.setHasApp((int) json.getInt("hasApp"));
        }
        if (json.has("haspermission")) {
            if (json.isNull("haspermission")) {
                obj.setHaspermission("");
            } else {
                obj.setHaspermission((String) json.getString("haspermission"));
            }
        }
        return obj;
    }

    public static CallerModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        CallerModel obj = realm.createObject(CallerModel.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                obj.setId((int) reader.nextInt());
            } else if (name.equals("number")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setNumber("");
                    reader.skipValue();
                } else {
                    obj.setNumber((String) reader.nextString());
                }
            } else if (name.equals("name")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setName("");
                    reader.skipValue();
                } else {
                    obj.setName((String) reader.nextString());
                }
            } else if (name.equals("hasApp")  && reader.peek() != JsonToken.NULL) {
                obj.setHasApp((int) reader.nextInt());
            } else if (name.equals("haspermission")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setHaspermission("");
                    reader.skipValue();
                } else {
                    obj.setHaspermission((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static CallerModel copyOrUpdate(Realm realm, CallerModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        CallerModel realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(CallerModel.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = table.findFirstLong(pkColumnIndex, object.getId());
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new CallerModelRealmProxy();
                realmObject.realm = realm;
                realmObject.row = table.getUncheckedRow(rowIndex);
                cache.put(object, (RealmObjectProxy) realmObject);
            } else {
                canUpdate = false;
            }
        }

        if (canUpdate) {
            return update(realm, realmObject, object, cache);
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static CallerModel copy(Realm realm, CallerModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        CallerModel realmObject = realm.createObject(CallerModel.class, newObject.getId());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setId(newObject.getId());
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setName(newObject.getName() != null ? newObject.getName() : "");
        realmObject.setHasApp(newObject.getHasApp());
        realmObject.setHaspermission(newObject.getHaspermission() != null ? newObject.getHaspermission() : "");
        return realmObject;
    }

    static CallerModel update(Realm realm, CallerModel realmObject, CallerModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setName(newObject.getName() != null ? newObject.getName() : "");
        realmObject.setHasApp(newObject.getHasApp());
        realmObject.setHaspermission(newObject.getHaspermission() != null ? newObject.getHaspermission() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("CallerModel = [");
        stringBuilder.append("{id:");
        stringBuilder.append(getId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{number:");
        stringBuilder.append(getNumber());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{name:");
        stringBuilder.append(getName());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{hasApp:");
        stringBuilder.append(getHasApp());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{haspermission:");
        stringBuilder.append(getHaspermission());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        String realmName = realm.getPath();
        String tableName = row.getTable().getName();
        long rowIndex = row.getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallerModelRealmProxy aCallerModel = (CallerModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aCallerModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aCallerModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aCallerModel.row.getIndex()) return false;

        return true;
    }

}
