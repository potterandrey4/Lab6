package org.example;

import org.example.commads.Command;
import org.example.consoleModule.ReadConsole;
import org.example.io.OutputHandler;
import org.example.tools.CommandManager;

import java.util.LinkedHashMap;

public class ClientMain {
    public static void main(String[] args) {

        CommandManager commandManager = new CommandManager();
        ClientConnect.connect();
        LinkedHashMap<String, Command> commands = commandManager.getCommands();

        while (true) {
            String command = ReadConsole.read();
            String[] commandList = command.split(" ");


            if (commands.containsKey(commandList[0])) {
                if (commandList.length == 1) {
                    commandManager.execute(commandList[0]);
                } else if (commandList.length == 2) {
                    commandManager.execute(commandList[0], commandList[1]);
                }
            }
            else {
                if (commandList.length == 1) {
                    ResponseSender.sendCommand(commandList[0]);
                } else if (commandList.length == 2) {
                    ResponseSender.sendCommandWithArg(commandList[0], commandList[1]);
                }
                OutputHandler.println(RequestReader.read());
            }

        }
    }
}
