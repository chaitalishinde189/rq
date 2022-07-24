package com.example.rqchallenge.employees.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 * @author chaitali shinde
 * Class to deserialize object or list of objects of type T into list of objects
 */
public class ListOrObjectJsonDeserializer<T> extends JsonDeserializer<List<T>> {

    private final Class<T> cls;

    public ListOrObjectJsonDeserializer() {
        final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.cls = (Class<T>) type.getActualTypeArguments()[0];
    }

    @Override
    public List<T> deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final ObjectCodec objectCodec = p.getCodec();
        final JsonNode listOrObjectNode = objectCodec.readTree(p);
        final List<T> result = new ArrayList<>();
        if (listOrObjectNode.isArray()) {
            for (JsonNode node : listOrObjectNode) {
                result.add(objectCodec.treeToValue(node, cls));
            }
        } else {
            result.add(objectCodec.treeToValue(listOrObjectNode, cls));
        }
        return result;
    }

}
