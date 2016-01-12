-- name: get-names
-- Just gets a simple list of the names from table foo.
select name 
from foo;

-- name: create-table-foo!
-- creates a test table 'foo'
CREATE TABLE foo (name text);

-- name: create-table-bar!
-- creates a test table 'bar'
CREATE TABLE bar (name text);

-- name: create-table-baz!
-- creates a test table 'baz'
CREATE TABLE baz (name text);
