package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.MessageModel;
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

public class MessageModelRealmProxy extends MessageModel
    implements RealmObjectProxy {

    private static long INDEX_NUMBER;
    private static long INDEX_MESSAGE;
    private static long INDEX_DATE;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("number");
        fieldNames.add("message");
        fieldNames.add("date");
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
    public String getDate() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_DATE);
    }

    @Override
    public void setDate(String value) {
        realm.checkIfValid();
        row.setString(INDEX_DATE, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_MessageModel")) {
            Table table = transaction.getTable("class_MessageModel");
            table.addColumn(ColumnType.STRING, "number");
            table.addColumn(ColumnType.STRING, "message");
            table.addColumn(ColumnType.STRING, "date");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_MessageModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_MessageModel")) {
            Table table = transaction.getTable("class_MessageModel");
            if (table.getColumnCount() != 3) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 3 but was " + table.getColumnCount());
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for (long i = 0; i < 3; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type MessageModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_NUMBER = table.getColumnIndex("number");
            INDEX_MESSAGE = table.getColumnIndex("message");
            INDEX_DATE = table.getColumnIndex("date");

            if (!columnTypes.containsKey("number")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'number'");
            }
            if (columnTypes.get("number") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'number'");
            }
            if (!columnTypes.containsKey("message")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'message'");
            }
            if (columnTypes.get("message") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'message'");
            }
            if (!columnTypes.containsKey("date")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'date'");
            }
            if (columnTypes.get("date") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'date'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The MessageModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_MessageModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static MessageModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        MessageModel obj = realm.createObject(MessageModel.class);
        if (json.has("number")) {
            if (json.isNull("number")) {
                obj.setNumber("");
            } else {
                obj.setNumber((String) json.getString("number"));
            }
        }
        if (json.has("message")) {
            if (json.isNull("message")) {
                obj.setMessage("");
            } else {
                obj.setMessage((String) json.getString("message"));
            }
        }
        if (json.has("date")) {
            if (json.isNull("date")) {
                obj.setDate("");
            } else {
                obj.setDate((String) json.getString("date"));
            }
        }
        return obj;
    }

    public static MessageModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        MessageModel obj = realm.createObject(MessageModel.class);
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
            } else if (name.equals("message")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setMessage("");
                    reader.skipValue();
                } else {
                    obj.setMessage((String) reader.nextString());
                }
            } else if (name.equals("date")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setDate("");
                    reader.skipValue();
                } else {
                    obj.setDate((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static MessageModel copyOrUpdate(Realm realm, MessageModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static MessageModel copy(Realm realm, MessageModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        MessageModel realmObject = realm.createObject(MessageModel.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setMessage(newObject.getMessage() != null ? newObject.getMessage() : "");
        realmObject.setDate(newObject.getDate() != null ? newObject.getDate() : "");
        return realmObject;
    }

    static MessageModel update(Realm realm, MessageModel realmObject, MessageModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setMessage(newObject.getMessage() != null ? newObject.getMessage() : "");
        realmObject.setDate(newObject.getDate() != null ? newObject.getDate() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("MessageModel = [");
        stringBuilder.append("{number:");
        stringBuilder.append(getNumber());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{message:");
        stringBuilder.append(getMessage());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{date:");
        stringBuilder.append(getDate());
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
        MessageModelRealmProxy aMessageModel = (MessageModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aMessageModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aMessageModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aMessageModel.row.getIndex()) return false;

        return true;
    }

}
