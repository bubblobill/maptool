package net.rptools.maptool.client.functions.exceptions;

import com.google.gson.JsonElement;
import net.rptools.maptool.language.I18N;
import net.rptools.parser.ParserException;
import org.apache.commons.lang3.tuple.Pair;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserExceptionBuilder {
    private ParserException parserException;
    private Throwable throwable = null;
    private List<Pair<String, Object>> messageParams;
    private static ParserExceptionBuilder instance;
    private String msgKey = null;
    private ParserExceptionBuilder() {}
    private void checkInitialised(){
        if(instance == null){
            start();
        }
    }
    public ParserException build() {
        if (throwable != null) {
            return new ParserException(throwable);
        } else if (msgKey != null) {
            return new ParserException(I18N.getMessage(msgKey, messageParams));
        } else {
            return new ParserException(I18N.getMessage("macro.function.general.unknownError"));
        }
    }

    public static ParserExceptionBuilder start(){
        return start(null);
    }
    public static ParserExceptionBuilder start(String i18nKey){
        instance = new ParserExceptionBuilder();
        instance.messageParams = new ArrayList<>();
        instance.throwable = null;
        instance.msgKey = i18nKey;
        return instance;
    }
    public ParserExceptionBuilder forThrowable(final Throwable cause) {
        checkInitialised();
        throwable = cause;
        return this;
    }
    public ParserExceptionBuilder functionName(final String functionName){
        checkInitialised();
        messageParams.add(Pair.of("functionName", functionName));
        return this;
    }
    public ParserExceptionBuilder i18nKey(final String i18nKey){
        checkInitialised();
        instance.msgKey = i18nKey;
        return this;
    }
    public ParserExceptionBuilder parameterIndex(final int parameterIndex){
        checkInitialised();
        messageParams.add(Pair.of("parameterIndex", parameterIndex));
        return this;
    }
    public ParserExceptionBuilder options(String options){
        checkInitialised();
        messageParams.add(Pair.of("options", options));
        return this;
    }
    public ParserExceptionBuilder results(String results){
        checkInitialised();
        messageParams.add(Pair.of("results", results));
        return this;
    }
    public ParserExceptionBuilder parameterValue(Object parameterValue){
        checkInitialised();
        if(parameterValue instanceof JsonElement je){
            parameterValue = je.toString();
        } else if(parameterValue instanceof List<?> list){
            parameterValue = Arrays.deepToString(list.toArray());
        } else if(parameterValue instanceof Object[] array){
            parameterValue = Arrays.deepToString(array);
        }
        messageParams.add(Pair.of("parameterValue", parameterValue));
        return this;
    }



}
