create table users
(
    id              varchar(50)                           not null
        primary key,
    password        varchar(255)                          not null,
    email           varchar(100)                          not null,
    employee_number varchar(50)                           null,
    name            varchar(100)                          not null,
    department      varchar(100)                          null,
    position        varchar(100)                          null,
    profile_picture varchar(255)                          null,
    is_approved     tinyint(1)  default 0                 null,
    created_at      timestamp   default CURRENT_TIMESTAMP null,
    updated_at      timestamp   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    auth            varchar(50) default 'ROLE_MEMBER'     not null,
    constraint email
        unique (email),
    constraint employee_number
        unique (employee_number)
);

drop table users