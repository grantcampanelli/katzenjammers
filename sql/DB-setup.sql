create table Specialties(
    parent_id INT,
    specialty_id INT PRIMARY KEY,
    title VARCHAR(100),
    code VARCHAR(15),
    website VARCHAR(100)
);

create table Source(
    source_id INT PRIMARY KEY,
    type VARCHAR(3), -- ORG or IND
    name_prefix VARCHAR(20),
    name VARCHAR(50),
    name_suffix VARCHAR(20),
    medical_credential VARCHAR(20),
    gender CHAR(1),
    dob DATE,
    is_sole_proprieter CHAR(1),
    phone VARCHAR(15),
    primary_specialty INT,
    secondary_specialty INT,
    FOREIGN KEY(primary_specialty) REFERENCES Specialties(specialty_id),
    FOREIGN KEY(secondary_specialty) REFERENCES Specialties(specialty_id)
);

create table Address(
    source_id INT,
    type CHAR(4), -- MAIL or PRAC
    street VARCHAR(50),
    unit VARCHAR(10),
    city VARCHAR(100),
    region VARCHAR(50),
    zip_code VARCHAR(15),
    county VARCHAR(50),
    country VARCHAR(50),
    FOREIGN KEY(source_id) REFERENCES Source(source_id),
    PRIMARY KEY(source_id,type)
);

create table Master(
    master_id INT PRIMARY KEY,
    type VARCHAR(12),
    name VARCHAR(100),
    gender CHAR(1),
    dob DATE,
    is_sole_proprieter BOOLEAN
);

create table Mastered_Phones(
    master_id INT,
    phone INT,
    PRIMARY KEY(master_id, phone),
    FOREIGN KEY(master_id) REFERENCES Master(master_id)
);

create table Mastered_Address(
    master_id INT,
    source_id INT,
    type CHAR(4),
    FOREIGN KEY(master_id) REFERENCES Master(master_id),
    FOREIGN KEY(source_id,type) REFERENCES Address(source_id,type),
    PRIMARY KEY(master_id, source_id, type)
);

create table Mastered_Specialties(
    master_id INT,
    specialty_id INT,
    type INT,
    FOREIGN KEY(master_id) REFERENCES Master(master_id),
    FOREIGN KEY(specialty_id) REFERENCES Specialties(specialty_id),
    PRIMARY KEY(master_id, specialty_id)
);

create table Rules(
    rule_id INT PRIMARY KEY,
    name VARCHAR(20),
    description VARCHAR(100)
);

create table Audit(
    source_id INT,
    master_id INT,
    rule_id INT,
    comment VARCHAR(100),
    FOREIGN KEY(source_id) REFERENCES Source(source_id),
    FOREIGN KEY(master_id) REFERENCES Master(master_id),
    FOREIGN KEY(rule_id) REFERENCES Rules(rule_id),
    PRIMARY KEY(source_id, master_id)
);