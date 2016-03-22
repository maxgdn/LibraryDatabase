create table sign_in (
  id                            INTEGER PRIMARY KEY not null,
  created_at                    timestamp,
  student_id                    INTEGER not null,
  time_in                       timestamp,
  time_out                      timestamp,
  was_manual                    INTEGER(1),
  updated_at                    timestamp not null,
  FOREIGN KEY(student_id) REFERENCES student(id)
);

create table student (
  id                            INTEGER PRIMARY KEY not null,
  created_at                    timestamp,
  student_id                    INTEGER,
  first_name                    varchar(255),
  last_name                     varchar(255),
  updated_at                    timestamp not null,
  constraint uq_student_student_id unique (student_id)
);

create index ix_sign_in_student_id on sign_in (student_id);