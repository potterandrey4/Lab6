package org.example.commads;

import org.example.RequestReader;
import org.example.io.OutputHandler;
import org.example.tools.CreateWorker;

import static org.example.ResponseSender.sendCommandWithWorker;

public class AddCommand extends Command {

    public AddCommand(String name) {
        super(name);
    }

    @Override
    public void execute(String arg) {execute();}

    @Override
    public void execute() {
        sendCommandWithWorker(this.name, CreateWorker.create());
        OutputHandler.println(RequestReader.read());
    }
}
