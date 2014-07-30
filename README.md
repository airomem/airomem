airomem
=======

Airomem is Java persistence framework based on [Prevayler](http://prevayler.org/).
The most important features are:
- speed,
- simplicity.

This is achieved because there is no _Database_. There is only _persistence_.

#Motivation

I hava lot of positive experience with _Prevayler_ - quite an old project published by Klaus Wuestefeld.
Pure Prevayler lacks however some features and is not perfectly safe for unexperienced teams.

Goal of this project is to provide Prevayler based persistence with extra features such as Java 8 Lambda expressions,
_Kryo_ based serialization, rich collections (_CQEngine_) and sanity checks that make Prevayler easier to implement.
(_Quite honestly Prevayler is perfectly easy to implement, it just does not always work as people expect_).

So airomem =  _Prevayler_ | safety | tools | lambdas | kryo | build...
Take a look at Features.

#Basic
Lets assume that system is build without need of persistence. All operations are mode on objects in memory. 
Instead of SQL queries, objects are traversed and filtered using stream API.

Instead of making INSERTS, UPDATES, transactions there are only changes on objects in java.
There is no Session, no EntityManager, no cache (stop -> everything is cache...), no problems with ORMs.
No performance penalties, no DB deadlocks. 

Such world exist and is fun to work with. And what is more funny persistence in this world also exists.

There are only two things to do:
 - all objects from domain must be serializable (this is typically easy  to achieve),
 - all operations that change domain must be enclosed in [commands](http://en.wikipedia.org/wiki/Command_pattern). 
 
 
#How simple is that? (Sample Project)
Tak a look airomem-chatsample. This is simple chat system (web based) build on airomem. Besides this is JEE7 project.
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

#Performance - this thing is insane
This thing must come. How fast is that. Answer is - insane.
I've tried to measure spped in operations per second using [jpab](http://www.jpab.org/) project.
The problem is I was getting often int overflows... 

Anyhow after some adaptations, corrections results are here.

In really one can expect to achive performance of 10 up to 1000 faster than DB/ORM approach.

#Applications
There are already projects using prevayler  out there.
I've run game for WEB developers once [CSSCraft](http://csscraft.setblack.pl) (this is runing for montths, meanwhile I've corrected few errors in game, adapted rules...
and never lost scores (that are persisted using _Prevayler_)).

#When not to use prevayler?
- When data volume is expected to be far bigger than 30GB.
- When there are expectations to generate ad hoc reports from database.
- When team is unexperienced in Java. Especially does not want to hear about _synchronized_ keyword.

Industry that should avoid prevayler: Banking.

#When to use prevayler/airomem
- Data volume is expected to be < 10 GB,
- Performance, responsiveness of application is important,
- Team loves Java ad is experienced in it,
- Team loves DDD, OOD,
 
Industry that can love Prevayler:
- Game,
- Social media,
- E-commerce,



#Features
| Feature | Comment | State | Estimation |  
|---------|---------|-------|------------|
| Rumtime Write Check | Provide simple check for WRITE context. | DONE | Day | 
| Sample | Provide sample system | DONE | Day |
| Safe Build | Provide maven artifact for building Prevayler based system, defensive | NOT DONE | Week | 




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




