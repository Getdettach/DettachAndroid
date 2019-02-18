package io.realm;


import android.util.JsonReader;
import io.realm.exceptions.RealmException;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Table;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.wwc.jajing.realmDB.CallerModel;
import com.wwc.jajing.realmDB.CallsModel;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.ExtendTimeModel;
import com.wwc.jajing.realmDB.HasAppModel;
import com.wwc.jajing.realmDB.MessageModel;
import com.wwc.jajing.realmDB.MissedMessageModel;
import com.wwc.jajing.realmDB.OptionsModel;
import com.wwc.jajing.realmDB.RegisterDeviceModel;
import com.wwc.jajing.realmDB.RepeatDaysModel;
import com.wwc.jajing.realmDB.SetStatusModel;
import com.wwc.jajing.realmDB.TimeModel;

@io.realm.annotations.RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {

    private static final List<Class<? extends RealmObject>> MODEL_CLASSES;
    static {
        List<Class<? extends RealmObject>> modelClasses = new ArrayList<Class<? extends RealmObject>>();
        modelClasses.add(CallerModel.class);
        modelClasses.add(ExtendTimeModel.class);
        modelClasses.add(RepeatDaysModel.class);
        modelClasses.add(MessageModel.class);
        modelClasses.add(TimeModel.class);
        modelClasses.add(HasAppModel.class);
        modelClasses.add(CallsModel.class);
        modelClasses.add(SetStatusModel.class);
        modelClasses.add(MissedMessageModel.class);
        modelClasses.add(ContactModel.class);
        modelClasses.add(OptionsModel.class);
        modelClasses.add(RegisterDeviceModel.class);
        MODEL_CLASSES = Collections.unmodifiableList(modelClasses);
    }

    @Override
    public Table createTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        checkClass(clazz);

        if (clazz.equals(CallerModel.class)) {
            return CallerModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(ExtendTimeModel.class)) {
            return ExtendTimeModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(RepeatDaysModel.class)) {
            return RepeatDaysModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(MessageModel.class)) {
            return MessageModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(TimeModel.class)) {
            return TimeModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(HasAppModel.class)) {
            return HasAppModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(CallsModel.class)) {
            return CallsModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(SetStatusModel.class)) {
            return SetStatusModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(MissedMessageModel.class)) {
            return MissedMessageModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(ContactModel.class)) {
            return ContactModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(OptionsModel.class)) {
            return OptionsModelRealmProxy.initTable(transaction);
        } else if (clazz.equals(RegisterDeviceModel.class)) {
            return RegisterDeviceModelRealmProxy.initTable(transaction);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void validateTable(Class<? extends RealmObject> clazz, ImplicitTransaction transaction) {
        checkClass(clazz);

        if (clazz.equals(CallerModel.class)) {
            CallerModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(ExtendTimeModel.class)) {
            ExtendTimeModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(RepeatDaysModel.class)) {
            RepeatDaysModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(MessageModel.class)) {
            MessageModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(TimeModel.class)) {
            TimeModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(HasAppModel.class)) {
            HasAppModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(CallsModel.class)) {
            CallsModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(SetStatusModel.class)) {
            SetStatusModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(MissedMessageModel.class)) {
            MissedMessageModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(ContactModel.class)) {
            ContactModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(OptionsModel.class)) {
            OptionsModelRealmProxy.validateTable(transaction);
        } else if (clazz.equals(RegisterDeviceModel.class)) {
            RegisterDeviceModelRealmProxy.validateTable(transaction);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public List<String> getFieldNames(Class<? extends RealmObject> clazz) {
        checkClass(clazz);

        if (clazz.equals(CallerModel.class)) {
            return CallerModelRealmProxy.getFieldNames();
        } else if (clazz.equals(ExtendTimeModel.class)) {
            return ExtendTimeModelRealmProxy.getFieldNames();
        } else if (clazz.equals(RepeatDaysModel.class)) {
            return RepeatDaysModelRealmProxy.getFieldNames();
        } else if (clazz.equals(MessageModel.class)) {
            return MessageModelRealmProxy.getFieldNames();
        } else if (clazz.equals(TimeModel.class)) {
            return TimeModelRealmProxy.getFieldNames();
        } else if (clazz.equals(HasAppModel.class)) {
            return HasAppModelRealmProxy.getFieldNames();
        } else if (clazz.equals(CallsModel.class)) {
            return CallsModelRealmProxy.getFieldNames();
        } else if (clazz.equals(SetStatusModel.class)) {
            return SetStatusModelRealmProxy.getFieldNames();
        } else if (clazz.equals(MissedMessageModel.class)) {
            return MissedMessageModelRealmProxy.getFieldNames();
        } else if (clazz.equals(ContactModel.class)) {
            return ContactModelRealmProxy.getFieldNames();
        } else if (clazz.equals(OptionsModel.class)) {
            return OptionsModelRealmProxy.getFieldNames();
        } else if (clazz.equals(RegisterDeviceModel.class)) {
            return RegisterDeviceModelRealmProxy.getFieldNames();
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public String getTableName(Class<? extends RealmObject> clazz) {
        checkClass(clazz);

        if (clazz.equals(CallerModel.class)) {
            return CallerModelRealmProxy.getTableName();
        } else if (clazz.equals(ExtendTimeModel.class)) {
            return ExtendTimeModelRealmProxy.getTableName();
        } else if (clazz.equals(RepeatDaysModel.class)) {
            return RepeatDaysModelRealmProxy.getTableName();
        } else if (clazz.equals(MessageModel.class)) {
            return MessageModelRealmProxy.getTableName();
        } else if (clazz.equals(TimeModel.class)) {
            return TimeModelRealmProxy.getTableName();
        } else if (clazz.equals(HasAppModel.class)) {
            return HasAppModelRealmProxy.getTableName();
        } else if (clazz.equals(CallsModel.class)) {
            return CallsModelRealmProxy.getTableName();
        } else if (clazz.equals(SetStatusModel.class)) {
            return SetStatusModelRealmProxy.getTableName();
        } else if (clazz.equals(MissedMessageModel.class)) {
            return MissedMessageModelRealmProxy.getTableName();
        } else if (clazz.equals(ContactModel.class)) {
            return ContactModelRealmProxy.getTableName();
        } else if (clazz.equals(OptionsModel.class)) {
            return OptionsModelRealmProxy.getTableName();
        } else if (clazz.equals(RegisterDeviceModel.class)) {
            return RegisterDeviceModelRealmProxy.getTableName();
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmObject> E newInstance(Class<E> clazz) {
        checkClass(clazz);

        if (clazz.equals(CallerModel.class)) {
            return clazz.cast(new CallerModelRealmProxy());
        } else if (clazz.equals(ExtendTimeModel.class)) {
            return clazz.cast(new ExtendTimeModelRealmProxy());
        } else if (clazz.equals(RepeatDaysModel.class)) {
            return clazz.cast(new RepeatDaysModelRealmProxy());
        } else if (clazz.equals(MessageModel.class)) {
            return clazz.cast(new MessageModelRealmProxy());
        } else if (clazz.equals(TimeModel.class)) {
            return clazz.cast(new TimeModelRealmProxy());
        } else if (clazz.equals(HasAppModel.class)) {
            return clazz.cast(new HasAppModelRealmProxy());
        } else if (clazz.equals(CallsModel.class)) {
            return clazz.cast(new CallsModelRealmProxy());
        } else if (clazz.equals(SetStatusModel.class)) {
            return clazz.cast(new SetStatusModelRealmProxy());
        } else if (clazz.equals(MissedMessageModel.class)) {
            return clazz.cast(new MissedMessageModelRealmProxy());
        } else if (clazz.equals(ContactModel.class)) {
            return clazz.cast(new ContactModelRealmProxy());
        } else if (clazz.equals(OptionsModel.class)) {
            return clazz.cast(new OptionsModelRealmProxy());
        } else if (clazz.equals(RegisterDeviceModel.class)) {
            return clazz.cast(new RegisterDeviceModelRealmProxy());
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public List<Class<? extends RealmObject>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override
    public Map<String, Long> getColumnIndices(Class<? extends RealmObject> clazz) {
        checkClass(clazz);

        if (clazz.equals(CallerModel.class)) {
            return CallerModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(ExtendTimeModel.class)) {
            return ExtendTimeModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(RepeatDaysModel.class)) {
            return RepeatDaysModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(MessageModel.class)) {
            return MessageModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(TimeModel.class)) {
            return TimeModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(HasAppModel.class)) {
            return HasAppModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(CallsModel.class)) {
            return CallsModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(SetStatusModel.class)) {
            return SetStatusModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(MissedMessageModel.class)) {
            return MissedMessageModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(ContactModel.class)) {
            return ContactModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(OptionsModel.class)) {
            return OptionsModelRealmProxy.getColumnIndices();
        } else if (clazz.equals(RegisterDeviceModel.class)) {
            return RegisterDeviceModelRealmProxy.getColumnIndices();
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmObject> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmObject, RealmObjectProxy> cache) {
        // This cast is correct because obj is either 
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(CallerModel.class)) {
            return clazz.cast(CallerModelRealmProxy.copyOrUpdate(realm, (CallerModel) obj, update, cache));
        } else if (clazz.equals(ExtendTimeModel.class)) {
            return clazz.cast(ExtendTimeModelRealmProxy.copyOrUpdate(realm, (ExtendTimeModel) obj, update, cache));
        } else if (clazz.equals(RepeatDaysModel.class)) {
            return clazz.cast(RepeatDaysModelRealmProxy.copyOrUpdate(realm, (RepeatDaysModel) obj, update, cache));
        } else if (clazz.equals(MessageModel.class)) {
            return clazz.cast(MessageModelRealmProxy.copyOrUpdate(realm, (MessageModel) obj, update, cache));
        } else if (clazz.equals(TimeModel.class)) {
            return clazz.cast(TimeModelRealmProxy.copyOrUpdate(realm, (TimeModel) obj, update, cache));
        } else if (clazz.equals(HasAppModel.class)) {
            return clazz.cast(HasAppModelRealmProxy.copyOrUpdate(realm, (HasAppModel) obj, update, cache));
        } else if (clazz.equals(CallsModel.class)) {
            return clazz.cast(CallsModelRealmProxy.copyOrUpdate(realm, (CallsModel) obj, update, cache));
        } else if (clazz.equals(SetStatusModel.class)) {
            return clazz.cast(SetStatusModelRealmProxy.copyOrUpdate(realm, (SetStatusModel) obj, update, cache));
        } else if (clazz.equals(MissedMessageModel.class)) {
            return clazz.cast(MissedMessageModelRealmProxy.copyOrUpdate(realm, (MissedMessageModel) obj, update, cache));
        } else if (clazz.equals(ContactModel.class)) {
            return clazz.cast(ContactModelRealmProxy.copyOrUpdate(realm, (ContactModel) obj, update, cache));
        } else if (clazz.equals(OptionsModel.class)) {
            return clazz.cast(OptionsModelRealmProxy.copyOrUpdate(realm, (OptionsModel) obj, update, cache));
        } else if (clazz.equals(RegisterDeviceModel.class)) {
            return clazz.cast(RegisterDeviceModelRealmProxy.copyOrUpdate(realm, (RegisterDeviceModel) obj, update, cache));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmObject> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        checkClass(clazz);

        if (clazz.equals(CallerModel.class)) {
            return clazz.cast(CallerModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(ExtendTimeModel.class)) {
            return clazz.cast(ExtendTimeModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(RepeatDaysModel.class)) {
            return clazz.cast(RepeatDaysModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(MessageModel.class)) {
            return clazz.cast(MessageModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(TimeModel.class)) {
            return clazz.cast(TimeModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(HasAppModel.class)) {
            return clazz.cast(HasAppModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(CallsModel.class)) {
            return clazz.cast(CallsModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(SetStatusModel.class)) {
            return clazz.cast(SetStatusModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(MissedMessageModel.class)) {
            return clazz.cast(MissedMessageModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(ContactModel.class)) {
            return clazz.cast(ContactModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(OptionsModel.class)) {
            return clazz.cast(OptionsModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else if (clazz.equals(RegisterDeviceModel.class)) {
            return clazz.cast(RegisterDeviceModelRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public <E extends RealmObject> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        checkClass(clazz);

        if (clazz.equals(CallerModel.class)) {
            return clazz.cast(CallerModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(ExtendTimeModel.class)) {
            return clazz.cast(ExtendTimeModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(RepeatDaysModel.class)) {
            return clazz.cast(RepeatDaysModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(MessageModel.class)) {
            return clazz.cast(MessageModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(TimeModel.class)) {
            return clazz.cast(TimeModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(HasAppModel.class)) {
            return clazz.cast(HasAppModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(CallsModel.class)) {
            return clazz.cast(CallsModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(SetStatusModel.class)) {
            return clazz.cast(SetStatusModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(MissedMessageModel.class)) {
            return clazz.cast(MissedMessageModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(ContactModel.class)) {
            return clazz.cast(ContactModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(OptionsModel.class)) {
            return clazz.cast(OptionsModelRealmProxy.createUsingJsonStream(realm, reader));
        } else if (clazz.equals(RegisterDeviceModel.class)) {
            return clazz.cast(RegisterDeviceModelRealmProxy.createUsingJsonStream(realm, reader));
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

}
