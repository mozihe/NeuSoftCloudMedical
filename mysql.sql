use nsm_db;

CREATE TABLE patients (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE,
                          contact_info VARCHAR(255),
                          avatar_url VARCHAR(255),
                          gender VARCHAR(10) NOT NULL,
                          is_profile_complete BOOLEAN DEFAULT FALSE,
                          age VARCHAR(3),
                          id_number VARCHAR(20) UNIQUE,
                          created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE departments (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             name VARCHAR(100) NOT NULL,
                             description TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE doctors (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         username VARCHAR(50) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         name VARCHAR(100) NOT NULL,
                         department_id BIGINT,
                         contact_info VARCHAR(255),
                         email VARCHAR(100),
                         introduction TEXT,
                         avatar_url VARCHAR(255),
                         role VARCHAR(20) NOT NULL, -- 用于区分医生、专家、药房医生、管理员
                         verified BOOLEAN DEFAULT FALSE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (department_id) REFERENCES departments(id)
);

