
package org.onap.ccsdk.sli.plugins.template;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

// This directive can be used when handling optional json attributes in a template
// If an attribute value is null the entire name-value pair will not be rendered
// If an attribute value is not null the name-value pair will be rendered
// Additional optional parameters decide which values are quoted and if a comma and or newline should be appended
public class HideNullJson extends Directive {

	public String getName() {
		return "hideNullJson";
	}

	public int getType() {
		return BLOCK;
	}

	// The first parameter is the json key
	// The second parameter is the json value
	// This directive handles placing the colon between the json key and json value
	// The third parameter is a boolean, when true the json key is surrounded in double quotes by this directive
	// The third parameter is true by default and is optional
	// The fourth parameter is a boolean when true the json value is surrounded in double quotes by this directive
	// The fourth parameter is true by default and is optional
	// The fifth parameter is a boolean when true a comma is appended to the end
	// The fifth parameter is true by default and is optional
	// The sixth parameter is a boolean when true a newline is appended to the end
	// The sixth parameter is true by default and is optional
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		String tagValue = null;
		Object tagValueObject = node.jjtGetChild(1).value(context);
		if (tagValueObject == null) {
			return true;
		}
		tagValue = String.valueOf(tagValueObject);

		String tagName = String.valueOf(node.jjtGetChild(0).value(context));

		Boolean quoteTagName = getBooleanParameter(true,node,2,context);
		Boolean quoteTagValue = getBooleanParameter(true,node,3,context);
		Boolean appendComma = getBooleanParameter(true,node,4,context);
		Boolean appendNewLine = getBooleanParameter(true,node,5,context);

		StringBuilder sb = new StringBuilder();

		if (quoteTagName) {
			appendQuotedString(tagName,sb);
		}else {
			sb.append(tagName);
		}
		
		sb.append(":");	
	
		if (quoteTagValue) {
			appendQuotedString(tagValue,sb);
		}else {
			sb.append(tagValue);
		}

		if(appendComma) {
			sb.append(",");
		}

		if(appendNewLine) {
			sb.append("\n");
		}
		writer.write(sb.toString());
		return true;
	}
	
	private Boolean getBooleanParameter(Boolean defaultBool, Node node, int parameterPostion, InternalContextAdapter context) {
		if (node.jjtGetNumChildren() > parameterPostion && node.jjtGetChild(parameterPostion) != null) {
			Object val = node.jjtGetChild(parameterPostion).value(context);
			if (val != null) {
				return (Boolean) val;
			}
		}
		return defaultBool;
	}
	
	private void appendQuotedString(String str, StringBuilder sb) {
		sb.append("\"");
		sb.append(str);
		sb.append("\"");
	}

}