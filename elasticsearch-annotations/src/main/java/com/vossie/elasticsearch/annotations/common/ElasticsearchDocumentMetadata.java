package com.vossie.elasticsearch.annotations.common;

import com.vossie.elasticsearch.annotations.ElasticsearchDocument;
import com.vossie.elasticsearch.annotations.ElasticsearchMapping;
import com.vossie.elasticsearch.annotations.exceptions.*;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

/**
 * Copyright © 2013 Carel Vosloo.
 * User: cvosloo
 * Date: 06/12/2013
 * Time: 09:34
 */
public class ElasticsearchDocumentMetadata {

    private String typeName;
    private ElasticsearchDocument elasticsearchDocument;
    private Map<String,ElasticsearchFieldMetadata> elasticsearchFields;
    private Map<String,ElasticsearchFieldMetadata> elasticsearchSystemFields;
    private Map<String, Object> attributes;

    public ElasticsearchDocumentMetadata(Class<?> clazz, ElasticsearchDocument elasticsearchDocument, Map<String, ElasticsearchFieldMetadata> elasticsearchFields, Map<String, ElasticsearchFieldMetadata> elasticsearchSystemFields)
            throws UnableToLoadConstraints, InvalidAttributeForType, ClassNotAnnotated, InvalidParentDocumentSpecified {

        // Set the type name
        this.typeName = (elasticsearchDocument.type().equals(Empty.NULL))
                ? UPPER_CAMEL.to(LOWER_HYPHEN, clazz.getSimpleName().toLowerCase())
                : elasticsearchDocument.type();

        this.elasticsearchDocument = elasticsearchDocument;
        this.elasticsearchFields = Collections.unmodifiableMap(elasticsearchFields);
        this.elasticsearchSystemFields = Collections.unmodifiableMap(elasticsearchSystemFields);

        // Todo: Find a way of doing this without the spring dependency.
        this.attributes = Collections.unmodifiableMap(AnnotationUtils.getAnnotationAttributes(elasticsearchDocument));

        // Init the XContent builder
        try {
            MetadataXContentBuilder.getXContentBuilder(this).string();
        } catch (IOException e) {
            throw new UnableToLoadConstraints(e);
        }
    }

    public Map<String, ElasticsearchFieldMetadata> getElasticsearchFields() {
        return elasticsearchFields;
    }

    /**
     * Get the index name.
     * @return index name
     */
    public String getIndexName(){
        return this.elasticsearchDocument.index();
    }

    /**
     * Get the type name for this index mapping.
     * If it is not set the class name will be changed from upper camel case to lower hyphen case.
     * @return
     */
    public String getTypeName() {
        return this.typeName;
    }

    /**
     * Get the parent
     * @return Parent
     */
    public ElasticsearchDocumentMetadata getParent() {

        try {
            return ElasticsearchMapping.getProperties(this.elasticsearchDocument.parent());

        } catch (AnnotationException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean hasParent() {
        return (!elasticsearchDocument.parent().getName().equals(Empty.class.getName()));
    }

    public boolean isChildType() {
        return (!Empty.class.isAssignableFrom(this.elasticsearchDocument.parent()));
    }

    /**
     * The time after which the indexed data will expire
     * @return The time to live
     */
    public String getTtl(){
        return this.elasticsearchDocument.ttl();
    }

    /**
     * Does this document have an expires after time.
     * @return True if the indexed item expires after a defined period.
     */
    public boolean hasTtl() {
        return (!this.elasticsearchDocument.ttl().equals(Empty.NULL));
    }

    /**
     * Should we store the source data in the index.
     * The _source field is an automatically generated field that stores the actual JSON that was used as the indexed document. It is not indexed (searchable), just stored. When executing "fetch" requests, like get or search, the _source field is returned by default.
     *
     * Though very handy to have around, the source field does incur storage overhead within the index. For this reason, it can be disabled. For example:
     * @return True if the source is stored.
     */
    public boolean isSourceStoredWithIndex() {
        return this.elasticsearchDocument.source();
    }

    /**
     * Get a list of field names.
     * @return
     */
    public String[] getFieldNames() {
        return this.getElasticsearchFields().keySet().toArray(new String[this.getElasticsearchFields().size()]);
    }

    /**
     * Get all the fields in this mapping.
     * @return Map of indexed fields.
     */
    public Map<String, ElasticsearchFieldMetadata> getFields() {
        return this.elasticsearchFields;
    }

    /**
     * Get the metadata associated with a specific field.
     * @param fieldName The name of the field to retrieve.
     * @return The meta data if found otherwise null.
     */
    public ElasticsearchFieldMetadata getFieldMetaData(String fieldName) {

        if(this.elasticsearchFields.containsKey(fieldName))
            return this.elasticsearchFields.get(fieldName);

        return null;
    }


    /**
     * Get a list of field names.
     * @return
     */
    public String[] getRootFieldNames() {
        return this.getRootFields().keySet().toArray(new String[this.getRootFields().size()]);
    }

    /**
     * Get all the fields in this mapping.
     * @return Map of indexed fields.
     */
    public Map<String, ElasticsearchFieldMetadata> getRootFields() {
        return this.elasticsearchSystemFields;
    }

    /**
     * Get the metadata associated with a specific field.
     * @param fieldName The name of the field to retrieve.
     * @return The meta data if found otherwise null.
     */
    public ElasticsearchFieldMetadata getRootFieldMetaData(String fieldName) {

        if(this.elasticsearchSystemFields.containsKey(fieldName))
            return this.elasticsearchSystemFields.get(fieldName);

        return null;
    }

    /**
     * Get a json formatted string representing the mapping;
     * @return The mapping.
     */
    @Override
    public String toString() {
        return toMapping();
    }

    /**
     * Get a json formatted string representing the mapping;
     * @return The mapping.
     * @throws IOException
     */
    public String toMapping() {

        try {
            return MetadataXContentBuilder.getXContentBuilder(this).string();

        } catch (AnnotationException e) {
            // This has been tested in the constructor so there is no need to report the errors.
            e.printStackTrace();
        } catch (IOException e) {
            // This has been tested in the constructor so there is no need to report the errors.
            e.printStackTrace();
        }

        return null;
    }
}
