package sandbox

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.kernel.Bootable
import akka.routing.SmallestMailboxPool
import com.typesafe.config.ConfigFactory

/**
 * Created by chriswong on 31/1/15.
 */
class HelloKernel extends Bootable {

  val config = ConfigFactory.load()

  implicit val system = ActorSystem()

  override def startup(): Unit = {
    val worker = system.actorOf(Props[WorkerActor]
      withRouter(SmallestMailboxPool(nrOfInstances = 4)))
    worker ! Request(9)
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
      log.info("Hihi")
  }
}