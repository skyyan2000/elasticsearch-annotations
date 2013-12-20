package com.vossie.elasticsearch.annotations.common;

import com.vossie.elasticsearch.annotations.ElasticsearchMapping;
import com.vossie.elasticsearch.annotations.enums.FieldName;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Copyright © 2013 GSMA. GSM and the GSM Logo are registered and owned by the GSMA.
 * User: cvosloo
 * Date: 10/12/2013
 * Time: 10:02
 */
public final class MetadataXContentBuilder {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MetadataXContentBuilder.class);

    private static HashMap<String, XContentBuilder> cache = new HashMap<>();

    protected static XContentBuilder getXContentBuilder(ElasticsearchDocumentMetadata elasticsearchDocumentMetadata) {

        String key = String.format("%s-%s", elasticsearchDocumentMetadata.getIndexName(), elasticsearchDocumentMetadata.getTypeName());

        // Return from cache if it has been previously parsed.
        if(cache.containsKey(key))
            return cache.get(key);

        try {

            /** Set the objects type name */
            XContentBuilder xbMapping = jsonBuilder()
                    .startObject()
                    .startObject(elasticsearchDocumentMetadata.getTypeName());


            for(String rootFieldName : elasticsearchDocumentMetadata.getRootFieldNames()) {

                ElasticsearchFieldMetadata rootField = elasticsearchDocumentMetadata.getRootFieldMetaData(rootFieldName);
                xbMapping.startObject(rootField.getFieldName());

                for(String attributeName : rootField.getAttributes().keySet()) {

                    if(rootFieldName.equals(FieldName._PARENT.toString()) && attributeName.equals("type")) {

                        xbMapping.field(
                                attributeName,
                                elasticsearchDocumentMetadata.getParent().getTypeName()
                        );
                    }
                    if(rootField.getAttributes().get(attributeName).getClass().isArray()) {
                        xbMapping.field(attributeName,rootField.getAttributes().get(attributeName));
                    }
                    else
                        xbMapping.field(attributeName,rootField.getAttributes().get(attributeName).toString());
                }

                xbMapping.endObject();
            }

            // Add the fields.
            setXContentBuilderFields(xbMapping, elasticsearchDocumentMetadata.getFields());

            // End
            xbMapping
                    .endObject()
                    .endObject();

            // Add to local cache.
            cache.put(key, xbMapping);

            return xbMapping;

        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * Populate the child field nodes.
     * @param xbMapping The content builder to use.
     * @param fields The fields to append
     * @throws UnableToLoadConstraints Occurs when the constraints file cannot be loaded.
     */
    private static void setXContentBuilderFields(XContentBuilder xbMapping, Map<String, ElasticsearchFieldMetadata> fields) {

        if(fields.keySet().size() < 1)
            return;

        try {
            xbMapping.startObject(ElasticsearchMapping.OBJECT_PROPERTIES);

            // Iterate over all the annotated fields
            for(String fieldName : fields.keySet()) {

                ElasticsearchFieldMetadata elasticsearchField = fields.get(fieldName);

                xbMapping.startObject(elasticsearchField.getFieldName());

                for(String attribute : elasticsearchField.getAttributes().keySet()) {
                    xbMapping.field(attribute, elasticsearchField.getAttributes().get(attribute));
                }

                setXContentBuilderFields(xbMapping, elasticsearchField.getChildren());

                xbMapping.endObject();
            }

            xbMapping.endObject();

        } catch (IOException e) {
            throw new RuntimeException("");
        }
    }
}