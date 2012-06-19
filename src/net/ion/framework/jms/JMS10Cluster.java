package net.ion.framework.jms;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

import net.ion.framework.logging.LogBroker;

public class JMS10Cluster extends Cluster {
	private final static Logger log = LogBroker.getLogger(JMS10Cluster.class);

	/**
	 * The name of this jms. Used to identify the sender of a message.
	 */
	private String clusterNode;

	/**
     */
	private TopicConnection connection;

	/**
	 * Th object used to publish new messages
	 */
	private TopicPublisher publisher;

	/**
	 * The current JMS session
	 */
	private TopicSession publisherSession;

	private static String createUniqueNodeName() {
		String hostAddress;
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			hostAddress = "127.0.0.1";
		}
		return hostAddress + ":" + System.currentTimeMillis();
	}

	public JMS10Cluster(String topic, String topicFactory) {
		this(createUniqueNodeName(), topic, topicFactory, null);
	}

	public JMS10Cluster(String topic, String topicFactory, Properties props) {
		this(createUniqueNodeName(), topic, topicFactory, props);
	}

	public JMS10Cluster(String nodeName, String topic, String topicFactory) {
		this(nodeName, topic, topicFactory, null);
	}

	public JMS10Cluster(String nodeName, String topic, String topicFactory, Properties props) {
		// Get the name of this node
		clusterNode = nodeName;

		try {
			// Make sure you have specified the necessary JNDI properties (usually in
			// a jndi.properties resource file, or as system properties)
			InitialContext jndi = new InitialContext(props);

			// Look up a JMS connection factory
			TopicConnectionFactory connectionFactory = (TopicConnectionFactory) jndi.lookup(topicFactory);

			// Create a JMS connection
			connection = connectionFactory.createTopicConnection();

			// Create session objects
			publisherSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

			TopicSession subSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

			// Look up the JMS topic
			Topic chatTopic = (Topic) jndi.lookup(topic);

			// Create the publisher and subscriber
			publisher = publisherSession.createPublisher(chatTopic);

			TopicSubscriber subscriber = subSession.createSubscriber(chatTopic);

			// Set the message listener
			subscriber.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					try {
						// check the message type
						ObjectMessage objectMessage = null;

						if (!(message instanceof ObjectMessage)) {
							log.warning("Cannot handle message of type (class=" + message.getClass().getName() + "). Notification ignored.");
							return;
						}

						objectMessage = (ObjectMessage) message;

						// check the message content
						if (!(objectMessage.getObject() instanceof ClusterMessage)) {
							System.out.println("An unknown cluster notification message received (class=" + objectMessage.getObject().getClass().getName()
									+ "). Notification ignored.");
							return;
						}

						// This prevents the notification sent by this node from being handled by itself
						if (!objectMessage.getStringProperty("nodeName").equals(clusterNode)) {
							// now handle the message
							dispatchClusterEvent(new ClusterEvent(JMS10Cluster.this, (ClusterMessage) objectMessage.getObject()));
						}
					} catch (JMSException jmsEx) {
						log.warning("Cannot handle jms Notification" + jmsEx);
					}
				}
			});

			// Start the JMS connection; allows messages to be delivered
			connection.start();
		} catch (Exception e) {
			throw new RuntimeException("Initialization of the JMS10Cluster failed: " + e);
		}
	}

	/**
	 * Called by the cache administrator class when a cache is destroyed.
	 * 
	 * @throws FinalizationException
	 *             thrown when there was a problem finalizing the listener. The cache administrator will catch and log this error.
	 */
	public void finialize() {
		try {
			connection.close();
		} catch (JMSException e) {
			// log.warn("A problem was encountered when closing the JMS connection", e);
		}
	}

	public void sendMessage(ClusterMessage message) {
		try {
			ObjectMessage objectMessage = publisherSession.createObjectMessage();
			objectMessage.setObject(message);

			// sign the message, with the name of this node
			objectMessage.setStringProperty("nodeName", clusterNode);
			publisher.publish(objectMessage);
		} catch (JMSException e) {
			// log.warning("Cannot send notification " + message, e);
		}
	}

	public void sendMessageAll(ClusterMessage message) {
		try {
			ObjectMessage objectMessage = publisherSession.createObjectMessage();
			objectMessage.setObject(message);

			// sign the message, with the name of this node
			objectMessage.setStringProperty("nodeName", "");
			publisher.publish(objectMessage);
		} catch (JMSException e) {
			// log.warning("Cannot send notification " + message, e);
		}
	}
}
