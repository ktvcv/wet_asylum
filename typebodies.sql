--Pet type body
CREATE OR REPLACE TYPE BODY PetType_objtyp AS 
    CONSTRUCTOR FUNCTION petType_objtyp( petTypeTitle varchar)
        RETURN SELF AS RESULT IS
            BEGIN
                IF LOWER(TRIM(petTypeTitle)) IN ('dog', 'cat')
                    THEN 
                        SELF.petTypeTitle := LOWER(TRIM(petTypeTitle));
                ELSE
                    RAISE_APPLICATION_ERROR(-20999,'Unknown type "' || LOWER(TRIM(petTypeTitle)) || '"');
                END IF;
        RETURN;
    END;

END;
/

--Address type body--
CREATE OR REPLACE TYPE BODY Address_objtyp AS
    MEMBER PROCEDURE display IS
        BEGIN
            DBMS_OUTPUT.PUT_LINE ('address: street: ' || self.street || ', house: '||
            self.house || ', apartment: ' || self.apartment  ||
            ', zip: ' || self.zip) ;
        END;


END;
/

--Family type body--
CREATE OR REPLACE TYPE BODY Family_objtyp AS
    MEMBER PROCEDURE display IS
        BEGIN
            DBMS_OUTPUT.PUT_LINE ('id: ' || self.id || ', family name: '||
            self.familyName || ', contact name: ' || self.contactName ||
            ', email: ' || self.contactEmail || ', phone: ' || self.contactPhone);
            self.Address_obj.display;
        END;

    MEMBER PROCEDURE setContactPhone(newPhone varchar) IS
        BEGIN 
            UPDATE Family_objtab 
            SET contactPhone = newPhone
            WHERE id = SELF.id;
        END;

    MEMBER PROCEDURE setEmail(newEmail varchar) IS
        BEGIN 
            UPDATE Family_objtab 
            SET contactEmail = newEmail
            WHERE id = SELF.id;
        END;

    MEMBER PROCEDURE setAddress(newAddress Address_objtyp) IS
        BEGIN 
            UPDATE Family_objtab 
            SET Address_obj = newAddress
            WHERE id = SELF.id;
        END;  

    MEMBER PROCEDURE deleteFamily IS
        BEGIN 
            DELETE FROM Family_objtab 
            WHERE id = SELF.id;
        END;  
END;
/

CREATE OR REPLACE TYPE BODY Treatment_objtyp AS
    MEMBER PROCEDURE display IS
        BEGIN
            DBMS_OUTPUT.PUT_LINE ('id: ' || self.id || ', date: '||
            self.treatmentDate || ', type: ' || self.treatmentType);
            
        END;
END;        
/

CREATE OR REPLACE TYPE BODY Pet_objtyp AS 
    -- add treatment to the pet
    MEMBER PROCEDURE addTreatment(treatmentType varchar, dateOfTr date) IS
        treatmentType_title VARCHAR(200);
        NULL_TABLE EXCEPTION;
        PRAGMA EXCEPTION_INIT (NULL_TABLE, -22908);
    BEGIN
        BEGIN
            SELECT t.treatmentTypeTitle INTO 
            treatmentType_title 
            FROM TreatmentType_objtab t
            WHERE t.treatmentTypeTitle = treatmentType;
    
        EXCEPTION
             WHEN NO_DATA_FOUND THEN
                 raise_application_error (-20001, 'No such treatment ' || treatmentType ||
                 ' check treatments type with getAllTypeTreatments function');
    
        END;
        BEGIN
        INSERT INTO TABLE (
            SELECT p.Treatments_List
            FROM Pet_objtab p
            WHERE p.id = self.id
        )
        SELECT treatmentTab_id_seq.nextval, dateOfTr, t.treatmentTypeTitle
        FROM TreatmentType_objtab t
        WHERE t.treatmentTypeTitle = LOWER(treatmentType);

        EXCEPTION
                WHEN NULL_TABLE THEN
                UPDATE Pet_objtab SET Treatments_List = TreatmentList_vartyp()
                     WHERE id = self.id;
                INSERT INTO TABLE (
                    SELECT p.Treatments_List
                    FROM Pet_objtab p
                    WHERE p.id = self.id)
                SELECT treatmentTab_id_seq.nextval, dateOfTr, 
                    t.treatmentTypeTitle
                    FROM TreatmentType_objtab t
                    WHERE t.treatmentTypeTitle = LOWER(treatmentType);
        
        END;      
    END;
    MEMBER PROCEDURE DISPLAY IS
        BEGIN
            DBMS_OUTPUT.PUT_LINE('Pet: ' || self.id ||', name: ' || self.name ||
            ', type: ' || self.petType || ', gender:' || self.gender || 'is taken?: ' || self.isTaken);
        END;


    MEMBER FUNCTION getALLTreatments return TreatmentList_vartyp IS
        BEGIN
            RETURN self.Treatments_List; 
        END;    

    MEMBER PROCEDURE cancelAdoption IS
    BEGIN
            UPDATE Pet_objtab 
            SET isTaken = 0,
                FamilyRef = NULL
            WHERE id = self.id;
    END;          

    MEMBER PROCEDURE adoptByFamily(idFamily number) IS
        familyRef_obj ref Family_objtyp;
        BEGIN
            SELECT REF (f) INTO familyRef_obj 
            FROM Family_objTab f
            WHERE id = idFamily;

            UPDATE Pet_objtab
            SET isTaken = 1,
                FamilyRef = familyRef_obj 
            WHERE id = self.id;

        END;   

     MEMBER FUNCTION hasPetThisTreatment(treatmentType varchar) return number IS
        countNum number;
        i INTEGER;
        BEGIN
            countNum := 0;
               FOR i in 1..SELF.Treatments_List.COUNT LOOP
               if(self.Treatments_List(i).treatmentType = LOWER(treatmentType)) then
                    countNum := countNum + 1;
                END IF;    
            END LOOP; 

            IF (countNum > 0) then
                RETURN 1;
            ELSE RETURN 0;
            END If;

        END; 

    MEMBER PROCEDURE setDateOfDep(dateOfDep date) IS
         BEGIN 
            UPDATE Pet_objtab 
            SET dateOfDeparture = dateOfDep
            WHERE id = SELF.id;
        END; 

    MEMBER PROCEDURE setName(newName1 varchar) IS
        BEGIN 
            UPDATE Pet_objtab
            SET name = newName1
            WHERE id = SELF.id;
        END; 

     MEMBER PROCEDURE setDateOfBirth(dateOfBirth date) IS
        BEGIN 
            UPDATE Pet_objtab 
            SET dateOfBirth = dateOfBirth
            WHERE id = SELF.id;
        END;

    MEMBER PROCEDURE setdateOfArrivalShelter(dateOfArrivalShelter date) IS
        BEGIN 
            UPDATE Pet_objtab 
            SET dateOfArrivalShelter = dateOfArrivalShelter
            WHERE id = SELF.id;
        END;

    MEMBER PROCEDURE setPetType(petTypepet varchar) IS
        BEGIN 
            UPDATE Pet_objtab 
            SET petType = petType_objtyp(petTypepet).petTypeTitle
            WHERE id = SELF.id;
        END; 

    MEMBER PROCEDURE deletePet IS 
        BEGIN
            DELETE FROM Pet_objtab 
            WHERE id = SELF.id;
        END;     
END;
/