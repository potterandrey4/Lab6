package org.example;

import org.example.collection.classes.Worker;
import org.example.messages.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Stream;

import static org.example.RequestReader.clientAddress;
import static org.example.ServerConnect.serverChannel;

public class ResponseSender {

    // максимальный размер одного пакета
    private static final int FRAGMENT_SIZE = 65507;
    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);

    static {
        Stream.of(1, 2, 3, 4).mapToInt(x -> x).map((int x) -> {
            System.out.println(x);
            return x;
        });
    }

    static {
        try {
            try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 final ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                final Worker a = new Worker();
                final Worker b = new Worker();
                a.workers = new LinkedList<>(Collections.singletonList(b));
                b.workers = new LinkedList<>(Collections.singletonList(a));
                oos.writeObject(a);

                final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                final ObjectInputStream ois = new ObjectInputStream(bais);
                final Worker x = (Worker) ois.readObject();
                final Worker y = x.workers.get(0);

                System.out.println(x == y.workers.get(0));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void sendCommand(String string) {
        try {
            Msg response = new Msg(string);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            oos.flush();

            byte[] responseData = baos.toByteArray();   // большой массив байтов

            // Разделение данных на фрагменты
            List<byte[]> fragments = new ArrayList<>();
            for (int i = 0; i < responseData.length; i += FRAGMENT_SIZE) {
                int fragmentLength = Math.min(FRAGMENT_SIZE, responseData.length - i);
                byte[] fragment = Arrays.copyOfRange(responseData, i, i + fragmentLength);
                fragments.add(fragment);
            }

            // отправка данных
            for (byte[] fragment : fragments) {
                ByteBuffer responseBuffer = ByteBuffer.wrap(fragment);
                serverChannel.send(responseBuffer, clientAddress);
            }
            logger.info("Ответ " + response + " был отправлен " + clientAddress);

            oos.close();
        } catch (IOException e) {
            logger.warn("Произошла ошибка при отправке");
        }
    }
}