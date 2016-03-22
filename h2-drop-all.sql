alter table sign_in drop constraint if exists fk_sign_in_student_id;
drop index if exists ix_sign_in_student_id;

drop table if exists sign_in;
drop sequence if exists sign_in_seq;

drop table if exists student;
drop sequence if exists student_seq;

