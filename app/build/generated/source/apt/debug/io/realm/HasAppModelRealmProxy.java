package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.wwc.jajing.realmDB.HasAppModel;
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

public class HasAppModelRealmProxy extends HasAppModel
    implements RealmObjectProxy {

    private static long INDEX_ID;
    private static long INDEX_NUMBER;
    private static long INDEX_PICTURE;
    private static long INDEX_ACCESSTOKEN;
    private static long INDEX_AVAILTIME;
    private static long INDEX_CALALLOWSTATUS;
    private static long INDEX_RECEALLOWSTATUS;
    private static long INDEX_STATUSNAME;
    private static long INDEX_ISACTIVE;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("number");
        fieldNames.add("picture");
        fieldNames.add("accessToken");
        fieldNames.add("availTime");
        fieldNames.add("calAllowStatus");
        fieldNames.add("receAllowStatus");
        fieldNames.add("statusName");
        fieldNames.add("isActive");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
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

    @Override
    public String getAccessToken() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_ACCESSTOKEN);
    }

    @Override
    public void setAccessToken(String value) {
        realm.checkIfValid();
        row.setString(INDEX_ACCESSTOKEN, (String) value);
    }

    @Override
    public String getAvailTime() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_AVAILTIME);
    }

    @Override
    public void setAvailTime(String value) {
        realm.checkIfValid();
        row.setString(INDEX_AVAILTIME, (String) value);
    }

    @Override
    public String getCalAllowStatus() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_CALALLOWSTATUS);
    }

    @Override
    public void setCalAllowStatus(String value) {
        realm.checkIfValid();
        row.setString(INDEX_CALALLOWSTATUS, (String) value);
    }

    @Override
    public String getReceAllowStatus() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_RECEALLOWSTATUS);
    }

    @Override
    public void setReceAllowStatus(String value) {
        realm.checkIfValid();
        row.setString(INDEX_RECEALLOWSTATUS, (String) value);
    }

    @Override
    public String getStatusName() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_STATUSNAME);
    }

    @Override
    public void setStatusName(String value) {
        realm.checkIfValid();
        row.setString(INDEX_STATUSNAME, (String) value);
    }

    @Override
    public String getIsActive() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_ISACTIVE);
    }

    @Override
    public void setIsActive(String value) {
        realm.checkIfValid();
        row.setString(INDEX_ISACTIVE, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_HasAppModel")) {
            Table table = transaction.getTable("class_HasAppModel");
            table.addColumn(ColumnType.STRING, "id");
            table.addColumn(ColumnType.STRING, "number");
            table.addColumn(ColumnType.STRING, "picture");
            table.addColumn(ColumnType.STRING, "accessToken");
            table.addColumn(ColumnType.STRING, "availTime");
            table.addColumn(ColumnType.STRING, "calAllowStatus");
            table.addColumn(ColumnType.STRING, "receAllowStatus");
            table.addColumn(ColumnType.STRING, "statusName");
            table.addColumn(ColumnType.STRING, "isActive");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_HasAppModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_HasAppModel")) {
            Table table = transaction.getTable("class_HasAppModel");
            if (table.getColumnCount() != 9) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 9 but was " + table.getColumnCount());
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for (long i = 0; i < 9; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type HasAppModel");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_ID = table.getColumnIndex("id");
            INDEX_NUMBER = table.getColumnIndex("number");
            INDEX_PICTURE = table.getColumnIndex("picture");
            INDEX_ACCESSTOKEN = table.getColumnIndex("accessToken");
            INDEX_AVAILTIME = table.getColumnIndex("availTime");
            INDEX_CALALLOWSTATUS = table.getColumnIndex("calAllowStatus");
            INDEX_RECEALLOWSTATUS = table.getColumnIndex("receAllowStatus");
            INDEX_STATUSNAME = table.getColumnIndex("statusName");
            INDEX_ISACTIVE = table.getColumnIndex("isActive");

            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'id'");
            }
            if (columnTypes.get("id") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'id'");
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
            if (!columnTypes.containsKey("accessToken")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'accessToken'");
            }
            if (columnTypes.get("accessToken") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'accessToken'");
            }
            if (!columnTypes.containsKey("availTime")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'availTime'");
            }
            if (columnTypes.get("availTime") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'availTime'");
            }
            if (!columnTypes.containsKey("calAllowStatus")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'calAllowStatus'");
            }
            if (columnTypes.get("calAllowStatus") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'calAllowStatus'");
            }
            if (!columnTypes.containsKey("receAllowStatus")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'receAllowStatus'");
            }
            if (columnTypes.get("receAllowStatus") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'receAllowStatus'");
            }
            if (!columnTypes.containsKey("statusName")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'statusName'");
            }
            if (columnTypes.get("statusName") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'statusName'");
            }
            if (!columnTypes.containsKey("isActive")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'isActive'");
            }
            if (columnTypes.get("isActive") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'isActive'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The HasAppModel class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_HasAppModel";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static HasAppModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        HasAppModel obj = realm.createObject(HasAppModel.class);
        if (json.has("id")) {
            if (json.isNull("id")) {
                obj.setId("");
            } else {
                obj.setId((String) json.getString("id"));
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
        if (json.has("accessToken")) {
            if (json.isNull("accessToken")) {
                obj.setAccessToken("");
            } else {
                obj.setAccessToken((String) json.getString("accessToken"));
            }
        }
        if (json.has("availTime")) {
            if (json.isNull("availTime")) {
                obj.setAvailTime("");
            } else {
                obj.setAvailTime((String) json.getString("availTime"));
            }
        }
        if (json.has("calAllowStatus")) {
            if (json.isNull("calAllowStatus")) {
                obj.setCalAllowStatus("");
            } else {
                obj.setCalAllowStatus((String) json.getString("calAllowStatus"));
            }
        }
        if (json.has("receAllowStatus")) {
            if (json.isNull("receAllowStatus")) {
                obj.setReceAllowStatus("");
            } else {
                obj.setReceAllowStatus((String) json.getString("receAllowStatus"));
            }
        }
        if (json.has("statusName")) {
            if (json.isNull("statusName")) {
                obj.setStatusName("");
            } else {
                obj.setStatusName((String) json.getString("statusName"));
            }
        }
        if (json.has("isActive")) {
            if (json.isNull("isActive")) {
                obj.setIsActive("");
            } else {
                obj.setIsActive((String) json.getString("isActive"));
            }
        }
        return obj;
    }

    public static HasAppModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        HasAppModel obj = realm.createObject(HasAppModel.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setId("");
                    reader.skipValue();
                } else {
                    obj.setId((String) reader.nextString());
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
            } else if (name.equals("accessToken")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setAccessToken("");
                    reader.skipValue();
                } else {
                    obj.setAccessToken((String) reader.nextString());
                }
            } else if (name.equals("availTime")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setAvailTime("");
                    reader.skipValue();
                } else {
                    obj.setAvailTime((String) reader.nextString());
                }
            } else if (name.equals("calAllowStatus")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setCalAllowStatus("");
                    reader.skipValue();
                } else {
                    obj.setCalAllowStatus((String) reader.nextString());
                }
            } else if (name.equals("receAllowStatus")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setReceAllowStatus("");
                    reader.skipValue();
                } else {
                    obj.setReceAllowStatus((String) reader.nextString());
                }
            } else if (name.equals("statusName")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setStatusName("");
                    reader.skipValue();
                } else {
                    obj.setStatusName((String) reader.nextString());
                }
            } else if (name.equals("isActive")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setIsActive("");
                    reader.skipValue();
                } else {
                    obj.setIsActive((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static HasAppModel copyOrUpdate(Realm realm, HasAppModel object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static HasAppModel copy(Realm realm, HasAppModel newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        HasAppModel realmObject = realm.createObject(HasAppModel.class);
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setId(newObject.getId() != null ? newObject.getId() : "");
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setPicture(newObject.getPicture() != null ? newObject.getPicture() : "");
        realmObject.setAccessToken(newObject.getAccessToken() != null ? newObject.getAccessToken() : "");
        realmObject.setAvailTime(newObject.getAvailTime() != null ? newObject.getAvailTime() : "");
        realmObject.setCalAllowStatus(newObject.getCalAllowStatus() != null ? newObject.getCalAllowStatus() : "");
        realmObject.setReceAllowStatus(newObject.getReceAllowStatus() != null ? newObject.getReceAllowStatus() : "");
        realmObject.setStatusName(newObject.getStatusName() != null ? newObject.getStatusName() : "");
        realmObject.setIsActive(newObject.getIsActive() != null ? newObject.getIsActive() : "");
        return realmObject;
    }

    static HasAppModel update(Realm realm, HasAppModel realmObject, HasAppModel newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setId(newObject.getId() != null ? newObject.getId() : "");
        realmObject.setNumber(newObject.getNumber() != null ? newObject.getNumber() : "");
        realmObject.setPicture(newObject.getPicture() != null ? newObject.getPicture() : "");
        realmObject.setAccessToken(newObject.getAccessToken() != null ? newObject.getAccessToken() : "");
        realmObject.setAvailTime(newObject.getAvailTime() != null ? newObject.getAvailTime() : "");
        realmObject.setCalAllowStatus(newObject.getCalAllowStatus() != null ? newObject.getCalAllowStatus() : "");
        realmObject.setReceAllowStatus(newObject.getReceAllowStatus() != null ? newObject.getReceAllowStatus() : "");
        realmObject.setStatusName(newObject.getStatusName() != null ? newObject.getStatusName() : "");
        realmObject.setIsActive(newObject.getIsActive() != null ? newObject.getIsActive() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("HasAppModel = [");
        stringBuilder.append("{id:");
        stringBuilder.append(getId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{number:");
        stringBuilder.append(getNumber());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{picture:");
        stringBuilder.append(getPicture());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{accessToken:");
        stringBuilder.append(getAccessToken());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{availTime:");
        stringBuilder.append(getAvailTime());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{calAllowStatus:");
        stringBuilder.append(getCalAllowStatus());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{receAllowStatus:");
        stringBuilder.append(getReceAllowStatus());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{statusName:");
        stringBuilder.append(getStatusName());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isActive:");
        stringBuilder.append(getIsActive());
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
        HasAppModelRealmProxy aHasAppModel = (HasAppModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aHasAppModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aHasAppModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aHasAppModel.row.getIndex()) return false;

        return true;
    }

}
