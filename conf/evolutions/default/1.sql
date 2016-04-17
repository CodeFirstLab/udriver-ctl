# Default schema
# --- !Ups

CREATE TABLE images (
    id SERIAL,
    filename varchar(255) NOT NULL,
    binary_data bytea NOT NULL,
    creation_date timestamp NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

CREATE TABLE expenses (
    id SERIAL,
    expense_type varchar(50) NOT NULL,
    expense_date date NOT NULL,
    description varchar(255) NOT NULL,
    quantity numeric NOT NULL,
    price numeric NOT NULL,
    image_id integer REFERENCES images,
    creation_date timestamp NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE expenses;

DROP TABLE images;