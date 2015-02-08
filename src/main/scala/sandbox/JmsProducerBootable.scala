package sandbox

import javax.jms._

import akka.actor.Actor.Receive
import akka.actor.{Props, ActorLogging, Actor, ActorSystem}
import akka.kernel.Bootable
import org.apache.activemq.ActiveMQConnectionFactory

import scala.util.Random

/**
 * Created by chriswong on 7/2/15.
 */
class JmsProducerBootable extends Bootable {

  val system = ActorSystem("Jms")

  override def startup(): Unit = {
    val producer = system.actorOf(ProducerActor.props, name = "Producer")

    while (true) {
      producer ! ProducerActor.Produce(System.currentTimeMillis())
      Thread.sleep(50)
    }
  }

  override def shutdown(): Unit = {
    system.shutdown()
  }
}

class ProducerActor extends Actor with ActorLogging {

  var jmsConnectionFactory : ActiveMQConnectionFactory = null
  var jmsConnection : Connection = null
  var jmsSession : Session = null

  override def receive: Receive = {
    case ProducerActor.Produce(n) =>
      val queue : Queue = jmsSession.createQueue("TEST")
      val producer: MessageProducer = jmsSession.createProducer(queue)
      producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT)
      val message: TextMessage = jmsSession.createTextMessage(n.toString)
      log.info("Sending {}", message.getText)
      producer.send(message)
  }

  override def preStart(): Unit = {
    jmsConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616")
    jmsConnection = jmsConnectionFactory.createConnection()
    jmsConnection.start()
    jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)
  }

  override def postStop(): Unit = {
    jmsSession.close()
    jmsConnection.close()

  }
}

object ProducerActor {
  lazy val props: Props = Props[ProducerActor]

  case class Produce(n: Long = System.currentTimeMillis())
}