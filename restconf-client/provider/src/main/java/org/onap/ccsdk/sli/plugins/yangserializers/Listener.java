package org.onap.ccsdk.sli.plugins.yangserializers;

/**
 * Abstraction of listener.
 */
public interface Listener {

    /**
     * Returns serializer helper for this listener.
     *
     * @return serializer helper
     */
    SerializerHelper serializerHelper();
}
