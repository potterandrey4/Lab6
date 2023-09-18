package org.example;

import org.example.collection.classes.Worker;
import org.example.io.OutputHandler;
import org.example.messages.Msg;
import org.example.messages.MsgWithArg;
import org.example.messages.MsgWithWorker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

import static org.example.ClientConnect.*;
import static org.example.ClientConnect.serverPort;

public class ResponseSender {

    private static void sendData(Object o) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.flush();

            byte[] serializedData = baos.toByteArray();

            DatagramPacket packet = new DatagramPacket(serializedData, serializedData.length, serverAddress, serverPort);

            clientSocket.send(packet);

            oos.close();
        } catch (IOException e) {
            OutputHandler.printErr("Данные не отправлены");
        }
    }


    public static void sendCommand(String command) {
        Msg response = new Msg(command);
        sendData(response);
    }

    public static void sendCommandWithArg(String command, String argument) {
        MsgWithArg response = new MsgWithArg(command, argument);
        sendData(response);
    }

    public static void sendCommandWithWorker(String command, Worker worker) {
        MsgWithWorker response = new MsgWithWorker(command, worker);
        sendData(response);
    }
}
