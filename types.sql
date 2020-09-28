drop type Address_objtyp force;

drop type Treatment_objtyp force;

drop type TreatmentType_objtyp force;

drop type TreatmentList_vartyp force;

drop type PetsList_vartyp force;

drop type PetType_objtyp force;

drop type Family_objtyp force;

drop type Pet_objtyp force;

create or replace type Address_objtyp as object (
    street    varchar(500),
    house     number,
    apartment varchar(500),
    zip       varchar(500),
    MEMBER PROCEDURE display
);
/

create or replace type TreatmentType_objtyp as object (
    id number,
    treatmentTypeTitle varchar(500)
);
/

create or replace type PetType_objtyp as object (
    petTypeTitle varchar(500),
    CONSTRUCTOR FUNCTION petType_objtyp( petTypeTitle varchar)
    RETURN SELF AS RESULT
);
/

create or replace type Treatment_objtyp  as object (
    id number,
    treatmentDate date,
    treatmentType varchar(500),
    MEMBER PROCEDURE display
);
/

create or replace type TreatmentList_vartyp  as table of Treatment_objtyp;
/

create or replace type Family_objtyp  as object (
    id number,
    familyName varchar(500),
    contactName varchar(500),
    contactEmail varchar(500),
    contactPhone varchar(500),
    Address_obj Address_objtyp,
    MEMBER PROCEDURE display,
    MEMBER PROCEDURE setContactPhone(newPhone varchar),
    MEMBER PROCEDURE setEmail(newEmail varchar),
    MEMBER PROCEDURE setAddress(newAddress Address_objtyp),
    MEMBER PROCEDURE deleteFamily
);
/

create or replace type FamilyList_vartyp as table of Family_objtyp;
/

create or replace type Pet_objtyp  as object (
    id number,
    name varchar(500),
    dateOfBirth date,
    gender NUMBER(1,0),
    isTaken NUMBER(1,0),
    dateOfArrivalShelter date,
    petType varchar(200),
    Treatments_List TreatmentList_vartyp,
    dateOfDeparture date,
    FamilyRef REF Family_objtyp,
    MEMBER FUNCTION hasPetThisTreatment(treatmentType varchar)    return number,
    MEMBER FUNCTION getALLTreatments                              return TreatmentList_vartyp,
    MEMBER PROCEDURE adoptByFamily(idFamily number),
    MEMBER PROCEDURE cancelAdoption,
    MEMBER PROCEDURE DISPLAY,
    MEMBER PROCEDURE addTreatment(treatmentType varchar, dateOfTr date),
    MEMBER PROCEDURE setDateOfDep(dateOfDep date),
    MEMBER PROCEDURE setName(newName1 varchar),
    MEMBER PROCEDURE setDateOfBirth(dateOfBirth date),
    MEMBER PROCEDURE setdateOfArrivalShelter(dateOfArrivalShelter date),
    MEMBER PROCEDURE setPetType(petTypepet varchar),
    MEMBER PROCEDURE deletePet
);
/

create or replace type PetsList_vartyp  as table of Pet_objtyp;
/
