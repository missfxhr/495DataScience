-- Table: locations

-- DROP TABLE locations;

CREATE TABLE incidents
(
  IncidntNum text,
  Category text,
  Descript text,
  DayOfWeek text,
  incident_date date,
  incident_time time,
  PdDistrict text,
  Resolution text,
  Address text,
  latitude double precision,
  longitude double precision,
  PdId bigint not null,
  CONSTRAINT incidents_pk PRIMARY KEY (PdId)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE incidents
  OWNER TO postgresql;

