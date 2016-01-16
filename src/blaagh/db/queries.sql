-- name: get-names
-- Just gets a simple list of the names from table foo.
select name 
from foo;

-- name: create-table-foo!
-- creates a test table 'foo'
CREATE TABLE IF NOT EXISTS foo (name text);

-- name: create-table-bar!
-- creates a test table 'bar'
CREATE TABLE IF NOT EXISTS bar (name text);

-- name: create-table-baz!
-- creates a test table 'baz'
CREATE TABLE IF NOT EXISTS baz (name text);

-- name: populate-table-foo!
-- populates the test table 'foo'
INSERT INTO foo (name) 
VALUES  ('Rick'),
        ('Carol'),
        ('Daryl'),
        ('Michonne'),
        ('Glenn'),
        ('Maggie'),
        ('Carl'),
        ('Sasha'),
        ('Abraham'),
        ('Morgan');

-- name: create-table-posts!
-- Sets up our table for blog posts
CREATE TABLE IF NOT EXISTS posts (
    id int PRIMARY KEY, 
    dt datetime DEFAULT CURRENT_TIMESTAMP NOT NULL, 
    live BOOLEAN NOT NULL DEFAULT (0), 
    title TEXT NOT NULL DEFAULT (''), 
    content TEXT NOT NULL DEFAULT ('')
);
