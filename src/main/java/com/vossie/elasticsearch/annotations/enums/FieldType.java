package com.vossie.elasticsearch.annotations.enums;

/**
 * Copyright © 2013 Carel Vosloo.
 * User: cvosloo
 * Date: 10/12/2013
 * Time: 14:30
 */
public enum FieldType {

    STRING {
        @Override
        public String toString() {
            return "string";
        }
    },

    TOKEN_COUNT {
        @Override
        public String toString() {
            return "token_count";
        }
    },

    DATE {
        @Override
        public String toString() {
            return "date";
        }
    },

    BOOLEAN {
        @Override
        public String toString() {
            return "boolean";
        }
    },

    BINARY {
        @Override
        public String toString() {
            return "binary";
        }
    },

    OBJECT {
        @Override
        public String toString() {
            return "object";
        }
    },

    NESTED {
        @Override
        public String toString() {
            return "nested";
        }
    },

    MULTI_FIELD {
        @Override
        public String toString() {
            return "multi_field";
        }
    },

    IP {
        @Override
        public String toString() {
            return "ip";
        }
    },

    ATTACHMENT {
        @Override
        public String toString() {
            return "attachment";
        }
    },

    // <<<<<<< Shape types >>>>>>>

    GEO_POINT {
        @Override
        public String toString() {
            return "geo_point";
        }
    },

    GEO_SHAPE {
        @Override
        public String toString() {
            return "geo_shape";
        }
    },

    // <<<<<<< Number types >>>>>>>

    FLOAT {
        @Override
        public String toString() {
            return "float";
        }
    },

    DOUBLE {
        @Override
        public String toString() {
            return "double";
        }
    },

    INTEGER {
        @Override
        public String toString() {
            return "integer";
        }
    },

    LONG {
        @Override
        public String toString() {
            return "long";
        }
    },

    SHORT {
        @Override
        public String toString() {
            return "short";
        }
    },

    BYTE {
        @Override
        public String toString() {
            return "byte";
        }
    }
}
