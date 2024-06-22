CREATE TABLE products
(
    id                  serial primary key,
    uuid                varchar        not null,
    activate            boolean        not null DEFAULT FALSE,
    product_name        varchar        not null,
    main_desc           TEXT           not null,
    desc_html           TEXT           not null,
    price               decimal(12, 2) not null,
    image_urls          varchar[]      not null,
    created_at           DATE,
    category_parameters integer REFERENCES "category_parameters" (id)
)
