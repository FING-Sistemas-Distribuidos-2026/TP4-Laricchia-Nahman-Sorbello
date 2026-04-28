package org.example;

/**
 * Subscriber.java — ZeroMQ Pub/Sub  |  CLIENTE (Java / JeroMQ)
 * TP4 - Sistemas Distribuidos Laricchia - Nahman - Sorbello
 */

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Subscriber {

    private static final String TOPIC = "noticias";
    private static final int    PORT  = 5556;

    public static void main(String[] args) {

        String host = (args.length > 0) ? args[0] : "localhost";
        String address = "tcp://" + host + ":" + PORT;

        System.out.println("[Subscriber Java] Conectando a " + address);
        System.out.println("[Subscriber Java] Suscrito al tópico: '" + TOPIC + "'");
        System.out.println("Esperando mensajes... (Ctrl+C para detener)\n");

        try (ZContext context = new ZContext()) {

            ZMQ.Socket socket = context.createSocket(SocketType.SUB);

            // Suscribirse al tópico
            socket.subscribe(TOPIC.getBytes(ZMQ.CHARSET));
            socket.connect(address);

            while (!Thread.currentThread().isInterrupted()) {
                // recv() bloquea hasta recibir un mensaje
                String mensaje = socket.recvStr(0);

                // El mensaje llega como "<TOPIC> <contenido>"
                // Separamos el tópico del contenido para mostrarlo prolijo
                if (mensaje != null) {
                    String[] partes   = mensaje.split(" ", 2);
                    String topico     = partes[0];
                    String contenido  = partes.length > 1 ? partes[1] : "";
                    System.out.println("[SUB] Tópico: " + topico + " | " + contenido);
                }
            }
        } catch (Exception e) {
            System.out.println("[Subscriber] Detenido: " + e.getMessage());
        }
    }
}