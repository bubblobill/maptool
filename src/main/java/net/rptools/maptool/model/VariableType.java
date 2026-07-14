package net.rptools.maptool.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.rptools.maptool.language.I18N;

public enum VariableType {
    UNDEFINED("variable.type.undefined", String.class),
    JSON("variable.type.json", JsonElement.class),
    JSON_ARRAY("variable.type.jsonArray", JsonArray.class),
    JSON_OBJECT("variable.type.jsonObject", JsonObject.class),
    NUMBER("variable.type.number", Number.class),
    STRING("variable.type.string", String.class),
    STRING_LIST("variable.type.stringList", String.class),
    STRING_PROP_LIST("variable.type.stringPropList", String.class),
    ;
    final String displayName;
    final Class<?> klass;
    VariableType(String i18nKey, Class<?> klass){
        this.displayName = I18N.getText(i18nKey);
        this.klass = klass;
    }

    public Class<?> getKlass() {
        return klass;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
