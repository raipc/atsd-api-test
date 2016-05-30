package com.axibase.tsd.api.method.property;

import com.axibase.tsd.api.method.Method;

/**
 * @author Dmitry Korchagin.
 */
abstract  public class PropertyMethod extends Method {
    protected static final String METHOD_PROPERTY_INSERT = "/properties/insert";
    protected static final String METHOD_PROPERTY_QUERY = "/properties/query";
    protected static final String METHOD_PROPERTY_DELETE = "/properties/delete";
}
