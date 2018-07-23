package org.onap.ccsdk.sli.plugins.yangserializers;

import java.util.Map;

/**
 * Abstraction of an entity which helps the data format serializers to obtain
 * schema context details and to build properties from data.
 *
 * @param <T> type of schema node
 */
public abstract class SerializerHelper<T> {

    private T rootSchema;
    private String rootURI;

    protected SerializerHelper(T t, String uri) {
        rootSchema = t;
    }

    /**
     * Returns root schema context node.
     *
     * @return root schema context node
     */
    protected abstract T getRootContext();

    /**
     * Returns current schema context node.
     *
     * @return current schema context node
     */
    protected abstract T getCurContext();

    /**
     * Returns child schema context node.
     *
     * @return child schema context node
     */
    protected abstract T getChildContext(T t, String name, String namespace);

    /**
     * Returns type of node
     * @param t node
     * @return node type
     */
    protected abstract NodeType getNodeType(T t);

    /**
     * Adds a node to current tree.
     *
     * @param name name of node
     * @param namespace namespace of node, it can be either module name or
     * namespace, null indicates parent namespace
     * @param value value of node, in case it's leaf/leaf-list node
     * @param valNamespace value namespace for identityref, could be module
     * name or namespace
     * @param type type of node if known like in case of JSON
     */
    protected abstract void addNode(String name, String namespace, String value,
        String valNamespace, NodeType type);

    /**
     * Exits the node, in case if it's leaf node add to properties map.
     */
    protected abstract void exitNode();

    /**
     * Returns the properties built corresponding to data.
     *
     * @return properties map
     */
    protected abstract Map<String, String> getProperties();
}
