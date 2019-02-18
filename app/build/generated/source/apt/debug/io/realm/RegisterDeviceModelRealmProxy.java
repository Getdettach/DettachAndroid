package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.RegisterDeviceModel;
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

public class RegisterDeviceModelRealmProxy extends RegisterDeviceModel
    implements RealmObjectProxy {

    private static long INDEX_ID;
    private static long INDEX_DEVICEID;
    private static long INDEX_PHONENUMBER;
    private static long INDEX_SIMSERIALNUMBER;
    private static long INDEX_ACCESTOKEN;
    private static long INDEX_REGISTERID;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("deviceId");
        fieldNames.add("phoneNumber");
        fieldNames.add("simSerialNumber");
        fieldNames.add("accestoken");
        fieldNames.add("registerId");
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
    public String getDeviceId() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_DEVICEID);
    }

    @Override
    public void setDeviceId(String value) {
        realm.checkIfValid();
        row.setString(INDEX_DEVICEID, (String) value);
    }

    @Override
    public String getPhoneNumber() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_PHONENUMBER);
    }

    @Override
    public void setPhoneNumber(String value) {
        realm.checkIfValid();
        row.setString(INDEX_PHONENUMBER, (String) value);
    }

    @Override
    public String getSimSerialNumber() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_SIMSERIALNUMBER);
    }

    @Override
    public void setSimSerialNumber(String value) {
        realm.checkIfValid();
        row.setString(INDEX_SIMSERIALNUMBER, (String) value);
    }

    @Override
    public String getAccestoken() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_ACCESTOKEN);
    }

    @Override
    public void setAccestoken(String value) {
        realm.checkIfValid();
        row.setString(INDEX_ACCESTOKEN, (String) value);
    }

    @Override
    public String getRegisterId() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_REGISTERID);
    }

    @Override
    public void setRegisterId(String value) {
        realm.checkIfValid();
        row.setString(INDEX_REGISTERID, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_RegisterDeviceModel")) {
            Table table = transaction.getTable("class_RegisterDeviceModel");
            table.addColumn(ColumnType.INTEGER, "id");
            table.addColumn(ColumnType.STRING, "deviceId");
            table.addColumn(ColumnType.STRING, "phoneNumber");
            table.addColumn(ColumnType.STRING, "simSerialNumber");
            table.addColumn(ColumnType.STRING, "accestoken");
            table.addColumn(ColumnType.STRING, "registerId");
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_RegisterDeviceModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_RegisterDeviceModel")) {
            Table table = transaction.getTable("class_RegisterDeviceModel");
            if (table.getColumnCount() != 6) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 6 but was " + table.getColumnCount());
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for (long i = 0; i < 6; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type RegisterDeviceModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_ID = table.getColumnIndex("id");
            INDEX_DEVICEID = table.getColumnIndex("deviceId");
            INDEX_PHONENUMBER = table.getColumnIndex("phoneNumber");
            INDEX_SIMSERIALNUMBER = table.getColumnIndex("simSerialNumber");
            INDEX_ACCESTOKEN = table.getColumnIndex("accestoken");
            INDEX_REGISTERID = table.getColumnIndex("registerId");

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
            if (!columnTypes.containsKey("deviceId")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'deviceId'");
            }
            if (columnTypes.get("deviceId") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'deviceId'");
            }
            if (!columnTypes.containsKey("phoneNumber")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'phoneNumber'");
            }
            if (columnTypes.get("phoneNumber") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'phoneNumber'");
            }
            if (!columnTypes.containsKey("simSerialNumber")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'simSerialNumber'");
            }
            if (columnTypes.get("simSerialNumber") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'simSerialNumber'");
            }
            if (!columnTypes.containsKey("accestoken")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'accestoken'");
            }
            if (columnTypes.get("accestoken") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'accestoken'");
            }
            if (!columnTypes.containsKey("registerId")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'registerId'");
            }
            if (columnTypes.get("registerId") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'registerId'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The RegisterDeviceModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_RegisterDeviceModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static RegisterDeviceModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        RegisterDeviceModel obj = null;
        if (update) {
            Table table = realm.getTable(RegisterDeviceModel.class);
            long pkColumnIndex = table.getPrimaryKey();
            if (!json.isNull("id")) {
                long rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
                if (rowIndex != TableOrView.NO_MATCH) {
                    obj = new RegisterDeviceModelRealmProxy();
                    obj.realm = realm;
                    obj.row = table.getUncheckedRow(rowIndex);
                }
            }
        }
        if (obj == null) {
            obj = realm.createObject(RegisterDeviceModel.class);
        }
        if (!json.isNull("id")) {
            obj.setId((int) json.getInt("id"));
        }
        if (json.has("deviceId")) {
            if (json.isNull("deviceId")) {
                obj.setDeviceId("");
            } else {
                obj.setDeviceId((String) json.getString("deviceId"));
            }
        }
        if (json.has("phoneNumber")) {
            if (json.isNull("phoneNumber")) {
                obj.setPhoneNumber("");
            } else {
                obj.setPhoneNumber((String) json.getString("phoneNumber"));
            }
        }
        if (json.has("simSerialNumber")) {
            if (json.isNull("simSerialNumber")) {
                obj.setSimSerialNumber("");
            } else {
                obj.setSimSerialNumber((String) json.getString("simSerialNumber"));
            }
        }
        if (json.has("accestoken")) {
            if (json.isNull("accestoken")) {
                obj.setAccestoken("");
            } else {
                obj.setAccestoken((String) json.getString("accestoken"));
            }
        }
        if (json.has("registerId")) {
            if (json.isNull("registerId")) {
                obj.setRegisterId("");
            } else {
                obj.setRegisterId((String) json.getString("registerId"));
            }
        }
        return obj;
    }

    public static RegisterDeviceModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        RegisterDeviceModel obj = realm.createObject(RegisterDeviceModel.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                obj.setId((int) reader.nextInt());
            } else if (name.equals("deviceId")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setDeviceId("");
                    reader.skipValue();
                } else {
                    obj.setDeviceId((String) reader.nextString());
                }
            } else if (name.equals("phoneNumber")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setPhoneNumber("");
                    reader.skipValue();
                } else {
                    obj.setPhoneNumber((String) reader.nextString());
                }
            } else if (name.equals("simSerialNumber")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setSimSerialNumber("");
                    reader.skipValue();
                } else {
                    obj.setSimSerialNumber((String) reader.nextString());
                }
            } else if (name.equals("accestoken")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setAccestoken("");
                    reader.skipValue();
                } else {
                    obj.setAccestoken((String) reader.nextString());
                }
            } else if (name.equals("registerId")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setRegisterId("");
                    reader.skipValue();
                } else {
                    obj.setRegisterId((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static RegisterDeviceModel copyOrUpdate(Realm realm, RegisterDeviceModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        RegisterDeviceModel realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(RegisterDeviceModel.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = table.findFirstLong(pkColumnIndex, object.getId());
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new RegisterDeviceModelRealmProxy();
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

    public static RegisterDeviceModel copy(Realm realm, RegisterDeviceModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        RegisterDeviceModel realmObject = realm.createObject(RegisterDeviceModel.class, newObject.getId());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setId(newObject.getId());
        realmObject.setDeviceId(newObject.getDeviceId() != null ? newObject.getDeviceId() : "");
        realmObject.setPhoneNumber(newObject.getPhoneNumber() != null ? newObject.getPhoneNumber() : "");
        realmObject.setSimSerialNumber(newObject.getSimSerialNumber() != null ? newObject.getSimSerialNumber() : "");
        realmObject.setAccestoken(newObject.getAccestoken() != null ? newObject.getAccestoken() : "");
        realmObject.setRegisterId(newObject.getRegisterId() != null ? newObject.getRegisterId() : "");
        return realmObject;
    }

    static RegisterDeviceModel update(Realm realm, RegisterDeviceModel realmObject, RegisterDeviceModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setDeviceId(newObject.getDeviceId() != null ? newObject.getDeviceId() : "");
        realmObject.setPhoneNumber(newObject.getPhoneNumber() != null ? newObject.getPhoneNumber() : "");
        realmObject.setSimSerialNumber(newObject.getSimSerialNumber() != null ? newObject.getSimSerialNumber() : "");
        realmObject.setAccestoken(newObject.getAccestoken() != null ? newObject.getAccestoken() : "");
        realmObject.setRegisterId(newObject.getRegisterId() != null ? newObject.getRegisterId() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("RegisterDeviceModel = [");
        stringBuilder.append("{id:");
        stringBuilder.append(getId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{deviceId:");
        stringBuilder.append(getDeviceId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{phoneNumber:");
        stringBuilder.append(getPhoneNumber());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{simSerialNumber:");
        stringBuilder.append(getSimSerialNumber());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{accestoken:");
        stringBuilder.append(getAccestoken());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{registerId:");
        stringBuilder.append(getRegisterId());
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
        RegisterDeviceModelRealmProxy aRegisterDeviceModel = (RegisterDeviceModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aRegisterDeviceModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aRegisterDeviceModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aRegisterDeviceModel.row.getIndex()) return false;

        return true;
    }

}
