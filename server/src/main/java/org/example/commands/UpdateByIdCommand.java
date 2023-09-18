package org.example.commands;

import org.example.collection.classes.Worker;
import org.example.tools.CommandExecutor;

public class UpdateByIdCommand extends Command {
    public UpdateByIdCommand(CommandExecutor commandExecutor, String description, String name) {
        super(commandExecutor, description, name);
    }

    public void execute(String arg) {
        commandExecutor.updateById(arg);
    }

    public void execute(Worker worker) {
        commandExecutor.updateById(worker);
    }

}

