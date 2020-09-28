drop sequence treatmentTypeTab_id_seq;
drop sequence petTab_id_seq;
drop sequence treatmentTab_id_seq;
drop sequence familyTab_id_seq;

CREATE SEQUENCE treatmentTypeTab_id_seq
    INCREMENT BY 1
    START WITH 1
    MAXVALUE 5000;

CREATE SEQUENCE petTab_id_seq
    INCREMENT BY 1
    START WITH 1
    MAXVALUE 5000;

CREATE SEQUENCE treatmentTab_id_seq
    INCREMENT BY 1
    START WITH 1
    MAXVALUE 5000;

CREATE SEQUENCE familyTab_id_seq
    INCREMENT BY 1
    START WITH 1
    MAXVALUE 5000;