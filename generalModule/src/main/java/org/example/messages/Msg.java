package org.example.messages;

import java.io.Serial;

public class Msg extends BaseMsg {

    @Serial
    private static final long serialVersionUID = 1L;

    public Msg(String command) {
        this.command = command;
    }

   public String getName() {
        return command;
   }
}
