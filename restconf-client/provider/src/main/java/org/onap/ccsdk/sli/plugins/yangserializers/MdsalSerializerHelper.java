package org.onap.ccsdk.sli.plugins.yangserializers;

import java.util.Map;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;

/**
 * Representation of MDSAL based schema helper.
 */
public class MdsalSerializerHelper extends SerializerHelper<SchemaNode> {

    protected MdsalSerializerHelper(SchemaNode node, String uri) {
        super(node, uri);
    }

    @Override
    public SchemaNode getRootContext() {
        return null;
    }

    @Override
    public SchemaNode getCurContext() {
        return null;
    }

    @Override
    public SchemaNode getChildContext(SchemaNode schemaNode, String name, String namespace) {
        return null;
    }

    @Override
    public NodeType getNodeType(SchemaNode schemaNode) {
        return null;
    }

    @Override
    public void addNode(String name, String namespace, String value, String valNamespace, NodeType type) {
    }

    @Override
    public void exitNode() {
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }
}
