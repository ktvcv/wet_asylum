CREATE OR REPLACE PACKAGE SHELTER AS

--TO-DO: to debug
    FUNCTION createAddress(street varchar, house number, apartment varchar, zip varchar)      RETURN Address_objtyp;
    PROCEDURE createTreatmentType(treatmentTypeName varchar);
    FUNCTION getAllPetsByType(petType varchar)              RETURN PetsList_vartyp;
    FUNCTION getAllAvailablePets                            RETURN PetsList_vartyp;
    FUNCTION getPetById(id number)                          RETURN Pet_objtyp;
    FUNCTION hasPetThisTreatment(petId number, treatmentType varchar)    RETURN number;
    FUNCTION getALLTreatments(petId number)                              RETURN TreatmentList_vartyp;
    PROCEDURE createPet(petName varchar, gender number, typeName varchar, dateOfArrivalInShelter date);
    PROCEDURE addTreatmentToPet(petId number, treatmentName varchar);
    PROCEDURE deletePet(petId number); 
    PROCEDURE setPetType(petId number, petType varchar);
    PROCEDURE setPetName(petId number, newNamePet varchar);
    PROCEDURE setDateOfBirth(petId number, dateOfBirth date);
    PROCEDURE setdateOfArrivalShelter(petId number, dateOfArrivalShelter date);
    PROCEDURE adoptByFamily(petId number, idFamily number);
    PROCEDURE cancelAdoption(petId number);

    PROCEDURE createFamily(familyName varchar, contactName varchar, contactPhone varchar, contactEmail varchar, famAdress Address_objtyp);
    PROCEDURE setFamilyEmail(familyId number, newEmail varchar);
    PROCEDURE setFamilyAddress(familyId number, newAddress Address_objtyp);
    PROCEDURE setFamilyPhone(familyId number, newPhone varchar);
    PROCEDURE deleteFamily(familyId number);
    FUNCTION getAllFamilies                                    RETURN FamilyList_vartyp;
    FUNCTION getFamilyById(id number)                          RETURN Family_objtyp;
    FUNCTION getFamilyByPhone(phone varchar)                   RETURN Family_objtyp;
    FUNCTION getFamilyIdByPhone(phone varchar)                 RETURN number;
END SHELTER;
/

CREATE OR REPLACE PACKAGE BODY SHELTER IS

--type's functions 
 PROCEDURE createTreatmentType(treatmentTypeName varchar) IS
    BEGIN 
        INSERT INTO TreatmentType_objtab
        (treatmentTypeTitle)
        VALUES
        (treatmentTypeName);
    END;

--address's functions
   FUNCTION createAddress(street varchar, house number, apartment varchar, zip varchar) 
    RETURN Address_objtyp IS
    newAddress Address_objtyp;
        BEGIN
            newAddress := Address_objtyp(street, house, apartment, zip);
            
            RETURN newAddress;
        END;

--pet's functions

    PROCEDURE createPet(petName varchar, gender number, typeName varchar, dateOfArrivalInShelter date) IS
        treatmentsList TreatmentList_vartyp := TreatmentList_vartyp();
        BEGIN
            INSERT INTO Pet_objtab
            (name, gender, isTaken, dateOfArrivalShelter, petType, Treatments_List)
            VALUES
            (petName, gender, 0, dateOfArrivalInShelter, petType_objtyp(typeName).petTypeTitle, treatmentsList);
        END;

    FUNCTION getAllPetsByType(petType varchar) return PetsList_vartyp IS
        pet Pet_objtyp;

        pets PetsList_vartyp := PetsList_vartyp();

        CURSOR allPetByType IS
		SELECT  *
		FROM Pet_objtab
        WHERE petType = petType_objtyp(petType).petTypeTitle;

        BEGIN 
		    FOR petRow IN allPetByType LOOP
                
                SELECT VALUE(p) INTO pet
                FROM Pet_objtab p
                WHERE id = petRow.id;

                 pets.extend();
                 pets(pets.count) := pet;
                 pet.display();

            END LOOP;

            RETURN pets;
        END;


    FUNCTION getAllAvailablePets return PetsList_vartyp IS
        pet Pet_objtyp;
        pets PetsList_vartyp := PetsList_vartyp();

        CURSOR allPetByType IS
		SELECT  *
		FROM Pet_objtab
        WHERE isTaken = 0;

        BEGIN 
		    FOR petRow IN allPetByType LOOP
                
                SELECT VALUE(p) INTO pet
                FROM Pet_objtab p
                WHERE id = petRow.id;

                 pets.extend();
                 pets(pets.count) := pet;
                 pet.display();

            END LOOP;

            RETURN pets;
        END;


    FUNCTION getPetById(id number)  RETURN Pet_objtyp IS
        pet Pet_objtyp;
        BEGIN
            BEGIN
            SELECT VALUE(p) INTO pet
                FROM Pet_objtab p
                WHERE p.id = id
                OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY;

            EXCEPTION
             WHEN NO_DATA_FOUND THEN
                 raise_application_error (-20001, 'No such pet ');
            END;
            BEGIN
               RETURN pet;
            END; 
    END;


    PROCEDURE addTreatmentToPet(petId number, treatmentName varchar) IS
        pet Pet_objtyp;
        BEGIN
           pet := getPetById(petId);
           pet.addTreatment(treatmentName, sysdate);
        END; 

    PROCEDURE deletePet(petId number) IS
        pet Pet_objtyp;
        BEGIN
            pet := getPetById(petId);
            pet.deletePet();
        END;

    PROCEDURE adoptByFamily(petId number, idFamily number) IS
        pet Pet_objtyp;
        BEGIN
            pet := getPetById(petId);
            pet.adoptByFamily(idFamily); 
        END; 

    PROCEDURE cancelAdoption(petId number) IS
        pet Pet_objtyp;
        BEGIN
            pet := getPetById(petId);
            pet.cancelAdoption(); 
        END;

     PROCEDURE setPetType(petId number, petType varchar) IS
        pet Pet_objtyp;
        BEGIN
            pet := getPetById(petId);
            pet.setPetType(petType);
        END;    

    PROCEDURE setPetName(petId number, newNamePet varchar) IS
        pet Pet_objtyp;
        BEGIN
            pet := getPetById(petId);
            pet.setName(newNamePet);
        END;
    
    PROCEDURE setDateOfBirth(petId number, dateOfBirth date) IS
        pet Pet_objtyp;
        BEGIN
            pet := getPetById(petId);
            pet.setDateOfBirth(dateOfBirth);
        END; 

    PROCEDURE setdateOfArrivalShelter(petId number, dateOfArrivalShelter date) IS
        pet Pet_objtyp;
        BEGIN
            pet := getPetById(petId);
            pet.setdateOfArrivalShelter(dateOfArrivalShelter);
        END; 

    FUNCTION hasPetThisTreatment(petId number, treatmentType varchar)    RETURN number IS
        pet Pet_objtyp;
        BEGIN 
            pet := getPetById(petId);
            RETURN pet.hasPetThisTreatment(treatmentType);
        END;  

    FUNCTION getALLTreatments(petId number)  RETURN TreatmentList_vartyp  IS
        pet Pet_objtyp;
        BEGIN 
            pet := getPetById(petId);
            RETURN pet.getALLTreatments;
        END;

    -- family's functions
    PROCEDURE createFamily(familyName varchar, contactName varchar, contactPhone varchar, contactEmail varchar, famAdress Address_objtyp) IS
        BEGIN 
            INSERT INTO Family_objtab
            (familyName, contactName,contactEmail,contactPhone, Address_obj)
            VALUES
            (familyName, contactName,contactEmail, contactPhone, famAdress);
        END;

    FUNCTION getFamilyById(id number)  RETURN Family_objtyp IS
        family family_objtyp;
        BEGIN
            BEGIN
            SELECT VALUE(f) INTO family
                FROM Family_objtab f
                WHERE f.id = id
                OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY;

            EXCEPTION
             WHEN NO_DATA_FOUND THEN
                 raise_application_error (-20001, 'No such family ');
            END;

            BEGIN
               RETURN family;
            END; 
    END;

    FUNCTION getFamilyByPhone(phone varchar)  RETURN Family_objtyp IS
        family family_objtyp;
        BEGIN
            BEGIN
            SELECT VALUE(f) INTO family
                FROM Family_objtab f
                WHERE f.contactPhone = phone
                OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY;

            EXCEPTION
             WHEN NO_DATA_FOUND THEN
                 raise_application_error (-20001, 'No such family');
            END;

            BEGIN
               RETURN family;
            END; 
    END;

    FUNCTION getFamilyIdByPhone(phone varchar)  RETURN number IS
    family family_objtyp;
        BEGIN
            BEGIN
            SELECT VALUE(f) INTO family
                FROM Family_objtab f
                WHERE f.contactPhone = phone
                OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY;

            EXCEPTION
             WHEN NO_DATA_FOUND THEN
                 raise_application_error (-20001, 'No such family');
            END;

            BEGIN
               RETURN family.id;
            END; 
    END;  

    FUNCTION getAllFamilies  return FamilyList_vartyp IS
        family Family_objtyp;
        families FamilyList_vartyp := FamilyList_vartyp();

        CURSOR allFamilies IS
		SELECT  *
		FROM Family_objtab;

        BEGIN 
		    FOR familyRow IN allFamilies LOOP
                
                SELECT VALUE(f) INTO family
                FROM Family_objtab f
                WHERE id = familyRow.id;

                 families.extend();
                 families(families.count) := family;
                 family.display();

            END LOOP;

            RETURN families;
        END;

    PROCEDURE deleteFamily(familyId number) IS
        family Family_objtyp;
        BEGIN
            family := getFamilyById(familyId);
            family.deleteFamily();
        END; 

    PROCEDURE setFamilyEmail(familyId number, newEmail varchar) IS
        family Family_objtyp;
        BEGIN
            family := getFamilyById(familyId);
            family.setEmail(newEmail);
        END; 

    PROCEDURE setFamilyAddress(familyId number, newAddress Address_objtyp) IS
        family Family_objtyp;
        BEGIN
            family := getFamilyById(familyId);
            family.setAddress(newAddress);
        END; 

    PROCEDURE setFamilyPhone(familyId number, newPhone varchar) IS
        family Family_objtyp;
        BEGIN
            family := getFamilyById(familyId);
            family.setContactPhone(newPhone);
        END; 
 

END;
/