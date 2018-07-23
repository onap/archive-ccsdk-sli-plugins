package org.onap.ccsdk.sli.plugins.yangserializers;

import java.util.List;
import java.util.Map;

/**
 * Representation of JSON serializer.
 */
public class JsonSerializer extends DataFormatSerializer {

    /**
     * Creates an instance of data format serializer.
     *
     * @param serializerContext data format serializer context
     */
    protected JsonSerializer(DataFormatSerializerContext serializerContext) {
        super(DataFormat.JSON, serializerContext);
    }

    @Override
    public String encode(Map<String, String> param, Map<String, List<Annotation>> annotations) {
        return null;
    }

    @Override
    public Map<String, String> decode(String dataFormatBody) {
        return null;
    }
}
