
CREATE TABLE public.pc
(
    LINE_ID integer NOT NULL,
    STATION_ID integer NOT NULL,
    PC_ID integer NOT NULL,
    PC_NAME character(40)  NOT NULL,
    PC_STATUS integer NOT NULL,
    PC_PLACE integer NOT NULL,
    PC_PRODUCER integer NOT NULL,
    PC_MODEL integer NOT NULL,
    PC_IP character(40)  NOT NULL,
    PC_ACCOUNT character(40)  NOT NULL,
    PC_PASSWORD character(40)   NOT NULL,
    CONSTRAINT pc_pkey PRIMARY KEY (LINE_ID, STATION_ID, PC_ID)
);

CREATE TABLE public.line
(
    LINE_ID integer NOT NULL,
    LINE_NAME character(40)  NOT NULL,
    LINE_STATUS integer NOT NULL,
    LINE_INF character(40)  NOT NULL,
    CONSTRAINT line_pkey PRIMARY KEY (LINE_ID)
);

CREATE TABLE public.producerconfig
(
    PRODUCT_TYPE integer NOT NULL,
    PRODUCER integer NOT NULL,
    MODEL integer NOT NULL,
    LANG integer NOT NULL,
    PRODUCER_NAME character(40)  NOT NULL,
    MODEL_NAME character(40)  NOT NULL,
    CONSTRAINT producerconfig_pkey PRIMARY KEY (PRODUCT_TYPE, PRODUCER, MODEL, LANG, PRODUCER_NAME, MODEL_NAME)
);

CREATE TABLE public.station
(
    LINE_ID integer NOT NULL,
    STATION_ID integer NOT NULL,
    SITE_NUMBER integer NOT NULL,
    X_POS integer NOT NULL,
    Y_POS integer NOT NULL,
    STATION_NAME character(40)  NOT NULL,
    STATION_STATUS integer NOT NULL,
    STATION_INF character(40)  NOT NULL,
    CONSTRAINT station_pkey PRIMARY KEY (LINE_ID, STATION_ID)
);

CREATE TABLE public.station_workstation
(
    LINE_ID integer NOT NULL,
    STATION_ID integer NOT NULL,
    STATION_SERVER_ID integer NOT NULL,
    WS_ID integer NOT NULL,
    STATION_WS_NAME character(40) NOT NULL,
    STATION_WS_NUMBER integer NOT NULL,
    STATION_WS_ACCOUNT character(40) NOT NULL,
    STATION_WS_PASSWORD character(40) NOT NULL,
    CONSTRAINT station_workstation_pkey PRIMARY KEY (LINE_ID, STATION_ID, STATION_SERVER_ID, WS_ID)
);

CREATE TABLE public.stationserver
(
    LINE_ID integer NOT NULL,
    STATION_ID integer NOT NULL,
    STATION_SERVER_ID integer NOT NULL,
    STATION_SERVER_NAME character(40) NOT NULL,
    STATION_SERVER_STATUS integer NOT NULL,
    STATION_SERVER_IS_USED integer NOT NULL,
    STATION_SERVER_PRODUCER integer NOT NULL,
    STATION_SERVER_MODEL integer NOT NULL,
    STATION_SERVER_IP character(40)  NOT NULL,
    STATION_SERVER_ACCOUNT character(40) NOT NULL,
    STATION_SERVER_PASSWORD character(40) NOT NULL,
    CONSTRAINT stationserver_pkey PRIMARY KEY (LINE_ID, STATION_ID, STATION_SERVER_ID)
);

CREATE TABLE public.stationtemplate
(
    TEMPLATE_ID integer NOT NULL,
    TEMPLATE_NAME character(40) NOT NULL,
    STATION_SERVER_IS_USED integer NOT NULL,
    STATION_SERVER_NAME character(40)  NOT NULL,
    STATION_SERVER_PRODUCER integer NOT NULL,
    STATION_SERVER_MODEL integer NOT NULL,
    STATION_WS_NUMBER integer NOT NULL,
    STATION_WS_FAMILY_NAME character(40)  NOT NULL,
    PC1_NAME character(40)  NOT NULL,
    PC1_PLACE integer NOT NULL,
    PC1_PRODUCER integer NOT NULL,
    PC1_MODEL integer NOT NULL,
    PC1_SCREEN_NUMBER integer NOT NULL,
    PC1_SCREEN_FAMILY_NAME character(40)  NOT NULL,
    PC1_SCREEN_PRODUCER integer NOT NULL,
    PC1_SCREEN_MODEL integer NOT NULL,
    PC2_NAME character(40)  NOT NULL,
    PC2_PLACE integer NOT NULL,
    PC2_PRODUCER integer NOT NULL,
    PC2_MODEL integer NOT NULL,
    PC2_SCREEN_NUMBER integer NOT NULL,
    PC2_SCREEN_FAMILY_NAME character(40)  NOT NULL,
    PC2_SCREEN_PRODUCER integer NOT NULL,
    PC2_SCREEN_MODEL integer NOT NULL,
    PC3_NAME character(40)  NOT NULL,
    PC3_PLACE integer NOT NULL,
    PC3_PRODUCER integer NOT NULL,
    PC3_MODEL integer NOT NULL,
    PC3_SCREEN_NUMBER integer NOT NULL,
    PC3_SCREEN_FAMILY_NAME character(40)  NOT NULL,
    PC3_SCREEN_PRODUCER integer NOT NULL,
    PC3_SCREEN_MODEL integer NOT NULL,
    CONSTRAINT stationtemplate_pkey PRIMARY KEY (TEMPLATE_ID)
);

CREATE TABLE public.screen
(
    LINE_ID integer NOT NULL,
    STATION_ID integer NOT NULL,
    PC_ID integer NOT NULL,
    SCREEN_ID integer NOT NULL,
    SCREEN_NAME character varying NOT NULL,
    SCREEN_STATUS integer NOT NULL,
    SCREEN_PRODUCER integer NOT NULL,
    SCREEN_MODEL integer NOT NULL,
    CONSTRAINT screen_pkey PRIMARY KEY (LINE_ID, STATION_ID, PC_ID, SCREEN_ID)
);

CREATE TABLE public.resource
(
    id1 integer NOT NULL,
    id2 integer NOT NULL,
    id3 integer NOT NULL,
    id4 integer NOT NULL,
    id5 integer NOT NULL,
    resource_type integer NOT NULL,
    resource_name character varying(40) NOT NULL,
    CONSTRAINT source_pkey PRIMARY KEY (id1, id2, id3, id4, id5, resource_type)
);