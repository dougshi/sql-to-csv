DROP TABLE IF EXISTS PROJECTS;
  
CREATE TABLE PROJECTS (
  name VARCHAR(250) NOT NULL PRIMARY KEY,
  description VARCHAR(512) NOT NULL,
  created_date timestamp with time zone DEFAULT current_timestamp
);