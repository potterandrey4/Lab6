package org.example;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import generalModule.exceptions.ReadException;
import org.example.collection.classes.Worker;
import org.example.messages.BaseMsg;
import org.example.messages.Msg;
import org.example.messages.MsgWithArg;
import org.example.messages.MsgWithWorker;
import org.example.tools.CommandExecutor;
import org.example.tools.CommandManager;
import org.example.tools.io.JacksonXmlReader;
import org.example.tools.io.OutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Objects;

public class ServerMain {

    static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {

        try {

            String env = "XML";
            String xmlFile;
            xmlFile = System.getenv(env);

            XmlMapper xmlMapper = new XmlMapper();
            LinkedList<Worker> collection = JacksonXmlReader.read(xmlMapper, Objects.requireNonNullElse(xmlFile, ""));

            CommandExecutor commandExecutor = new CommandExecutor(collection, xmlFile, xmlMapper);
            CommandManager commandManager = new CommandManager(commandExecutor);

            ServerConnect.connect();

            while (true) {
                BaseMsg receivedCommand = RequestReader.readCommand();

                if (receivedCommand instanceof Msg) {
                    String command = ((Msg) receivedCommand).getName();
                    commandManager.execute(command);
                }
                else if (receivedCommand instanceof MsgWithArg) {
                    MsgWithArg command = (MsgWithArg) receivedCommand;
                    String name = command.getName();
                    String arg = command.getArg();
                    commandManager.execute(name, arg);
                }
                else if (receivedCommand instanceof MsgWithWorker) {
                    String command = ((MsgWithWorker) receivedCommand).getName();
                    Worker worker = ((MsgWithWorker) receivedCommand).getWorker();
                    commandManager.execute(command, worker);
                }
            }

        } catch (ReadException e) {
            OutputHandler.printErr(e.getMessage());
        }
    }
}
