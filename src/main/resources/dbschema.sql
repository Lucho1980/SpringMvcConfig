DROP TABLE IF EXISTS AUTHORITIES;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS BLOG_POST;

--USER TABLE
CREATE TABLE IF NOT EXISTS USER (
ID BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
USERNAME VARCHAR(255) NOT NULL,
PASSWORD VARCHAR(255) NOT NULL,
ENABLED BOOLEAN NOT NULL,
PRIMARY KEY(ID)
);

--AUTHORITIES TABLE
CREATE TABLE IF NOT EXISTS AUTHORITIES (
ID BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
USERNAME VARCHAR(255) NOT NULL,
AUTHORITY VARCHAR(255) NOT NULL,
);

--BLOG TABLE
CREATE TABLE IF NOT EXISTS BLOG_POST (
ID BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
TITLE VARCHAR(255) NOT NULL,
CONTENT VARCHAR(4000) NOT NULL,
DRAFT BOOLEAN NULL,
PUBLISHDATE DATE NULL,
USER_ID BIGINT NOT NULL,
CONSTRAINT FK_BLOGPOST_USER FOREIGN KEY(USER_ID) REFERENCES USER(ID)
);