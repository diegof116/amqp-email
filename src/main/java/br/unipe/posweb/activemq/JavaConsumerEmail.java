package br.unipe.posweb.activemq;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;

import br.unipe.posweb.modelo.Email;

public class JavaConsumerEmail implements Runnable {

	private String REMETENTE = "Fila";
	
	@Override
	public void run() {
		try {
			ActiveMQConnectionFactory factory = 
					new ActiveMQConnectionFactory("tcp://localhost:61616");
			factory.setTrustAllPackages(true);
			
			//Cria a conexão com ActiveMQ
			Connection connection = factory.createConnection();
			
			// Inicia a conexão
			connection.start();

			// Cria a sessão
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			do{
				System.out.println("\nDigite o Email que deseja Ler a Caixa de Entrada:");
				Scanner scanner = new Scanner(System.in);  
				
				REMETENTE = scanner.nextLine();  
				
				scanner.close();

				//Cria a fila e informa qual o destinatário.
				Destination queue = session.createQueue(REMETENTE);            
			
				MessageConsumer consumer = session.createConsumer(queue);
				
				Message message = consumer.receive();
				
				if(message == null){
					System.out.println("Não Existem Mensagens");
					break;
				}
				
				if (message instanceof ObjectMessage) {
					
					ObjectMessage objectMessage = (ObjectMessage) message;
					
					Email email = (Email) objectMessage.getObject();
					
					Gson gson = new Gson();
					
					String json = gson.toJson(email);
					
					System.out.println(json.toString());
					
				}
				
			} while (true);
			
			session.close();
			connection.close();
		}
		catch (Exception ex) {
			System.out.println("Exception Occured");
			ex.printStackTrace();
		}
	}
}
