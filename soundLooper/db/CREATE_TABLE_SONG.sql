create table song
(
id int generated by default as identity,  
file varchar (255),
lastuse timestamp,
isfavorite int
);