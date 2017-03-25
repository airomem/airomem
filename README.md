airomem
=======
[![Build Status](https://travis-ci.org/airomem/airomem.svg?branch=master)](https://travis-ci.org/airomem/airomem)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6c932d5fca3c4936aaaa8fab6a84a8e4)](https://www.codacy.com/app/jarekratajski/airomem?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=airomem/airomem&amp;utm_campaign=Badge_Grade)

Airomem is Java persistence framework based on [Prevayler](http://prevayler.org/).
The most important features are:
- speed,
- simplicity.

This is achieved because there is no _Database_. There is only _persistence_.

#Motivation

I have a lot of positive experience with _Prevayler_ - quite an old project published by Klaus Wuestefeld.
Pure Prevayler lacks however some features  and is not perfectly safe for inexperienced teams.

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
**New!**
[Galakpizza Sample](https://github.com/airomem/galakpizza)

#Performance - this thing is insane
This thing must come. How fast is that? Can be more than 1000 times faster than traditional SQL / ORM approach...

I've tried to measure speed in operations per second using [jpab](http://www.jpab.org/) project.
Results are [here](https://github.com/jarekratajski/airomem/wiki/JPAB-Benchmark).

In reality one can expect to achive performance of 10 up to 1000 faster compared to DB/ORM approach.

#More
Please read more on:
[Wiki](https://github.com/jarekratajski/airomem/wiki)

# Release  notes
## Version 1.1.1
Changed to use file  sync always. 
It is slower but safer on some environments. 

## Version 1.1.0
- Simplified - no longer use Storable interface with getImmutable
- Use *Path* in API instead of String name  of folder 
- Transient version for tests
- xml snapshot (import and export) 

### Migration from 1.0.5
 - No more SimpleController (what a stupid name it was :-) ?) - class is called *Persistent*
 - *Path* instead  of Strings as folder names 

## Version 1.0.5
.