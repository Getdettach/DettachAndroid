package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.ContactModel;
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

public class ContactModelRealmProxy extends ContactModel
    implements RealmObjectProxy {

    private static long INDEX_NAME;
    private static long INDEX_NUMBER;
    private static long INDEX_PICTURE;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("name");
        fieldNames.add("number");
        fieldNames.add("picture");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
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
    public String getPicture() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_PICTURE);
    }

    @Override
    public void setPicture(String value) {
        realm.checkIfValid();
        row.setString(INDEX_PICTURE, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_ContactModel")) {
            Table table = transaction.getTable("class_ContactModel");
            table.addColumn(ColumnType.STRING, "name");
            table.addColumn(ColumnType.STRING, "number");
            table.addColumn(ColumnType.STRING, "picture");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_ContactModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_ContactModel")) {
            Table table = transaction.getTable("class_ContactModel");
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
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type ContactModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_NAME = table.getColumnIndex("name");
            INDEX_NUMBER = table.getColumnIndex("number");
            INDEX_PICTURE = table.getColumnIndex("picture");

            if (!columnTypes.containsKey("name")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'name'");
            }
            if (columnTypes.get("name") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'name'");
            }
            if (!columnTypes.containsKey("number")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'number'");
            }
            if (columnTypes.get("number") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'number'");
            }
            if (!columnTypes.containsKey("picture")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'picture'");
            }
            if (columnTypes.get("picture") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'picture'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The ContactModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_ContactModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static ContactModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        ContactModel obj = realm.createObject(ContactModel.class);
        if (json.has("name")) {
            if (json.isNull("name")) {
                obj.setName("");
            } else {
                obj.setName((String) json.getString("name"));
            }
        }
        if (json.has("number")) {
            if (json.isNull("number")) {
                obj.setNumber("");
            } else {
                obj.setNumber((String) json.getString("number"));
            }
        }
        if (json.has("picture")) {
            if (json.isNull("picture")) {
                obj.setPicture("");
            } else {
                obj.setPicture((String) json.getString("picture"));
            }
        }
        return obj;
    }

    public static ContactModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        ContactModel obj = realm.createObject(ContactModel.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setName("");
                    reader.skipValue();
                } else {
                    obj.setName((String) reader.nextString());
                }
            } else if (name.equals("number")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setNumber("");
                    reader.skipValue();
                } else {
                    obj.setNumber((String) reader.nextString());
                }
            } else if (name.equals("picture")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setPicture("");
                    reader.skipValue();
                } else {
                    obj.setPicture((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static ContactModel copyOrUpdate(Realm realm, ContactModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static ContactModel copy(Realm realm, ContactModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        ContactModel realmObject = realm.createObject(ContactModel.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setName(newObject.getName() != null ? newObject.getName() : "");
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setPicture(newObject.getPicture() != null ? newObject.getPicture() : "");
        return realmObject;
    }

    static ContactModel update(Realm realm, ContactModel realmObject, ContactModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setName(newObject.getName() != null ? newObject.getName() : "");
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setPicture(newObject.getPicture() != null ? newObject.getPicture() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("ContactModel = [");
        stringBuilder.append("{name:");
        stringBuilder.append(getName());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{number:");
        stringBuilder.append(getNumber());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{picture:");
        stringBuilder.append(getPicture());
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
        ContactModelRealmProxy aContactModel = (ContactModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aContactModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aContactModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aContactModel.row.getIndex()) return false;

        return true;
    }

}
