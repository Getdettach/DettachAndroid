package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.RepeatDaysModel;
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

public class RepeatDaysModelRealmProxy extends RepeatDaysModel
    implements RealmObjectProxy {

    private static long INDEX_DAYS;
    private static long INDEX_ID;
    private static long INDEX_IDFLAG;
    private static long INDEX_AVAILABLE;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("days");
        fieldNames.add("id");
        fieldNames.add("idFlag");
        fieldNames.add("available");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    @Override
    public String getDays() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_DAYS);
    }

    @Override
    public void setDays(String value) {
        realm.checkIfValid();
        row.setString(INDEX_DAYS, (String) value);
    }

    @Override
    public String getId() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_ID);
    }

    @Override
    public void setId(String value) {
        realm.checkIfValid();
        row.setString(INDEX_ID, (String) value);
    }

    @Override
    public String getIdFlag() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_IDFLAG);
    }

    @Override
    public void setIdFlag(String value) {
        realm.checkIfValid();
        row.setString(INDEX_IDFLAG, (String) value);
    }

    @Override
    public String getAvailable() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_AVAILABLE);
    }

    @Override
    public void setAvailable(String value) {
        realm.checkIfValid();
        row.setString(INDEX_AVAILABLE, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_RepeatDaysModel")) {
            Table table = transaction.getTable("class_RepeatDaysModel");
            table.addColumn(ColumnType.STRING, "days");
            table.addColumn(ColumnType.STRING, "id");
            table.addColumn(ColumnType.STRING, "idFlag");
            table.addColumn(ColumnType.STRING, "available");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_RepeatDaysModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_RepeatDaysModel")) {
            Table table = transaction.getTable("class_RepeatDaysModel");
            if (table.getColumnCount() != 4) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 4 but was " + table.getColumnCount());
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for (long i = 0; i < 4; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type RepeatDaysModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_DAYS = table.getColumnIndex("days");
            INDEX_ID = table.getColumnIndex("id");
            INDEX_IDFLAG = table.getColumnIndex("idFlag");
            INDEX_AVAILABLE = table.getColumnIndex("available");

            if (!columnTypes.containsKey("days")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'days'");
            }
            if (columnTypes.get("days") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'days'");
            }
            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'id'");
            }
            if (columnTypes.get("id") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'id'");
            }
            if (!columnTypes.containsKey("idFlag")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'idFlag'");
            }
            if (columnTypes.get("idFlag") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'idFlag'");
            }
            if (!columnTypes.containsKey("available")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'available'");
            }
            if (columnTypes.get("available") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'available'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The RepeatDaysModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_RepeatDaysModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static RepeatDaysModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        RepeatDaysModel obj = realm.createObject(RepeatDaysModel.class);
        if (json.has("days")) {
            if (json.isNull("days")) {
                obj.setDays("");
            } else {
                obj.setDays((String) json.getString("days"));
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                obj.setId("");
            } else {
                obj.setId((String) json.getString("id"));
            }
        }
        if (json.has("idFlag")) {
            if (json.isNull("idFlag")) {
                obj.setIdFlag("");
            } else {
                obj.setIdFlag((String) json.getString("idFlag"));
            }
        }
        if (json.has("available")) {
            if (json.isNull("available")) {
                obj.setAvailable("");
            } else {
                obj.setAvailable((String) json.getString("available"));
            }
        }
        return obj;
    }

    public static RepeatDaysModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        RepeatDaysModel obj = realm.createObject(RepeatDaysModel.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("days")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setDays("");
                    reader.skipValue();
                } else {
                    obj.setDays((String) reader.nextString());
                }
            } else if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setId("");
                    reader.skipValue();
                } else {
                    obj.setId((String) reader.nextString());
                }
            } else if (name.equals("idFlag")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setIdFlag("");
                    reader.skipValue();
                } else {
                    obj.setIdFlag((String) reader.nextString());
                }
            } else if (name.equals("available")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setAvailable("");
                    reader.skipValue();
                } else {
                    obj.setAvailable((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static RepeatDaysModel copyOrUpdate(Realm realm, RepeatDaysModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static RepeatDaysModel copy(Realm realm, RepeatDaysModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        RepeatDaysModel realmObject = realm.createObject(RepeatDaysModel.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setDays(newObject.getDays() != null ? newObject.getDays() : "");
        realmObject.setId(newObject.getId() != null ? newObject.getId() : "");
        realmObject.setIdFlag(newObject.getIdFlag() != null ? newObject.getIdFlag() : "");
        realmObject.setAvailable(newObject.getAvailable() != null ? newObject.getAvailable() : "");
        return realmObject;
    }

    static RepeatDaysModel update(Realm realm, RepeatDaysModel realmObject, RepeatDaysModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setDays(newObject.getDays() != null ? newObject.getDays() : "");
        realmObject.setId(newObject.getId() != null ? newObject.getId() : "");
        realmObject.setIdFlag(newObject.getIdFlag() != null ? newObject.getIdFlag() : "");
        realmObject.setAvailable(newObject.getAvailable() != null ? newObject.getAvailable() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("RepeatDaysModel = [");
        stringBuilder.append("{days:");
        stringBuilder.append(getDays());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{id:");
        stringBuilder.append(getId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{idFlag:");
        stringBuilder.append(getIdFlag());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{available:");
        stringBuilder.append(getAvailable());
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
        RepeatDaysModelRealmProxy aRepeatDaysModel = (RepeatDaysModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aRepeatDaysModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aRepeatDaysModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aRepeatDaysModel.row.getIndex()) return false;

        return true;
    }

}
