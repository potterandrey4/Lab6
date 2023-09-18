package org.example.messages;

import org.example.collection.classes.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public abstract class BaseMsg implements Serializable {
    String command;
    String arg;
    Worker worker;
    LinkedList<Worker> collection;
}
