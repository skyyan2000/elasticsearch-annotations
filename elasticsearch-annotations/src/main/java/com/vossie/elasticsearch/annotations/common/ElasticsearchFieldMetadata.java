package com.vossie.elasticsearch.annotations.common;

import com.vossie.elasticsearch.annotations.ElasticsearchField;
import com.vossie.elasticsearch.annotations.enums.BooleanValue;
import com.vossie.elasticsearch.annotations.enums.ElasticsearchType;
import com.vossie.elasticsearch.annotations.util.ESTypeAttributeConstraints;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2013 Carel Vosloo.
 * User: cvosloo
 * Date: 06/12/2013
 * Time: 09:36
 */
public class ElasticsearchFieldMetadata {

    private boolean isArray;
    private String fieldName;
    private ElasticsearchField elasticsearchField;
    private Map<String, ElasticsearchFieldMetadata> children;
    private Map<String, Object> attributes;

    public ElasticsearchFieldMetadata(String fieldName, ElasticsearchField elasticsearchField, boolean isArray, Map<String,ElasticsearchFieldMetadata> children){

        this.fieldName = fieldName;
        this.elasticsearchField = elasticsearchField;
        this.isArray = isArray;
        this.children = Collections.unmodifiableMap(children);

        setAttributes();
    }

    private void setAttributes() {

        // Todo: Find a way of doing this without the spring dependency.
        Map<String, Object> allAttributes = Collections.unmodifiableMap(AnnotationUtils.getAnnotationAttributes(elasticsearchField));

        ESTypeAttributeConstraints constraints = new ESTypeAttributeConstraints();
        Map<String, Object> tempAttributes = new HashMap<>();

        for(String key : allAttributes.keySet()) {

            if(allAttributes.get(key).toString().equals(Empty.NULL))
                continue;
            if(allAttributes.get(key).toString().equals("0") || allAttributes.get(key).toString().equals("0.0"))
                continue;
            else if(!constraints.isValidAttributeForType(this.elasticsearchField.type(), key))
                continue;

            tempAttributes.put(key, allAttributes.get(key));
        }


        this.attributes = Collections.unmodifiableMap(tempAttributes);
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getAnalyzer() {
        return this.elasticsearchField.analyzer();
    }

    public ElasticsearchType getType() {
        return this.elasticsearchField.type();
    }

    public boolean isParentId() {
        return (this.elasticsearchField.isParentId().equals(BooleanValue.TRUE));
    }

    public Map<String, ElasticsearchFieldMetadata> getChildren() {
        return this.children;
    }

    public boolean isArray() {
        return this.isArray;
    }

    /**
     * Should we use this field as the default sort order for queries if none is specified.
     * @return Boolean
     */
    public boolean isDefaultSortByField() {
        return (this.elasticsearchField.isDefaultSortByField().equals(BooleanValue.TRUE));
    }

    /**
     * The default sort order to use if no sort order is specified.
     * @return
     */
    public SortOrder getDefaultSortOrder() {
        return this.elasticsearchField.defaultSortOrder();
    }

    public void getFieldAttributes() {


    }
}