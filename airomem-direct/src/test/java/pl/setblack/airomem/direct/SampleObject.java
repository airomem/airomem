/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0 
 */

package pl.setblack.airomem.direct;

import java.io.Serializable;

/**
 *
 * @author jratajsk
 */
public class SampleObject implements Serializable{
    private static final long serialVersionUID = 1l;
    
    private String field1;
    
    private String field2;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
    
}
