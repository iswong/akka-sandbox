package sandbox

import javax.jms._

import akka.actor._
import akka.kernel.Bootable
import akka.routing.RoundRobinPool
import org.apache.activemq.ActiveMQConnectionFactory
import org.slf4j.LoggerFactory

import scala.util.Random

/**
 * Created by chriswong on 7/2/15.
 */
class JmsConsumerBootable extends Bootable {

  val log = LoggerFactory.getLogger(classOf[JmsConsumerBootable])

  val system = ActorSystem("Jms")

  var jmsConnectionFactory: ActiveMQConnectionFactory = null
  var jmsConnection: Connection = null
  var jmsSession: Session = null


  override def startup(): Unit = {

    jmsConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616")
    jmsConnection = jmsConnectionFactory.createConnection()
    jmsConnection.start()
    jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)

    val consumer: ActorRef = system.actorOf(ConsumerActor.propsWithRoundRobin(3), name = "Consumer")

    while (true) {
      val queue: Queue = jmsSession.createQueue("TEST")
      val jmsConsumer: MessageConsumer = jmsSession.createConsumer(queue)
      val message: Message = jmsConsumer.receive()
      message match {
        case t: TextMessage =>
          consumer ! ConsumerActor.Consume(t.getText)
        case m@_ =>
          log.warn("Receiving unknown message {}", m)
      }
      jmsConsumer.close()
    }
  }

  override def shutdown(): Unit = {
    jmsSession.close()
    jmsConnection.close()

    system.shutdown()
  }
}

class ConsumerActor extends Actor with ActorLogging {


  override def receive: Receive = {
    case ConsumerActor.Consume(p) =>
      log.info("Receiving {} in {}ms", p, (System.currentTimeMillis - p.toLong))
    case n @ _ =>
      log.info("Receiving unknown {}", n)
  }

  override def preStart(): Unit = {
  }

  override def postStop(): Unit = {

  }
}

object ConsumerActor {
  lazy val props: Props = Props[ConsumerActor]
  def propsWithRoundRobin(nrOfInstances: Int): Props = props.withRouter(RoundRobinPool(nrOfInstances))

  case class Consume(msg : String)
}

