package org.example.commands;

import org.example.ResponseSender;
import org.example.tools.CommandExecutor;

public class RemoveByIdCommand extends Command {
    public RemoveByIdCommand(CommandExecutor commandExecutor, String description, String name) {
        super(commandExecutor, description, name);
    }

    public void execute() {
        ResponseSender.sendCommand("Вы не ввели id");
    }

    public void execute(String arg) {
        try {
            commandExecutor.removeById(Integer.parseInt(arg));
        } catch (NumberFormatException e) {
            ResponseSender.sendCommand("Введённый аргумент не является целочисленным");
        }
    }

}
