package org.djr.eventlog.interceptor;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.djr.eventlog.annotations.EventLogAttribute;

public class InterceptedReturnStuct {
    @EventLogAttribute
    private String someReturnValue;
    @EventLogAttribute(maskAttribute = true)
    private String someReturnMasked;

    public InterceptedReturnStuct(String someReturnValue, String someReturnMasked) {
        this.someReturnValue = someReturnValue;
        this.someReturnMasked = someReturnMasked;
    }

    public String getSomeReturnValue() {
        return someReturnValue;
    }

    public void setSomeReturnValue(String someReturnValue) {
        this.someReturnValue = someReturnValue;
    }

    public String getSomeReturnMasked() {
        return someReturnMasked;
    }

    public void setSomeReturnMasked(String someReturnMasked) {
        this.someReturnMasked = someReturnMasked;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

