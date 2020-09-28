-- drop trigger treatmentType_on_insert;
-- drop trigger PetType_objtab_on_insert;
-- drop trigger FamilyType_objtab_on_insert;

--trigger on treatmentType inserting
CREATE OR REPLACE TRIGGER treatmentType_on_insert
    BEFORE INSERT ON TreatmentType_objtab
    FOR EACH ROW
        BEGIN 
            SELECT treatmentTypeTab_id_seq.nextval
            INTO :new.id
            FROM dual;
        END;
/

--trigger on Pet_objtab inserting
CREATE OR REPLACE TRIGGER PetType_objtab_on_insert
    BEFORE INSERT ON  Pet_objtab
    FOR EACH ROW
        BEGIN 
            SELECT petTab_id_seq.nextval
            INTO :new.id
            FROM dual;
        END;
/

--trigger on familyTab_id_seq inserting
CREATE OR REPLACE TRIGGER FamilyType_objtab_on_insert
    BEFORE INSERT ON Family_objtab
    FOR EACH ROW
        BEGIN 
            SELECT familyTab_id_seq.nextval
            INTO :new.id
            FROM dual;
        END;
/