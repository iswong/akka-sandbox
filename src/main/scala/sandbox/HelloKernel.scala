package sandbox

import akka.actor.Actor.Receive
import akka.actor.{Props, ActorLogging, Actor, ActorSystem}
import akka.kernel.Bootable
import com.typesafe.config.ConfigFactory

/**
 * Created by chriswong on 31/1/15.
 */
class HelloKernel extends Bootable {

  val config = ConfigFactory.load()

  implicit val system = ActorSystem()

  override def startup(): Unit = {
    system.actorOf(Props[WorkerActor] withRouter())
  }

  override def shutdown(): Unit = {
    system.shutdown()
  }
}

case class Request(x: Int)
case class Result(y: Int)

class WorkerActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case Request(r) => 
      sender ! Result(r)
  }
}