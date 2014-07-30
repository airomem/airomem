airomem
=======

Airomem is Java persistence framework based on [Prevayler](http://prevayler.org/).
The most important features are:
- speed,
- simplicity.

This is achieved because there is no _Database_. There is only _persistence_.

#Motivation
I hava lot of positive experience with _Prevayler_ - quite an old project published by Klaus Wuestefeld.

Goal of this project is to provide Prevayler based persistence with some extra features such as Java 8 Lambda expressions,
_Kryo_ based serialization, rich collections (_CQEngine_).


#Basic
Lets assume that system is build without need of persistence. All operations are mode on objects in memory. 
Instead of SQL queries, objects are traversed and filtered using stream API.

Instead of making INSERTS, UPDATES, transactions there are only changes on objects in java.
There is no Session, no EntityManager, no cache (because everything is cached...), no problems with ORMs.
No performance penalties, no DB deadlocks. 

Such world exist and is fun to work with. And what is more funny persistence in this world also exists.

There are only two things to do:
 - all objects from domain must be serializable (this is typically easy  to achieve),
 - all operations that change domain must be enclosed in [commands](http://en.wikipedia.org/wiki/Command_pattern). 
 
#How simple is that?
Tak a look airomem-chatsample. Simple chat system (web based) using airomem. Besides JEE7.
## Domain
Domain consists of three classes:
- [Chat](https://github.com/jarekratajski/airomem/blob/master/airomem-chatsample/airomem-chatsample-data/src/main/java/pl/setblack/airomem/chatsample/data/Chat.java), 
- [Message](https://github.com/jarekratajski/airomem/blob/master/airomem-chatsample/airomem-chatsample-data/src/main/java/pl/setblack/airomem/chatsample/data/Message.java), 
- [Author](https://github.com/jarekratajski/airomem/blob/master/airomem-chatsample/airomem-chatsample-data/src/main/java/pl/setblack/airomem/chatsample/data/Chat.java)


## How to store new messsage
See [ChatControllerImpl](https://github.com/jarekratajski/airomem/blob/master/airomem-chatsample/airomem-chatsample-data/src/main/java/pl/setblack/airomem/chatsample/execute/ChatControllerImpl.java).

```
private PersistenceController<DataRoot<ChatView, Chat>, ChatView> controller;

    @PostConstruct
    void initController() {
        final PersistenceFactory factory = new PersistenceFactory();
        controller = factory.initOptional("chat", () -> new DataRoot<>(new Chat()));
    }

    public ChatView getChatView() {
        return controller.query((view) -> view);
    }

    public void addMessage(String nick, String message) {
        controller.execute((chat, ctx) -> {
            chat.getDataObject().addMessage(nick, message, LocalDateTime.ofInstant(ctx.time, ZoneId.systemDefault()));
        });
    }
```	



And that is all...

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




