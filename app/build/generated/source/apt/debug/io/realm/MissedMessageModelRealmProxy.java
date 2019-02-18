package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.MissedMessageModel;
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

public class MissedMessageModelRealmProxy extends MissedMessageModel
    implements RealmObjectProxy {

    private static long INDEX_NUMBER;
    private static long INDEX_TYPE;
    private static long INDEX_TIME;
    private static long INDEX_DATE;
    private static long INDEX_MESSAGE;
    private static long INDEX_CALLERNAME;
    private static long INDEX_CALLDATEFORMAT;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("number");
        fieldNames.add("type");
        fieldNames.add("time");
        fieldNames.add("date");
        fieldNames.add("message");
        fieldNames.add("callerName");
        fieldNames.add("callDateFormat");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
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
    public String getType() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_TYPE);
    }

    @Override
    public void setType(String value) {
        realm.checkIfValid();
        row.setString(INDEX_TYPE, (String) value);
    }

    @Override
    public String getTime() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_TIME);
    }

    @Override
    public void setTime(String value) {
        realm.checkIfValid();
        row.setString(INDEX_TIME, (String) value);
    }

    @Override
    public String getDate() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_DATE);
    }

    @Override
    public void setDate(String value) {
        realm.checkIfValid();
        row.setString(INDEX_DATE, (String) value);
    }

    @Override
    public String getMessage() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_MESSAGE);
    }

    @Override
    public void setMessage(String value) {
        realm.checkIfValid();
        row.setString(INDEX_MESSAGE, (String) value);
    }

    @Override
    public String getCallerName() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_CALLERNAME);
    }

    @Override
    public void setCallerName(String value) {
        realm.checkIfValid();
        row.setString(INDEX_CALLERNAME, (String) value);
    }

    @Override
    public Date getCallDateFormat() {
        realm.checkIfValid();
        return (java.util.Date) row.getDate(INDEX_CALLDATEFORMAT);
    }

    @Override
    public void setCallDateFormat(Date value) {
        realm.checkIfValid();
        row.setDate(INDEX_CALLDATEFORMAT, (Date) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_MissedMessageModel")) {
            Table table = transaction.getTable("class_MissedMessageModel");
            table.addColumn(ColumnType.STRING, "number");
            table.addColumn(ColumnType.STRING, "type");
            table.addColumn(ColumnType.STRING, "time");
            table.addColumn(ColumnType.STRING, "date");
            table.addColumn(ColumnType.STRING, "message");
            table.addColumn(ColumnType.STRING, "callerName");
            table.addColumn(ColumnType.DATE, "callDateFormat");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_MissedMessageModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_MissedMessageModel")) {
            Table table = transaction.getTable("class_MissedMessageModel");
            if (table.getColumnCount() != 7) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 7 but was " + table.getColumnCount());
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for (long i = 0; i < 7; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type MissedMessageModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_NUMBER = table.getColumnIndex("number");
            INDEX_TYPE = table.getColumnIndex("type");
            INDEX_TIME = table.getColumnIndex("time");
            INDEX_DATE = table.getColumnIndex("date");
            INDEX_MESSAGE = table.getColumnIndex("message");
            INDEX_CALLERNAME = table.getColumnIndex("callerName");
            INDEX_CALLDATEFORMAT = table.getColumnIndex("callDateFormat");

            if (!columnTypes.containsKey("number")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'number'");
            }
            if (columnTypes.get("number") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'number'");
            }
            if (!columnTypes.containsKey("type")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'type'");
            }
            if (columnTypes.get("type") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'type'");
            }
            if (!columnTypes.containsKey("time")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'time'");
            }
            if (columnTypes.get("time") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'time'");
            }
            if (!columnTypes.containsKey("date")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'date'");
            }
            if (columnTypes.get("date") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'date'");
            }
            if (!columnTypes.containsKey("message")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'message'");
            }
            if (columnTypes.get("message") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'message'");
            }
            if (!columnTypes.containsKey("callerName")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'callerName'");
            }
            if (columnTypes.get("callerName") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'callerName'");
            }
            if (!columnTypes.containsKey("callDateFormat")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'callDateFormat'");
            }
            if (columnTypes.get("callDateFormat") != ColumnType.DATE) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'Date' for field 'callDateFormat'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The MissedMessageModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_MissedMessageModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static MissedMessageModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        MissedMessageModel obj = realm.createObject(MissedMessageModel.class);
        if (json.has("number")) {
            if (json.isNull("number")) {
                obj.setNumber("");
            } else {
                obj.setNumber((String) json.getString("number"));
            }
        }
        if (json.has("type")) {
            if (json.isNull("type")) {
                obj.setType("");
            } else {
                obj.setType((String) json.getString("type"));
            }
        }
        if (json.has("time")) {
            if (json.isNull("time")) {
                obj.setTime("");
            } else {
                obj.setTime((String) json.getString("time"));
            }
        }
        if (json.has("date")) {
            if (json.isNull("date")) {
                obj.setDate("");
            } else {
                obj.setDate((String) json.getString("date"));
            }
        }
        if (json.has("message")) {
            if (json.isNull("message")) {
                obj.setMessage("");
            } else {
                obj.setMessage((String) json.getString("message"));
            }
        }
        if (json.has("callerName")) {
            if (json.isNull("callerName")) {
                obj.setCallerName("");
            } else {
                obj.setCallerName((String) json.getString("callerName"));
            }
        }
        if (!json.isNull("callDateFormat")) {
            Object timestamp = json.get("callDateFormat");
            if (timestamp instanceof String) {
                obj.setCallDateFormat(JsonUtils.stringToDate((String) timestamp));
            } else {
                obj.setCallDateFormat(new Date(json.getLong("callDateFormat")));
            }
        }
        return obj;
    }

    public static MissedMessageModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        MissedMessageModel obj = realm.createObject(MissedMessageModel.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("number")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setNumber("");
                    reader.skipValue();
                } else {
                    obj.setNumber((String) reader.nextString());
                }
            } else if (name.equals("type")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setType("");
                    reader.skipValue();
                } else {
                    obj.setType((String) reader.nextString());
                }
            } else if (name.equals("time")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setTime("");
                    reader.skipValue();
                } else {
                    obj.setTime((String) reader.nextString());
                }
            } else if (name.equals("date")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setDate("");
                    reader.skipValue();
                } else {
                    obj.setDate((String) reader.nextString());
                }
            } else if (name.equals("message")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setMessage("");
                    reader.skipValue();
                } else {
                    obj.setMessage((String) reader.nextString());
                }
            } else if (name.equals("callerName")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setCallerName("");
                    reader.skipValue();
                } else {
                    obj.setCallerName((String) reader.nextString());
                }
            } else if (name.equals("callDateFormat")  && reader.peek() != JsonToken.NULL) {
                if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        obj.setCallDateFormat(new Date(timestamp));
                    }
                } else {
                    obj.setCallDateFormat(JsonUtils.stringToDate(reader.nextString()));
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static MissedMessageModel copyOrUpdate(Realm realm, MissedMessageModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static MissedMessageModel copy(Realm realm, MissedMessageModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        MissedMessageModel realmObject = realm.createObject(MissedMessageModel.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setType(newObject.getType() != null ? newObject.getType() : "");
        realmObject.setTime(newObject.getTime() != null ? newObject.getTime() : "");
        realmObject.setDate(newObject.getDate() != null ? newObject.getDate() : "");
        realmObject.setMessage(newObject.getMessage() != null ? newObject.getMessage() : "");
        realmObject.setCallerName(newObject.getCallerName() != null ? newObject.getCallerName() : "");
        realmObject.setCallDateFormat(newObject.getCallDateFormat() != null ? newObject.getCallDateFormat() : new Date(0));
        return realmObject;
    }

    static MissedMessageModel update(Realm realm, MissedMessageModel realmObject, MissedMessageModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setType(newObject.getType() != null ? newObject.getType() : "");
        realmObject.setTime(newObject.getTime() != null ? newObject.getTime() : "");
        realmObject.setDate(newObject.getDate() != null ? newObject.getDate() : "");
        realmObject.setMessage(newObject.getMessage() != null ? newObject.getMessage() : "");
        realmObject.setCallerName(newObject.getCallerName() != null ? newObject.getCallerName() : "");
        realmObject.setCallDateFormat(newObject.getCallDateFormat() != null ? newObject.getCallDateFormat() : new Date(0));
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("MissedMessageModel = [");
        stringBuilder.append("{number:");
        stringBuilder.append(getNumber());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{type:");
        stringBuilder.append(getType());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{time:");
        stringBuilder.append(getTime());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{date:");
        stringBuilder.append(getDate());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{message:");
        stringBuilder.append(getMessage());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{callerName:");
        stringBuilder.append(getCallerName());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{callDateFormat:");
        stringBuilder.append(getCallDateFormat());
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
        MissedMessageModelRealmProxy aMissedMessageModel = (MissedMessageModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aMissedMessageModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aMissedMessageModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aMissedMessageModel.row.getIndex()) return false;

        return true;
    }

}
