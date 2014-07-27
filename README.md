airomem
=======

Airomem is Java persistence framework based on [Prevayler(http://prevayler.org/)].
The most important features are:
-speed,
-simplicity.

This is achieved because there is no _Database_. There is only _persistence_.

#Motivation
I hava lot of positive experience with _Prevayler_.

Goal of this project is to provide Prevayler based persistence with some extra features such as Java 8 Lambda expressions,
_Kryo_ based serialization, rich collections (_CQEngine_).


#Basic
Lets assume that system is build without need of persistence. All operations are mode on objects in memory. 
Instead of SQL queries, objects are traversed and filtered.
Instead of making INSERTS, UPDATES, transactions there are only changes on objects in java.
There is no Session, no EntityManager, no cache (because everything is cached...), no problems with ORMs.
No performance penalties, no DB deadlocks. 

Such world exist and is fun to work with. And what is more funny persistence in this world also exists.

There are only two things to do:
 - all objects from domain must be serializable (this is typically easy  to achieve),
 - all operations that change domain must be enclosed in command.
 


#Quick Start

#Typical questions and answers
##SO all the data must fit into RAM? This is huge limitation.
Yes it is indeed so. The point is how  many systems are really affected by this limit.
As for July 2014 one can buy 8GB of RAM for ~ 200$. It is less that one day sallary of a developer...
What can be stored in 8GB of RAM:
 - over 40 milions of user accounts with basic user data (name, lastname, email...),
 - ...
 
If estimations show that data volume is going to exceed 32 GB it may be sensible to consider other options.
##The data is only in RAM. Will it be simply lost after restart, failure?
Absolutely not. Data will be preserved. This is achieved by replying all _transactions_ on last _snapshot_ of system. 




