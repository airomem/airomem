/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0 
 */

package pl.setblack.airomem.direct.impl;

/**
 *
 */
interface ElemHandler {

    Class getTargetType();

    void storeValue(Object target, Object val);

    void cleanValue(Object target);

    public String getName();

}
