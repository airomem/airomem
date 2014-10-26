package pl.setblack.chat.service

import akka.actor.Actor
import spray.routing._
import spray.http._
import spray.json._
import spray.httpx._
import spray.routing._
import MediaTypes._
import spray.json.DefaultJsonProtocol._;
import pl.setblack.chat.Chat;
import pl.setblack.chat.Message;
import pl.setblack.chat.MessageJsonProtocol._
import pl.setblack.chat.MessageListJsonProtocol._
import pl.setblack.airomem.core.SimpleController
import pl.setblack.airomem.core.SimpleController._
import pl.setblack.airomem.core._;
import pl.setblack.scalaport.ScalaIntegration._;
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller

class ChatServiceActor extends Actor with ChatService {
  def actorRefFactory = context

  def receive = runRoute(myRoute)
}

class ChatSupplier extends java.util.function.Supplier[Chat] {
  def get  = { new Chat();} 
}


class AddMessage(m: Message) extends VoidCommand[Chat] with Serializable {
  def executeVoid( c : Chat) = {
    c.addMessage(m.value,m.author);
        
  }
}

trait ChatService extends HttpService {
  val chat : Chat = new Chat();
  val controller : SimpleController[Chat] = loadOptional("chat", 
                                                         new ChatSupplier()
  );
  
 
  chat.addMessage("test","nick");
  val myRoute =
    path("chat") { 
     
      post { 
        entity(as[Message]) { message =>
          respondWithMediaType(`application/json`) {
            complete {
              controller.execute( (s:Chat) => s.addMessage(message.value, message.author)); 
              JsObject("result"->JsString("ok")).prettyPrint;
            }
          }
        }
      } ~ 
      get { 
        respondWithMediaType(`application/json`) {
          complete {
            var messages =controller.query( (c:Chat) => c.getMessages()); 
            messages.toJson.prettyPrint;
          }
        }
      }
      
     
    }

}