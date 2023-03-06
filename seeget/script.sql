create table users
(
    id          serial
        primary key,
    username    varchar(100) not null,
    password    varchar(100) not null,
    avatar      varchar(100),
    description varchar(100),
    create_time timestamp    not null,
    phone       varchar(100),
    realname    varchar(100),
    id_number   varchar(100),
    city        varchar(100),
    wexin_id    varchar(100),
    qq_id       varchar(100),
    email       varchar(100),
    sex         boolean,
    age         integer,
    education   integer,
    uuid        varchar(50),
    nickname    varchar(100)
);

alter table users
    owner to postgres;

create table role
(
    id      serial
        primary key,
    name    varchar(50) not null,
    user_id integer
        references users
);

alter table role
    owner to postgres;

create table acl_class
(
    id    serial
        primary key,
    class varchar(150)
);

alter table acl_class
    owner to postgres;

create table acl_sid
(
    id        serial
        primary key,
    sid       varchar(100),
    principal boolean
);

alter table acl_sid
    owner to postgres;

create table acl_object_identity
(
    id                 serial
        primary key,
    object_id_class    integer
        references acl_class,
    parent_object      integer
        references acl_object_identity,
    owner_id           integer
        references acl_sid,
    entries_inheriting integer
);

alter table acl_object_identity
    owner to postgres;

create table acl_entry
(
    id                  serial
        primary key,
    acl_object_identity integer
        references acl_object_identity,
    ace_order           integer,
    sid                 integer
        references acl_sid,
    mask                integer,
    granting            boolean,
    audit_success       integer,
    audit_failure       integer
);

alter table acl_entry
    owner to postgres;

create table bucket
(
    id    serial
        primary key,
    name  varchar(100),
    owner integer
        references users
);

alter table bucket
    owner to postgres;

create table article
(
    id          serial
        constraint article_pk
            primary key,
    creator     integer
        constraint article___user
            references users,
    bucket_id   integer
        constraint article___bucket
            references bucket,
    title       varchar(100) not null,
    description varchar(1000),
    create_time timestamp    not null,
    update_time timestamp,
    images      varchar(1000)
);

alter table article
    owner to postgres;

create table article_like_record
(
    id          serial
        constraint article_like_record_pk
            primary key,
    article     integer   not null
        constraint articles_like_record___a
            references article,
    creator     integer   not null
        constraint articles_like_record___u
            references users,
    create_time timestamp not null
);

alter table article_like_record
    owner to postgres;

create table marker
(
    id          integer default nextval('maker_id_seq'::regclass) not null
        constraint maker_pk
            primary key,
    description varchar(2500),
    title       varchar(50),
    height      double precision                                  not null,
    width       double precision                                  not null,
    key         varchar(100)                                      not null,
    create_time timestamp                                         not null,
    update_time timestamp,
    creator     integer                                           not null
        constraint maker___owner
            references users,
    share       boolean default false,
    bucket_id   integer
        constraint marker___bucket
            references bucket
);

alter table marker
    owner to postgres;

create table article_comment_record
(
    id          serial
        constraint article_comment_record_pk
            primary key,
    article     integer
        constraint article_comment_record___article
            references article,
    creator     integer
        constraint article_comment_record___user
            references users,
    content     varchar(200),
    create_time timestamp not null
);

alter table article_comment_record
    owner to postgres;

create table source
(
    id          serial
        constraint source_pk
            primary key,
    creator     integer
        constraint source___owner
            references users,
    bucket_id   integer
        constraint source___bucket
            references bucket,
    key         varchar(200) not null,
    create_time timestamp,
    update_time timestamp
);

alter table source
    owner to postgres;

create table datapackage
(
    id          serial
        constraint datapackage_pk
            primary key,
    create_time timestamp not null,
    update_time timestamp,
    author      integer
        constraint datapackage_users_id_fk
            references users,
    description varchar(500),
    cover       varchar(100),
    thumb       varchar(100),
    location    varchar(100),
    is_public   integer,
    uuid        varchar(100),
    title       varchar(200),
    file_num    integer,
    file_size   double precision
);

alter table datapackage
    owner to postgres;

create table tags
(
    id          serial
        constraint tags_pk
            primary key,
    uuid        varchar(50),
    author      integer      not null
        constraint tags_users_id_fk
            references users,
    type        varchar(100) not null,
    content     varchar(200) not null,
    create_time timestamp    not null,
    update_time timestamp
);

alter table tags
    owner to postgres;

create unique index tags_id_uindex
    on tags (id);

create table datablock
(
    id              serial
        constraint datablock_pk
            primary key,
    type            integer               not null,
    create_time     timestamp             not null,
    fps             integer,
    image_height    integer,
    image_width     integer,
    barometer       boolean default false not null,
    acc             boolean default false not null,
    geo             boolean default false not null,
    gyro            boolean default false not null,
    acc_calib       boolean default false not null,
    geo_calib       boolean default false not null,
    gyro_calib      boolean default false not null,
    deep_map        boolean default false not null,
    pose            boolean default false not null,
    aruco_marker    boolean default false not null,
    upload_status   integer               not null,
    upload_progress varchar(1000),
    minio_key       varchar(100),
    data_package_id integer               not null
        constraint datablock_datapackage_id_fk
            references datapackage,
    owner           integer               not null
        constraint datablock_users_id_fk
            references users,
    gps             boolean default false not null,
    confidence      boolean default false not null,
    uuid            varchar(50)
);

alter table datablock
    owner to postgres;

create unique index datablock_id_uindex
    on datablock (id);

create table datapackage_to_tag
(
    id              serial
        constraint datapackage_to_tag_pk
            primary key,
    data_package_id integer
        constraint datapackage_to_tag_datapackage_id_fk
            references datapackage,
    tag_id          integer
        constraint datapackage_to_tag_tags_id_fk
            references tags
);

alter table datapackage_to_tag
    owner to postgres;

create unique index datapackage_to_tag_id_uindex
    on datapackage_to_tag (id);

create table folders
(
    id          serial
        constraint folders_pk
            primary key,
    author      integer      not null,
    uuid        varchar(50),
    name        varchar(100) not null,
    keys        varchar(1000),
    create_time timestamp    not null,
    update_time timestamp,
    type        varchar(100)
);

alter table folders
    owner to postgres;

create unique index folders_id_uindex
    on folders (id);

create table scene
(
    id                 serial
        constraint scene_pk
            primary key,
    author             integer,
    folder             integer,
    title              varchar(100),
    description        varchar(500),
    cover              varchar(500),
    thumb              varchar(500),
    location           varchar(500),
    file_num           double precision,
    file_size          double precision,
    create_time        timestamp not null,
    update_time        timestamp,
    is_public          integer,
    algorithm          varchar(100),
    algorithm_status   integer,
    start_time         timestamp,
    end_time           timestamp,
    total_time         varchar(200),
    result_info        varchar(1000),
    is_publish         boolean,
    algorithm_progress double precision,
    uuid               varchar(64)
);

alter table scene
    owner to postgres;

create unique index scene_id_uindex
    on scene (id);

create table scene_to_dp
(
    id              serial
        constraint scene_to_dp_pk
            primary key,
    data_package_id integer not null
        constraint scene_to_dp_datapackage_id_fk
            references datapackage,
    scene_id        integer not null
        constraint scene_to_dp_scene_id_fk
            references scene
            on delete cascade
);

alter table scene_to_dp
    owner to postgres;

create unique index scene_to_dp_id_uindex
    on scene_to_dp (id);

create table table_name
(
    id       serial
        constraint table_name_pk
            primary key,
    scene_id integer not null
        constraint table_name_scene_id_fk
            references scene,
    tag_id   integer not null
        constraint table_name_tags_id_fk
            references tags
);

alter table table_name
    owner to postgres;

create unique index table_name_id_uindex
    on table_name (id);

create table scene_to_tag
(
    id       serial
        constraint scene_to_tag_pk
            primary key,
    scene_id integer not null
        constraint scene_to_tag_scene_id_fk
            references scene,
    tag_id   integer not null
        constraint scene_to_tag_tags_id_fk
            references tags
);

alter table scene_to_tag
    owner to postgres;

create unique index scene_to_tag_id_uindex
    on scene_to_tag (id);

create table object
(
    id                 serial
        constraint object_pk
            primary key,
    author             integer   not null
        constraint object_users_id_fk
            references users,
    folder             integer   not null
        constraint object_folders_id_fk
            references folders,
    title              varchar(100),
    descripiton        varchar(500),
    cover              varchar(100),
    thumb              varchar(100),
    location           varchar(100),
    file_num           integer,
    file_size          double precision,
    create_time        timestamp not null,
    update_time        timestamp,
    is_public          integer,
    algorithm          varchar(100),
    algorithm_status   integer,
    start_time         timestamp,
    end_time           timestamp,
    total_time         varchar(100),
    result_info        varchar(1000),
    is_publish         boolean,
    algorithm_progress double precision,
    uuid               varchar(64)
);

alter table object
    owner to postgres;

create unique index object_id_uindex
    on object (id);

create table object_to_dp
(
    id              serial
        constraint object_to_dp_pk
            primary key,
    object_id       integer not null
        constraint object_to_dp_object_id_fk
            references object,
    data_package_id integer not null
        constraint object_to_dp_datapackage_id_fk
            references datapackage
);

alter table object_to_dp
    owner to postgres;

create unique index object_to_dp_id_uindex
    on object_to_dp (id);

create table object_to_tag
(
    id        serial
        constraint object_to_tag_pk
            primary key,
    object_id integer not null
        constraint object_to_tag_object_id_fk
            references object,
    tag_id    integer not null
        constraint object_to_tag___fkt
            references tags
);

alter table object_to_tag
    owner to postgres;

create unique index object_to_tag_id_uindex
    on object_to_tag (id);


