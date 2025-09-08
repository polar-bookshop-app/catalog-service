ALTER TABLE book
    ADD COLUMN publisher varchar(255);

UPDATE book
SET publisher = 'Oreilly'
WHERE publisher IS NULL;

ALTER TABLE book
    ALTER COLUMN publisher SET NOT NULL;
