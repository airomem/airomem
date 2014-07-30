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


## How do we controll the domain?
See [ChatControllerImpl](https://github.com/jarekratajski/airomem/blob/master/airomem-chatsample/airomem-chatsample-data/src/main/java/pl/setblack/airomem/chatsample/execute/ChatControllerImpl.java).

```
@ApplicationScoped
public class ChatControllerImpl implements ChatController {

    private PersistenceController<DataRoot<ChatView, Chat>, ChatView> controller;

    @PostConstruct
    void initController() {
        final PersistenceFactory factory = new PersistenceFactory();
        controller = factory.initOptional("chat", () -> new DataRoot<>(new Chat()));
    }

    @Override
    public List<MessageView> getRecentMessages() {
        return controller.query((view) -> view.getRecentMessages());
    }

    public void addMessage(String nick, String message) {
        controller.execute((chat, ctx) -> {
            chat.getDataObject().addMessage(nick, message, LocalDateTime.ofInstant(ctx.time, ZoneId.systemDefault()));
        });
    }
}
```
* In *initController* persistence is initialized (_controller_ object is like _PersistenceContext_ in JEE),
* In *getRecentMessages* last messages are queried to display them in a Page.
* In *addMessage*  new message ( from web) is stored,
```
(chat, ctx) -> {
            chat.getDataObject().addMessage(nick, message, LocalDateTime.ofInstant(ctx.time, ZoneId.systemDefault()));
        }
```
This is the lmbda that changes domain states and this lambda is preserved (in so called _Journal_).

In case system goes down unexpectedly  _Journal_ Lambdas are restored and reapplied to model. Thats is why... last argument of *addMessages* which should be current date
looks so (instead of *LocalDateTime.now()*;). This one of few things to remember.


## So how does Chat class look like?
... it is just java object.
```
public class Chat implements ChatView, Serializable {

    private CopyOnWriteArrayList<Message> messages;

    public Chat() {
        this.messages = new CopyOnWriteArrayList<>();
    }

    public void addMessage(String nick, String content, LocalDateTime time) {
        assert WriteChecker.hasPrevalanceContext();
        final Author author = new Author(nick);
        final Message msg = new Message(author, content, time);
        this.messages.add(msg);

    }

    @Override
    public List<MessageView> getRecentMessages() {
        int count = this.messages.size();
        final List<MessageView> res = this.messages.stream().skip(Math.max(count - 10, 0))
                .limit(10)
                .collect(Collectors.toList());
        return res;
    }
}
```




And that is all...

# Performance
It is very unfair to compare Prevalance with regular Databases. If there are network overhead, ORM, JDBC taken into account... 
RAM and CPU nowadays are so fast that naive iterating over milion of Strings in memory and filtering using *.contains("mystr")* may be faster than
quering INDEXED Table using *SQL LIKE* :-). (I am sure there is a limit, ORM and database and data kind for which statement is not True anymore).

No matter how unfair is that I've invested some time to run some benchark called [jpab](http://www.jpab.org/). (Made to compare ORMs).
I run this on 8GB RAM  PC Intel i7 -3370 3.40 Ghz. 

## I run Hibernate ORM with mysql (not tuned!).

Starting Hibernate-MySQL-server -> IndexTest
IndexTest(thread=1, batch=5000) results:
Persist: 2 839
Disk Space: 0MB
Retrieve: 118 245
Query: 46,9
Update: 3 290
Remove: 3 633
Completed in 190 seconds.

Numbers are measured operations per second.

## Then I've adopted code for airomem/prevayler persistence :

Starting Prevayler-Prevayler-server -> IndexTest
IndexTest(thread=1, batch=5000) results:
Persist: 10 759
Disk Space: 0MB
Retrieve: 46 881 103
Query: 202
Update: 33 463 809
Remove: 12 359
Completed in 194 seconds.

Some operations are 5 times, some 10 000 times faster ( I am further investigaing benchmark to find where the problem is).





#Typical questions and answers
##SO all the data must fit into RAM? Seems huge limitation.
Yes it is indeed so. The point is how  many systems are really affected by this limit.
As for July 2014 one can buy 8GB of RAM for ~ 200$. It is less that one day sallary of a developer...
What can be stored in 8GB of RAM:
 - over 40 milions of user accounts with basic user data (name, lastname, email...),
 - info about monthly income for 100 Years for 1000000 people
 
If estimations show that data volume is going to exceed 32 GB it may be sensible to consider other options.
##The data is only in RAM. Will it be simply lost after restart, failure?
Absolutely not. Data will be preserved. This is achieved by replying all _transactions_ on last _snapshot_ of system. 




