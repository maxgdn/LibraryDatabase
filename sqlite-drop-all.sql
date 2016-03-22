alter table sign_in drop constraint if exists fk_sign_in_student_id;
drop index if exists ix_sign_in_student_id;

drop table if exists sign_in;

drop table if exists student;

