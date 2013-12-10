package com.vossie.elasticsearch.annotations;

import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;

/**
 * Copyright © 2013 Carel Vosloo.
 * User: cvosloo
 * Date: 06/12/2013
 * Time: 09:36
 */
public class ElasticsearchFieldMetadataTemp {

    private boolean isArray;
    private String fieldName;
    private ElasticsearchField elasticsearchField;
    private ArrayList<ElasticsearchFieldMetadataTemp> children = new ArrayList<>();

    public ElasticsearchFieldMetadataTemp(String fieldName, ElasticsearchField elasticsearchField){
        this.fieldName = fieldName;
        this.elasticsearchField = elasticsearchField;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getAnalyzer() {
        return this.elasticsearchField.analyzer();
    }

    public ElasticsearchField.Type getCoreType() {
        return this.elasticsearchField.type();
    }

    public boolean isParentId() {
        return this.elasticsearchField.isParentId();
    }

    public ArrayList<ElasticsearchFieldMetadataTemp> getChildren() {
        return children;
    }

    public boolean isArray() {
        return isArray;
    }

    public ElasticsearchFieldMetadataTemp setArray(boolean array) {
        isArray = array;
        return this;
    }

    /**
     * Should we use this field as the default sort order for queries if none is specified.
     * @return Boolean
     */
    public boolean isDefaultSortByField() {
        return this.elasticsearchField.isDefaultSortByField();
    }

    /**
     * The default sort order to use if no sort order is specified.
     * @return
     */
    public SortOrder getDefaultSortOrder() {
        return this.elasticsearchField.defaultSortOrder();
    }

    public ElasticsearchFieldMetadataTemp setChildren(ArrayList<ElasticsearchFieldMetadataTemp> children) {
        this.children = children;
        return this;
    }
}
