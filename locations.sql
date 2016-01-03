-- Table: locations

-- DROP TABLE locations;

CREATE TABLE locations
(
  location_id text NOT NULL,
  business_account_number bigint,
  ownership_name text,
  dba_name text,
  street_address text,
  zip_code integer,
  business_start_date date,
  business_end_date date,
  location_start_date date,
  location_end_date date,
  class_code integer,
  pbc_code integer,
  latitude double precision,
  longitude double precision,
  CONSTRAINT locations_pk PRIMARY KEY (location_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE locations
  OWNER TO postgresql;
