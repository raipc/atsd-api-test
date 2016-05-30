package com.axibase.tsd.api.builder;

import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
public interface Builder<T> {


    public T build(Map fields);

    public T buildRandom();

}
