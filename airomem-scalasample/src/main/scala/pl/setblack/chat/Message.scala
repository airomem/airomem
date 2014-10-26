package pl.setblack.chat;
import spray.json.DefaultJsonProtocol;
import spray.json._;


/**
 * Simple class representing message.
 */
class Message(val value: String, val author: String) extends Serializable {
    
}


object MessageJsonProtocol extends DefaultJsonProtocol {
  implicit object MessageJsonFormat extends RootJsonFormat[Message] {
    def write (m: Message) = {
      JsArray( JsString(m.value), JsString(m.author));
    }
    def read (value:JsValue) = value.asJsObject.getFields("value", "author") match {
      case Seq(JsString(value), JsString(author)) => 
        new Message(value, author);
      case _ => deserializationError("what is this?");
    };
  }
}

object MessageListJsonProtocol extends DefaultJsonProtocol {
  implicit object MessageListJsonFormat extends RootJsonFormat[List[Message]] {
    def write (l: List[Message]) = {
      
       l.map(m => JsObject( "value" -> JsString(m.value), "author" -> JsString(m.author)) ).toList.toJson;
    }
    
    def read (value:JsValue) = ???;
  }

}