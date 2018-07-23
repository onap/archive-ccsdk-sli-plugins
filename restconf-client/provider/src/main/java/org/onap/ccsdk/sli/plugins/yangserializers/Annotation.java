package org.onap.ccsdk.sli.plugins.yangserializers;

/**
 * Representation of an entity that represents annotated attribute.
 */
public class Annotation {

    private String name;
    private String value;

    /**
     * Creates an instance of annotation.
     *
     * @param n annotation name
     * @param v annotation value
     */
    public Annotation(String n, String v) {
        name = n;
        value = v;
    }

    /**
     * Returns name of annotation.
     *
     * @return name of annotation
     */
    public String name() {
        return name;
    }

    /**
     * Returns value of annotation.
     *
     * @return value of annotation
     */
    public String value() {
        return value;
    }
}
