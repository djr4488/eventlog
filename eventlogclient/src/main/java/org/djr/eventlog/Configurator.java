package org.djr.eventlog;

import org.aeonbits.owner.ConfigFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Configurator {
    @SuppressWarnings("unchecked")
    public <T> T getConfiguration(Class clazz) {
        return (T)ConfigFactory.create(clazz);
    }
}
