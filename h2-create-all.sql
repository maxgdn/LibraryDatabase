create table sign_in (
  id                            bigint not null,
  created_at                    timestamp,
  student_id                    bigint not null,
  time_in                       timestamp,
  time_out                      timestamp,
  was_manual                    boolean,
  updated_at                    timestamp not null,
  constraint pk_sign_in primary key (id)
);
create sequence sign_in_seq;

create table student (
  id                            bigint not null,
  created_at                    timestamp,
  student_id                    integer,
  first_name                    varchar(255),
  last_name                     varchar(255),
  updated_at                    timestamp not null,
  constraint uq_student_student_id unique (student_id),
  constraint pk_student primary key (id)
);
create sequence student_seq;

alter table sign_in add constraint fk_sign_in_student_id foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_sign_in_student_id on sign_in (student_id);

