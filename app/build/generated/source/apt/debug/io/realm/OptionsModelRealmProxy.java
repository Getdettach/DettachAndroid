package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.OptionsModel;
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

public class OptionsModelRealmProxy extends OptionsModel
    implements RealmObjectProxy {

    private static long INDEX_MESSAGE;
    private static long INDEX_DESCRIPTION;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("message");
        fieldNames.add("description");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
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
    public String getDescription() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_DESCRIPTION);
    }

    @Override
    public void setDescription(String value) {
        realm.checkIfValid();
        row.setString(INDEX_DESCRIPTION, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_OptionsModel")) {
            Table table = transaction.getTable("class_OptionsModel");
            table.addColumn(ColumnType.STRING, "message");
            table.addColumn(ColumnType.STRING, "description");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_OptionsModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_OptionsModel")) {
            Table table = transaction.getTable("class_OptionsModel");
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
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type OptionsModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_MESSAGE = table.getColumnIndex("message");
            INDEX_DESCRIPTION = table.getColumnIndex("description");

            if (!columnTypes.containsKey("message")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'message'");
            }
            if (columnTypes.get("message") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'message'");
            }
            if (!columnTypes.containsKey("description")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'description'");
            }
            if (columnTypes.get("description") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'description'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The OptionsModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_OptionsModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static OptionsModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        OptionsModel obj = realm.createObject(OptionsModel.class);
        if (json.has("message")) {
            if (json.isNull("message")) {
                obj.setMessage("");
            } else {
                obj.setMessage((String) json.getString("message"));
            }
        }
        if (json.has("description")) {
            if (json.isNull("description")) {
                obj.setDescription("");
            } else {
                obj.setDescription((String) json.getString("description"));
            }
        }
        return obj;
    }

    public static OptionsModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        OptionsModel obj = realm.createObject(OptionsModel.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("message")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setMessage("");
                    reader.skipValue();
                } else {
                    obj.setMessage((String) reader.nextString());
                }
            } else if (name.equals("description")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setDescription("");
                    reader.skipValue();
                } else {
                    obj.setDescription((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static OptionsModel copyOrUpdate(Realm realm, OptionsModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static OptionsModel copy(Realm realm, OptionsModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        OptionsModel realmObject = realm.createObject(OptionsModel.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setMessage(newObject.getMessage() != null ? newObject.getMessage() : "");
        realmObject.setDescription(newObject.getDescription() != null ? newObject.getDescription() : "");
        return realmObject;
    }

    static OptionsModel update(Realm realm, OptionsModel realmObject, OptionsModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setMessage(newObject.getMessage() != null ? newObject.getMessage() : "");
        realmObject.setDescription(newObject.getDescription() != null ? newObject.getDescription() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("OptionsModel = [");
        stringBuilder.append("{message:");
        stringBuilder.append(getMessage());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{description:");
        stringBuilder.append(getDescription());
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
        OptionsModelRealmProxy aOptionsModel = (OptionsModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aOptionsModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aOptionsModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aOptionsModel.row.getIndex()) return false;

        return true;
    }

}
