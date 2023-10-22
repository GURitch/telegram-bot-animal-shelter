--liquibase formatted sql
-- changeset amargolina:2
CREATE TABLE animal_shelter (
                          id SERIAL primary key,
                          name TEXT,
                          country   TEXT,
                          city TEXT,
                          address TEXT,
                          working_hours TEXT,
                          pass_rules    TEXT,
                          rules TEXT,
                          animal_type   TEXT
);