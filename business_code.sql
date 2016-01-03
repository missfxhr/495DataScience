-- Table: business_code

-- DROP TABLE business_code;

CREATE TABLE business_code
(
  ownership_type character(1) NOT NULL,
  business_major_class integer NOT NULL,
  business_minor_class integer NOT NULL,
  description text,
  CONSTRAINT business_code_pk PRIMARY KEY (ownership_type, business_minor_class, business_major_class)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE business_code
  OWNER TO postgresql;
