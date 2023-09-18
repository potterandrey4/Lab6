package org.example.messages;

import org.example.collection.classes.Worker;

public class MsgWithWorker extends BaseMsg {
    public MsgWithWorker(String command, Worker worker) {
        this.command = command;
        this.worker = worker;
    }

    public String getName() {
        return command;
    }

    public Worker getWorker() {
        return worker;
    }
}
