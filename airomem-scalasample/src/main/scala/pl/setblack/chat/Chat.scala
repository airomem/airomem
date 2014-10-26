package pl.setblack.chat;

import java.util.concurrent.CopyOnWriteArrayList;
import scala.collection.JavaConverters._;


/**
 * Root domain object.
 */
class Chat extends Serializable {
  var messages : CopyOnWriteArrayList[Message] = new CopyOnWriteArrayList[Message]();
  
  def addMessage( text : String, nick : String) {
     var message : Message = new Message(text, nick);
     messages.add(message);
  }
  
  def getMessages() :List[Message]= {
    var result = messages.asScala.toList;
    return result;
  }
}
