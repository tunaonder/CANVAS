DROP TABLE IF EXISTS UserFile, SimulationModel, Project, User;

CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR (32) NOT NULL,
    password VARCHAR (32) NOT NULL,
    first_name VARCHAR (32) NOT NULL,
    last_name VARCHAR (32) NOT NULL,
    email VARCHAR (128) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE UserFile
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       filename VARCHAR (256) NOT NULL,
       user_id INT UNSIGNED,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Project
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       projectname VARCHAR (256) NOT NULL,
       mapname VARCHAR (256) NOT NULL,
       publicProject BIT NOT NULL,
       user_id INT UNSIGNED,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE SimulationModel
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       modeldata VARCHAR (10000) NOT NULL,
       project_id INT UNSIGNED,
       FOREIGN KEY (project_id) REFERENCES Project(id) ON DELETE CASCADE
);
