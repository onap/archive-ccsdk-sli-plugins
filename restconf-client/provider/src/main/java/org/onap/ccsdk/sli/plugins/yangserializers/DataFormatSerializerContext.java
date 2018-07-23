package org.onap.ccsdk.sli.plugins.yangserializers;

import java.util.Map;

/**
 * Abstraction of data format serializer context.
 */
public class DataFormatSerializerContext {

    private Listener listener;
    private String uri;
    private Map<String, String> protocolAnnotation;

    /**
     * Creates an instance of data format serializer context.
     *
     * @param listener data format listener
     * @param uri URI corresponding to instance identifier
     * @param protocolAnnotation protocol annotations
     */
    public DataFormatSerializerContext(Listener listener, String uri,
        Map<String, String> protocolAnnotation) {
        this.listener = listener;
        this.uri = uri;
        this.protocolAnnotation = protocolAnnotation;
    }

    /**
     * Retruns data format listener.
     *
     * @return data format listener
     */
    public Listener listener() {
        return listener;
    }

    /**
     * Returns URI.
     *
     * @return URI
     */
    public String uri() {
        return uri;
    }

    /**
     * Returns protocol annotations.
     *
     * @return protocol annotations
     */
    public Map<String, String> getProtocolAnnotation() {
        return protocolAnnotation;
    }
}