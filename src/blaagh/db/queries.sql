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
    id INTEGER PRIMARY KEY AUTOINCREMENT, 
    dt datetime DEFAULT CURRENT_TIMESTAMP NOT NULL, 
    live BOOLEAN NOT NULL DEFAULT (0), 
    slug TEXT NOT NULL DEFAULT (''),
    title TEXT NOT NULL DEFAULT (''), 
    content TEXT NOT NULL DEFAULT ('')
);

-- name: populate-table-posts1!
-- Sets up a test record in the post table
INSERT INTO posts (slug, title, content)
VALUES (
    'test-post-01',
    'TEST POST 01',
    '<h2>This is test post 01</h2>'
);

-- name: populate-table-posts2!
-- Sets up a test record in the post table
INSERT INTO posts (slug, title, content)
VALUES (
    'test-post-02',
    'TEST POST 02',
    '<h2>This is test post 02</h2>'
);

-- name: get-post-by-slug
-- Fetches a single post by slug
SELECT id, dt, live, slug, title, content
FROM posts
WHERE slug = :slug

-- name: write-new-post!
-- Writes a new post to the DB
INSERT INTO posts (
    slug, title, content
)
VALUES (
    :slug, :title, :content
);

-- name: admin-get-posts
-- Fetches a list of posts
SELECT id, dt, live, slug, title, content
FROM posts;
