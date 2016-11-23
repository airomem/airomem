airomem
=======
[![Build Status](https://travis-ci.org/airomem/airomem.svg?branch=master)](https://travis-ci.org/airomem/airomem)


Airomem is Java persistence framework based on [Prevayler](http://prevayler.org/).
The most important features are:
- speed,
- simplicity.

This is achieved because there is no _Database_. There is only _persistence_.

#Motivation

I hava lot of positive experience with _Prevayler_ - quite an old project published by Klaus Wuestefeld.
Pure Prevayler lacks however some features  and is not perfectly safe for unexperienced teams.

Goal of this project is to provide Prevayler based persistence with extra features such as Java 8 Lambda expressions,
_Kryo_ based serialization, rich collections (_CQEngine_) and sanity checks that make Prevayler easier to implement.
(_Quite honestly Prevayler is perfectly easy to implement, it just does not always work as people expect_).

So airomem =  _Prevayler_ | safety | tools | lambdas | kryo | build...

Take a look at [Features](https://github.com/jarekratajski/airomem/wiki/Features).

#Basic
Lets assume that system is build without need of persistence. All operations are mode on objects in memory. 
Instead of SQL queries, objects are traversed and filtered using stream API.

Instead of making INSERTS, UPDATES, transactions, there are only changes on objects in java.
There is no Session, no EntityManager, no cache ( oh,wait a moment - everything is cache...), no problems with ORMs,
no performance penalties, no DB deadlocks.

Such world exist and is fun to work with. And what is more funny persistence in this world also exists.

There are only two things to do:
 - all objects from domain must be serializable (this is typically easy  to achieve),
 - all operations that change domain must be enclosed in [commands](http://en.wikipedia.org/wiki/Command_pattern). 
  
 
#How simple is that? (Sample Project)
[Chat Sample](https://github.com/jarekratajski/airomem/wiki/Chat-Sample)

#Performance - this thing is insane
This thing must come. How fast is that? Can be more than 1000 times faster than traditional SQL / ORM approach...

I've tried to measure spped in operations per second using [jpab](http://www.jpab.org/) project.
Results are [here](https://github.com/jarekratajski/airomem/wiki/JPAB-Benchmark).

In reality one can expect to achive performance of 10 up to 1000 faster compared to DB/ORM approach.

#More
Please read more on:
[Wiki](https://github.com/jarekratajski/airomem/wiki)



