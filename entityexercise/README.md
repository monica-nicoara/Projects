A Java project that implements a simple REST API to:
- list Entity entries
- send an Entity (to root, or attached to another entity)
- read an Entity

It uses Gradle to build and run; Vert.X and QBit to implement the server + logic; JSON payload format.
No persistence, no TLS required and it is single-host.

To start the project, execute the following command in a terminal:

gradlew run

By default, there are 2 entities added: one root entity (with id 1), which has one subentity (with id 2).

For testing purposes, I used Postman.

You should now be able to see them with the following command:

http://localhost:8888/v1/entity-service/list (GET)

In order to add a new entity with parent id 2, I used the following command:

http://localhost:8888/v1/entity-service/add (POST)
and in the body:  {"parentId": "2", "data": {"newKey": "newValue"}}

A successful message will be dislayed:
"Entity with id 3 was added."

In order to add a new root entity, I used the following command (I considered an entity as root if his parent id is "null"):

http://localhost:8888/v1/entity-service/add (POST) 
and in the body: {"parentId": "null", "data": {"newRootKey": "newRootValue"}}

In this case, the old root will be added as a subentity of the new root.

In order to retrieve an existing entity from the database, I used the following command:

http://localhost:8888/v1/entity-service/get?id=3 (GET)

If I want to retrieve an unexisting entity (the id is not valid), an error message is displayed. This is the case also when I try to add a new entity with an unexisting parent id. 