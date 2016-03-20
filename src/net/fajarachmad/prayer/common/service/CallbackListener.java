package net.fajarachmad.prayer.common.service;

/**
 * Created by user on 3/19/2016.
 */
public interface CallbackListener<T> {

    void afterProcess(T result);
}
