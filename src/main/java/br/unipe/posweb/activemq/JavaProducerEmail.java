package br.unipe.posweb.activemq;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import br.unipe.posweb.modelo.Email;

public class JavaProducerEmail implements Runnable {

	private static String REMETENTE = "fujioka@empresa.com.br"; 
	private static String DESTINATARIO = "joao@empresa.com.br"; 
	private static String MENSAGEM = "Uma mensagem de integração enviada"; 
	private static String MENSAGEM_REMETENTE = "Mensagem enviada com sucesso"; 
	private static String MENSAGEM_DESTINATARIO = "Há uma mensagem aguardando leitura"; 

	public void run() {
		try { // Create a connection factory.

			Email mensagemFila = new Email();

			mensagemFila.setRemetente(REMETENTE);
			mensagemFila.setDestinatario(DESTINATARIO);
			mensagemFila.setMensagem(MENSAGEM);
			mensagemFila.setDataHoraEnvio(new Date());

			ActiveMQConnectionFactory factory = 
					new ActiveMQConnectionFactory("tcp://localhost:61616");
			factory.setTrustAllPackages(true);

			//Create connection.
			Connection connection = factory.createConnection();

			// Start the connection
			connection.start();

			// Create a session which is non transactional
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Create Destination queue
			Destination queue = session.createQueue("Fila");

			// Create a producer
			MessageProducer producer = session.createProducer(queue);

			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");

			mensagemFila.setDataHoraEnvio(new Date());

			// insert message
			ObjectMessage message = session.createObjectMessage(mensagemFila);

			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String dataHora = sd.format(mensagemFila.getDataHoraEnvio());

			System.out.println(
					"Remetente: " + mensagemFila.getRemetente() 
					+ " - Destinatário: " +	mensagemFila.getDestinatario() 
					+ " - Mensagem: " +	mensagemFila.getMensagem()
					+ " - Data de Envio: " + dataHora);

			producer.send(message);

			//--------------------------------------------------------------------------------
			
			//Email para o Remetente

			// Create Destination queue
			Destination queueEnvio = session.createQueue(REMETENTE);

			// Create a producer
			MessageProducer producerEnvio = session.createProducer(queueEnvio);

			producerEnvio.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");

			Email mensagemRemetente = new Email();
			mensagemRemetente.setMensagem(MENSAGEM_REMETENTE);

			// insert message
			ObjectMessage message2 = session.createObjectMessage(mensagemRemetente);

			producerEnvio.send(message2);
			
			System.out.println(
					REMETENTE
					+ " - Mensagem: " + mensagemRemetente.getMensagem());

			//--------------------------------------------------------------------------------
			
			//Email para o Destinatário

			// Create Destination queue
			Destination queueDestinarario = session.createQueue(DESTINATARIO);

			// Create a producer
			MessageProducer producerDestino = session.createProducer(queueDestinarario);

			producerDestino.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");

			Email mensagemDestinatario = new Email();
			mensagemDestinatario.setMensagem(MENSAGEM_DESTINATARIO);

			// insert message
			ObjectMessage message3 = session.createObjectMessage(mensagemDestinatario);

			producerDestino.send(message3);

			System.out.println(
					DESTINATARIO
					+ " - Mensagem: " + mensagemDestinatario.getMensagem());
			
			session.close();
			connection.close();
		}
		catch (Exception ex) {
			System.out.println("Exception Occured");
		}
	}
}
