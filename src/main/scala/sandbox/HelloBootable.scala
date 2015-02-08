package sandbox

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.kernel.Bootable
import akka.routing.SmallestMailboxPool
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
 * Created by chriswong on 31/1/15.
 */
class HelloBootable extends Bootable {

  val config = ConfigFactory.load()
  val log = LoggerFactory.getLogger(classOf[HelloBootable])

  implicit val system = ActorSystem()

  override def startup(): Unit = {
    log.info("Starting up")
    val worker = system.actorOf(Props[WorkerActor]
      withRouter(SmallestMailboxPool(nrOfInstances = 4)), name = "Worker")
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
      log.error(new RuntimeException("Test"), "Screw up")
  }
}