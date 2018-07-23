package org.onap.ccsdk.sli.plugins.yangserializers;

import java.util.List;
import java.util.Map;

/**
 * Representation of XML serializer.
 */
public class XmlSerializer extends DataFormatSerializer {

    /**
     * Creates an instance of XML serializer.
     *
     * @param serializerContext data format serializer context
     */
    protected XmlSerializer(DataFormatSerializerContext serializerContext) {
        super(DataFormat.XML, serializerContext);
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