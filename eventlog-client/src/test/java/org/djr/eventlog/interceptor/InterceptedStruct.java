package org.djr.eventlog.interceptor;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.djr.eventlog.annotations.EventLogAttribute;

public class InterceptedStruct {
    @EventLogAttribute(maskAttribute = true, attributeName = "somethingOfValue")
    private String someValue;
    @EventLogAttribute
    private int someIntValue;
    @EventLogAttribute
    private boolean someBooleanValue;

    public InterceptedStruct() {
    }

    public InterceptedStruct(String someValue, int someIntValue, boolean someBooleanValue) {
        this.someValue = someValue;
        this.someIntValue = someIntValue;
        this.someBooleanValue = someBooleanValue;
    }

    public String getSomeValue() {
        return someValue;
    }

    public void setSomeValue(String someValue) {
        this.someValue = someValue;
    }

    public int getSomeIntValue() {
        return someIntValue;
    }

    public void setSomeIntValue(int someIntValue) {
        this.someIntValue = someIntValue;
    }

    public boolean isSomeBooleanValue() {
        return someBooleanValue;
    }

    public void setSomeBooleanValue(boolean someBooleanValue) {
        this.someBooleanValue = someBooleanValue;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
