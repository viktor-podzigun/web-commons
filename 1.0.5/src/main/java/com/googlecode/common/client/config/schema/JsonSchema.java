
package com.googlecode.common.client.config.schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gwt.json.client.JSONObject;
import com.googlecode.common.client.util.CollectionsUtil;
import com.googlecode.common.client.util.JSONHelpers;


public final class JsonSchema {

    private final Map<String, AbstractNode> refs = 
        new HashMap<String, AbstractNode>();
    
    private final JSONObject    schema;
    private final ObjectNode    root;
    
    
    private JsonSchema(JSONObject schema) {
        this.schema = schema;
        
        ObjectNode root = parseObject("#", schema);
        if (root.getTitle() == null) {
            root.setTitle("Root");
        }
        
        this.root = root;
    }

    public static ObjectNode parse(JSONObject schema) {
        return new JsonSchema(schema).root;
    }
    
    private AbstractNode parseNode(JSONObject jsonNode) {
        AbstractNode node = null;
        JSONObject refNode = null;
        String ref = JSONHelpers.getString(jsonNode, "$ref");
        if (ref != null) {
            if (!ref.startsWith("#")) {
                throw new IllegalArgumentException(
                        "External refs are not supported: " + ref);
            }
            
            node = refs.get(ref);
            if (node == null) {
                String[] path = ref.split("/");
                refNode = schema;
                final int count = path.length;
                for (int i = 1; i < count && refNode != null; i++) {
                    refNode = JSONHelpers.getObject(refNode, path[i]);
                }
                
                if (refNode == null) {
                    throw new IllegalArgumentException(
                            "Unresolvable ref: " + ref);
                }
            } else {
                node = node.asRef();
            }
        }
        
        if (node == null) {
            JSONObject json = (refNode != null ? refNode : jsonNode);
            String typeName = JSONHelpers.getString(json, "type");
            JsonType type = JsonType.fromName(typeName);
    
            switch (type) {
            case OBJECT:    node = parseObject(ref, json);  break;
            case ARRAY:     node = parseArray(ref, json);   break;
            case NUMBER:    node = parseNumber(ref, json);  break;
            case INTEGER:   node = parseInteger(ref, json); break;
            case STRING:    node = parseString(ref, json);  break;
            case BOOLEAN:   node = parseBoolean(ref, json); break;
            
            default:
                throw new RuntimeException("Unsupported type: " + typeName);
            }
        }
        
        return parseInfo(null, jsonNode, node);
    }
    
    private <T extends AbstractNode> T parseInfo(String ref, 
            JSONObject json, T node) {
        
        if (ref != null) {
            refs.put(ref, node);
        }
        
        node.setTitle(JSONHelpers.getString(json, "title"));
        node.setDescription(JSONHelpers.getString(json, "description"));
        return node;
    }
    
    private ObjectNode parseObject(String ref, JSONObject json) {
        ObjectNode node = new ObjectNode();
        parseInfo(ref, json, node);
        
        JSONObject props = JSONHelpers.getObject(json, "properties");
        if (props == null) {
            throw new IllegalArgumentException("properties node not found");
        }
        
        Set<String> keys = props.keySet();
        List<PropertyNode<?>> properties = null;
        List<ComplexNode> nodes = null;
        
        for (String key : keys) {
            AbstractNode n = parseNode(JSONHelpers.getObject(props, key));
            n.setKey(key);
            
            if (n instanceof PropertyNode) {
                properties = CollectionsUtil.addToList(properties, 
                        (PropertyNode<?>)n);
            } else {
                nodes = CollectionsUtil.addToList(nodes, 
                        (ComplexNode)n);
            }
        }
        
        node.setProperties(properties);
        node.setNodes(nodes);
        return (ref != null ? node.asRef() : node);
    }
    
    private ArrayNode<?> parseArray(String ref, JSONObject json) {
        JSONObject items = JSONHelpers.getObject(json, "items");
        if (items == null) {
            throw new IllegalArgumentException("items node not found");
        }

        ArrayNode<?> node = null;
        AbstractNode n = parseNode(items);
        if (n instanceof ObjectNode) {
            node = new ObjectArrayNode((ObjectNode)n);
        
        } else if (n instanceof ArrayNode) {
            node = new ArrayArrayNode((ArrayNode<?>)n);
        } else {
            node = new PropertyArrayNode((PropertyNode<?>)n);
        }
        
        Integer maxItems = JSONHelpers.getInteger(json, "maxItems");
        if (maxItems != null) {
            node.setMaxItems(maxItems);
        }
        
        Integer minItems = JSONHelpers.getInteger(json, "minItems");
        if (minItems != null) {
            node.setMinItems(minItems);
        }
        
        Boolean uniqueItems = JSONHelpers.getBoolean(json, "uniqueItems");
        if (uniqueItems != null) {
            node.setUniqueItems(uniqueItems);
        }
        
        parseInfo(ref, json, node);
        return (ref != null ? node.asRef() : node);
    }
    
    private NumberNode parseNumber(String ref, JSONObject json) {
        NumberNode node = new NumberNode();
        parseInfo(ref, json, node);
        
        node.setDefault(JSONHelpers.getNumber(json, "default"));
        node.setMaximum(JSONHelpers.getNumber(json, "maximum"));
        node.setMinimum(JSONHelpers.getNumber(json, "minimum"));
        return (ref != null ? node.asRef() : node);
    }
    
    private IntegerNode parseInteger(String ref, JSONObject json) {
        IntegerNode node = new IntegerNode();
        parseInfo(ref, json, node);
        
        node.setDefault(JSONHelpers.getInteger(json, "default"));
        node.setMaximum(JSONHelpers.getInteger(json, "maximum"));
        node.setMinimum(JSONHelpers.getInteger(json, "minimum"));
        return (ref != null ? node.asRef() : node);
    }
    
    private StringNode parseString(String ref, JSONObject json) {
        StringNode node = new StringNode();
        parseInfo(ref, json, node);
        
        node.setDefault(JSONHelpers.getString(json, "default"));
        return (ref != null ? node.asRef() : node);
    }
    
    private BooleanNode parseBoolean(String ref, JSONObject json) {
        BooleanNode node = new BooleanNode();
        parseInfo(ref, json, node);
        
        node.setDefault(JSONHelpers.getBoolean(json, "default"));
        return (ref != null ? node.asRef() : node);
    }
    
}
