package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.TimeModel;
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

public class TimeModelRealmProxy extends TimeModel
    implements RealmObjectProxy {

    private static long INDEX_STARTTIME;
    private static long INDEX_ENDTIME;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("startTime");
        fieldNames.add("endTime");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    @Override
    public String getStartTime() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_STARTTIME);
    }

    @Override
    public void setStartTime(String value) {
        realm.checkIfValid();
        row.setString(INDEX_STARTTIME, (String) value);
    }

    @Override
    public String getEndTime() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_ENDTIME);
    }

    @Override
    public void setEndTime(String value) {
        realm.checkIfValid();
        row.setString(INDEX_ENDTIME, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_TimeModel")) {
            Table table = transaction.getTable("class_TimeModel");
            table.addColumn(ColumnType.STRING, "startTime");
            table.addColumn(ColumnType.STRING, "endTime");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_TimeModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_TimeModel")) {
            Table table = transaction.getTable("class_TimeModel");
            if (table.getColumnCount() != 2) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 2 but was " + table.getColumnCount());
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for (long i = 0; i < 2; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type TimeModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_STARTTIME = table.getColumnIndex("startTime");
            INDEX_ENDTIME = table.getColumnIndex("endTime");

            if (!columnTypes.containsKey("startTime")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'startTime'");
            }
            if (columnTypes.get("startTime") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'startTime'");
            }
            if (!columnTypes.containsKey("endTime")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'endTime'");
            }
            if (columnTypes.get("endTime") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'endTime'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The TimeModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_TimeModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static TimeModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        TimeModel obj = realm.createObject(TimeModel.class);
        if (json.has("startTime")) {
            if (json.isNull("startTime")) {
                obj.setStartTime("");
            } else {
                obj.setStartTime((String) json.getString("startTime"));
            }
        }
        if (json.has("endTime")) {
            if (json.isNull("endTime")) {
                obj.setEndTime("");
            } else {
                obj.setEndTime((String) json.getString("endTime"));
            }
        }
        return obj;
    }

    public static TimeModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        TimeModel obj = realm.createObject(TimeModel.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("startTime")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setStartTime("");
                    reader.skipValue();
                } else {
                    obj.setStartTime((String) reader.nextString());
                }
            } else if (name.equals("endTime")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setEndTime("");
                    reader.skipValue();
                } else {
                    obj.setEndTime((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static TimeModel copyOrUpdate(Realm realm, TimeModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static TimeModel copy(Realm realm, TimeModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        TimeModel realmObject = realm.createObject(TimeModel.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setStartTime(newObject.getStartTime() != null ? newObject.getStartTime() : "");
        realmObject.setEndTime(newObject.getEndTime() != null ? newObject.getEndTime() : "");
        return realmObject;
    }

    static TimeModel update(Realm realm, TimeModel realmObject, TimeModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setStartTime(newObject.getStartTime() != null ? newObject.getStartTime() : "");
        realmObject.setEndTime(newObject.getEndTime() != null ? newObject.getEndTime() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("TimeModel = [");
        stringBuilder.append("{startTime:");
        stringBuilder.append(getStartTime());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{endTime:");
        stringBuilder.append(getEndTime());
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
        TimeModelRealmProxy aTimeModel = (TimeModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aTimeModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aTimeModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aTimeModel.row.getIndex()) return false;

        return true;
    }

}
