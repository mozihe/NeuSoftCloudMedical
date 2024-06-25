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
                         id_number VARCHAR(20) UNIQUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE applications (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              doctor_id BIGINT NOT NULL,
                              name VARCHAR(100) NOT NULL,
                              applied_role VARCHAR(20) NOT NULL,
                              application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              status VARCHAR(20) DEFAULT 'ING',
                              FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE registrations (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               patient_id BIGINT NOT NULL,
                               doctor_id BIGINT NOT NULL,
                               department_id BIGINT NOT NULL,
                               medical_record_number VARCHAR(25) NOT NULL UNIQUE, -- 病历号
                               reason TEXT, -- 挂号原因
                               is_appointment BOOLEAN DEFAULT FALSE, -- 是否预约
                               appointment_id BIGINT, -- 预约号，可为空
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (patient_id) REFERENCES patients(id),
                               FOREIGN KEY (doctor_id) REFERENCES doctors(id),
                               FOREIGN KEY (department_id) REFERENCES departments(id),
                               FOREIGN KEY (appointment_id) REFERENCES appointments(id) -- 关联到预约表，可为空
);


CREATE TABLE appointments (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              patient_id BIGINT NOT NULL,
                              doctor_id BIGINT NOT NULL,
                              department_id BIGINT NOT NULL,
                              appointment_date DATETIME NOT NULL,
                              status VARCHAR(20) DEFAULT 'PENDING', -- 预约状态
                              reason TEXT, -- 预约原因
                              is_registered BOOLEAN DEFAULT FALSE, -- 是否已经挂号
                              medical_record_number VARCHAR(25),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (patient_id) REFERENCES patients(id),
                              FOREIGN KEY (doctor_id) REFERENCES doctors(id),
                              FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE medications (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             name VARCHAR(255) NOT NULL,
                             price DECIMAL(10, 2) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE diagnostic_reports (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    patient_id BIGINT NOT NULL,
                                    doctor_id BIGINT NOT NULL,
                                    medical_record_number VARCHAR(25) NOT NULL, -- 添加病历号字段
                                    diagnosis TEXT NOT NULL,
                                    reason TEXT, -- 添加挂号原因字段
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    FOREIGN KEY (patient_id) REFERENCES patients(id),
                                    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);
-- 创建药物处方表
CREATE TABLE prescriptions (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               diagnostic_report_id BIGINT NOT NULL,
                               medication_id BIGINT NOT NULL,
                               medication_name VARCHAR(255) NOT NULL,
                               dosage VARCHAR(255) NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (diagnostic_report_id) REFERENCES diagnostic_reports(id),
                               FOREIGN KEY (medication_id) REFERENCES medications(id)
);
