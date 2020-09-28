-- connect system/root
-- alter session set “_ORACLE_SCRIPT”=true;
-- ALTER USER guillermo quota unlimited on system;

drop table TreatmentType_objtab cascade constraints;

drop table Pet_objtab cascade constraints;

drop table Family_objtab cascade constraints;

create table TreatmentType_objtab of TreatmentType_objtyp (id primary key) 
object identifier is primary key;

alter table TreatmentType_objtab
ADD CONSTRAINT unique_tr_type_title unique(treatmentTypeTitle);

create table Family_objtab of Family_objtyp
(primary key (id))
object identifier is primary key;

alter table Family_objtab
ADD CONSTRAINT unique_fam_phone unique(contactPhone);

create table Pet_objtab of Pet_objtyp 
(primary key (id),
FOREIGN KEY (FamilyRef) REFERENCES Family_objtab)
object identifier is primary key
nested table Treatments_List store as Pets_Treatments((
    PRIMARY KEY(NESTED_TABLE_ID, id))
    ORGANIZATION INDEX COMPRESS);

alter table  Pet_objtab 
ADD CONSTRAINT petType_notNull check ( petType is not null );