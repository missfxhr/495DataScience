-- Table: parking_places

-- DROP TABLE parking_places;

CREATE TABLE parking_places
(
  "Owner" text,
  "Address" text,
  "PrimeType" text,
  "SecondType" text,
  "GarOrLot" text,
  "RegCap" bigint,
  "ValetCap" bigint,
  "MCCap" bigint,
  "LANDUSETYP" text,
  "Location 1" text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parking_places
  OWNER TO postgresql;
