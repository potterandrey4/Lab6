package org.example.messages;

public class MsgWithArg extends BaseMsg {
    public MsgWithArg(String command, String arg) {
        this.command = command;
        this.arg = arg;
    }

    public String getName() {
        return command;
    }

    public String getArg() {
        return arg;
    }
}