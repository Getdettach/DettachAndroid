package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.ExtendTimeModel;
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

public class ExtendTimeModelRealmProxy extends ExtendTimeModel
    implements RealmObjectProxy {

    private static long INDEX_TIMEID;
    private static long INDEX_STARTTIME;
    private static long INDEX_ENDTIME;
    private static long INDEX_STARTID;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("timeId");
        fieldNames.add("startTime");
        fieldNames.add("endTime");
        fieldNames.add("startId");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    @Override
    public String getTimeId() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_TIMEID);
    }

    @Override
    public void setTimeId(String value) {
        realm.checkIfValid();
        row.setString(INDEX_TIMEID, (String) value);
    }

    @Override
    public Date getStartTime() {
        realm.checkIfValid();
        return (java.util.Date) row.getDate(INDEX_STARTTIME);
    }

    @Override
    public void setStartTime(Date value) {
        realm.checkIfValid();
        row.setDate(INDEX_STARTTIME, (Date) value);
    }

    @Override
    public Date getEndTime() {
        realm.checkIfValid();
        return (java.util.Date) row.getDate(INDEX_ENDTIME);
    }

    @Override
    public void setEndTime(Date value) {
        realm.checkIfValid();
        row.setDate(INDEX_ENDTIME, (Date) value);
    }

    @Override
    public String getStartId() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_STARTID);
    }

    @Override
    public void setStartId(String value) {
        realm.checkIfValid();
        row.setString(INDEX_STARTID, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_ExtendTimeModel")) {
            Table table = transaction.getTable("class_ExtendTimeModel");
            table.addColumn(ColumnType.STRING, "timeId");
            table.addColumn(ColumnType.DATE, "startTime");
            table.addColumn(ColumnType.DATE, "endTime");
            table.addColumn(ColumnType.STRING, "startId");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_ExtendTimeModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_ExtendTimeModel")) {
            Table table = transaction.getTable("class_ExtendTimeModel");
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
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type ExtendTimeModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_TIMEID = table.getColumnIndex("timeId");
            INDEX_STARTTIME = table.getColumnIndex("startTime");
            INDEX_ENDTIME = table.getColumnIndex("endTime");
            INDEX_STARTID = table.getColumnIndex("startId");

            if (!columnTypes.containsKey("timeId")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'timeId'");
            }
            if (columnTypes.get("timeId") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'timeId'");
            }
            if (!columnTypes.containsKey("startTime")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'startTime'");
            }
            if (columnTypes.get("startTime") != ColumnType.DATE) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'Date' for field 'startTime'");
            }
            if (!columnTypes.containsKey("endTime")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'endTime'");
            }
            if (columnTypes.get("endTime") != ColumnType.DATE) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'Date' for field 'endTime'");
            }
            if (!columnTypes.containsKey("startId")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'startId'");
            }
            if (columnTypes.get("startId") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'startId'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The ExtendTimeModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_ExtendTimeModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static ExtendTimeModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        ExtendTimeModel obj = realm.createObject(ExtendTimeModel.class);
        if (json.has("timeId")) {
            if (json.isNull("timeId")) {
                obj.setTimeId("");
            } else {
                obj.setTimeId((String) json.getString("timeId"));
            }
        }
        if (!json.isNull("startTime")) {
            Object timestamp = json.get("startTime");
            if (timestamp instanceof String) {
                obj.setStartTime(JsonUtils.stringToDate((String) timestamp));
            } else {
                obj.setStartTime(new Date(json.getLong("startTime")));
            }
        }
        if (!json.isNull("endTime")) {
            Object timestamp = json.get("endTime");
            if (timestamp instanceof String) {
                obj.setEndTime(JsonUtils.stringToDate((String) timestamp));
            } else {
                obj.setEndTime(new Date(json.getLong("endTime")));
            }
        }
        if (json.has("startId")) {
            if (json.isNull("startId")) {
                obj.setStartId("");
            } else {
                obj.setStartId((String) json.getString("startId"));
            }
        }
        return obj;
    }

    public static ExtendTimeModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        ExtendTimeModel obj = realm.createObject(ExtendTimeModel.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("timeId")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setTimeId("");
                    reader.skipValue();
                } else {
                    obj.setTimeId((String) reader.nextString());
                }
            } else if (name.equals("startTime")  && reader.peek() != JsonToken.NULL) {
                if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        obj.setStartTime(new Date(timestamp));
                    }
                } else {
                    obj.setStartTime(JsonUtils.stringToDate(reader.nextString()));
                }
            } else if (name.equals("endTime")  && reader.peek() != JsonToken.NULL) {
                if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        obj.setEndTime(new Date(timestamp));
                    }
                } else {
                    obj.setEndTime(JsonUtils.stringToDate(reader.nextString()));
                }
            } else if (name.equals("startId")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setStartId("");
                    reader.skipValue();
                } else {
                    obj.setStartId((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static ExtendTimeModel copyOrUpdate(Realm realm, ExtendTimeModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static ExtendTimeModel copy(Realm realm, ExtendTimeModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        ExtendTimeModel realmObject = realm.createObject(ExtendTimeModel.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setTimeId(newObject.getTimeId() != null ? newObject.getTimeId() : "");
        realmObject.setStartTime(newObject.getStartTime() != null ? newObject.getStartTime() : new Date(0));
        realmObject.setEndTime(newObject.getEndTime() != null ? newObject.getEndTime() : new Date(0));
        realmObject.setStartId(newObject.getStartId() != null ? newObject.getStartId() : "");
        return realmObject;
    }

    static ExtendTimeModel update(Realm realm, ExtendTimeModel realmObject, ExtendTimeModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setTimeId(newObject.getTimeId() != null ? newObject.getTimeId() : "");
        realmObject.setStartTime(newObject.getStartTime() != null ? newObject.getStartTime() : new Date(0));
        realmObject.setEndTime(newObject.getEndTime() != null ? newObject.getEndTime() : new Date(0));
        realmObject.setStartId(newObject.getStartId() != null ? newObject.getStartId() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("ExtendTimeModel = [");
        stringBuilder.append("{timeId:");
        stringBuilder.append(getTimeId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{startTime:");
        stringBuilder.append(getStartTime());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{endTime:");
        stringBuilder.append(getEndTime());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{startId:");
        stringBuilder.append(getStartId());
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
        ExtendTimeModelRealmProxy aExtendTimeModel = (ExtendTimeModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aExtendTimeModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aExtendTimeModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aExtendTimeModel.row.getIndex()) return false;

        return true;
    }

}
