package org.example.tools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.ResponseSender;
import org.example.collection.classes.Worker;
import org.example.commands.Command;
import org.example.commands.HistoryCommand;
import org.example.tools.io.JacksonXmlWriter;
import org.example.tools.io.OutputHandler;
import org.example.toolsForCollection.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.tools.CheckSizeCollection.checkerSizeCollection;

public class CommandExecutor {

    static LinkedList<Worker> collection;
    XmlMapper xmlMapper;
    String xmlFile;

    List<String> commandsList = new ArrayList<>();

    public CommandExecutor(LinkedList<Worker> collection, String xmlFile, XmlMapper xmlMapper) {
        CommandExecutor.collection = collection;
        this.xmlFile = xmlFile;
        this.xmlMapper = xmlMapper;
    }

    public static Vector<Worker> statusSorter() {
        StatusOfWorkerComparator comp = new StatusOfWorkerComparator();
        Vector<Worker> list = new Vector<>(collection);
        list.sort(comp);
        return list;
    }

    public void help() {
        StringBuilder sb = new StringBuilder();
        for (Command element : CommandManager.getCommands().values()) {
            sb.append(element.description());
            sb.append("\n");
        }
        commandsList.add("help");

        ResponseSender.sendCommand(sb.toString());
    }

    public void exit() {
    }

    public void add(Worker worker) {

        int id = GetNewId.get(collection);
        worker.setId(id);
        worker.setCreationDate(LocalDateTime.now());

        collection.add(worker);
        save();
        ResponseSender.sendCommand("Worker добавлен");
        commandsList.add("add");
    }

    public void show() {
        ShowCollection.show(collection);
        commandsList.add("show");
    }

    public void clear() {
        collection.clear();
        save();
        ResponseSender.sendCommand("Коллекция очищена");
        commandsList.add("clear");
    }

    public void head() {
        if (checkerSizeCollection(collection)) {
            ResponseSender.sendCommand(collection.getFirst().toString());
        } else {
            ResponseSender.sendCommand("Коллекция пуста");
        }
        commandsList.add("head");
    }

    public void removeById(int id) {
        try {
            if (checkerSizeCollection(collection)) {
                int lastLengthCollection = collection.size();
                collection = RemoveById.remove(id, collection);
                save();
                if (lastLengthCollection != collection.size()) {
                    ResponseSender.sendCommand("Элемент успешно удалён");
                    commandsList.add("remove_by_id");
                } else {
                    ResponseSender.sendCommand("Элемент с указанным ID не найден");
                }
            } else {
                ResponseSender.sendCommand("Коллекция пуста");
            }
        } catch (NumberFormatException e) {
            ResponseSender.sendCommand("Некорректный формат ID");
        }
    }

    public void updateById(String stId) {
        int id;
        if (stId.trim().isEmpty()) {
            ResponseSender.sendCommand("false");
        } else {
            try {
                id = Integer.parseInt(stId);
                boolean flag = false;
                if (checkerSizeCollection(collection)) {
                    for (Worker worker : collection) {
                        if (Objects.equals(worker.getId(), id)) {
                            collection = RemoveById.remove(id, collection);
                            save();
                            flag = true;
                            ResponseSender.sendCommand("true");
                            break;
                        }
                    }
                }
                if (!flag) {
                    ResponseSender.sendCommand("false");
                }
            } catch (NumberFormatException e) {
                ResponseSender.sendCommand("false");
            }
        }
    }

    public void updateById(Worker worker) {
        worker.setCreationDate(LocalDateTime.now());
        collection.add(worker);
        ResponseSender.sendCommand("Объект успешно изменён");
        save();
        commandsList.add("update_by_id");
    }

    // значимость статусов сотрудников: regular, probation, hired, fired
    public void printFieldDescendingStatus() {
        // Если понимать формулировку "все используемые статусы в порядке убывания"
        if (checkerSizeCollection(collection)) {
            ResponseSender.sendCommand(PrintFieldDescending.print());
        } else {
            ResponseSender.sendCommand("Коллекция пуста");
        }

        commandsList.add("print_field_descending_status");
    }

    public void maxByStatus() {
        if (checkerSizeCollection(collection)) {
            ResponseSender.sendCommand(statusSorter().elementAt(0).toString());
            commandsList.add("max_by_status");
        } else {
            ResponseSender.sendCommand("Коллекция пуста");
        }
    }

    public void removeGreater(int id) {
        if (checkerSizeCollection(collection)) {
            try {
                int lastLengthCollection = collection.size();
                collection = RemoveGreater.remove(id, collection);
                save();
                if (lastLengthCollection != collection.size()) {
                    ResponseSender.sendCommand("Элементы начиная с ID=" + id + " и старше удалены");
                    commandsList.add("remove_greater");
                } else {
                    ResponseSender.sendCommand("Элемента с заданным ID не найдено");
                }
            } catch (NumberFormatException e) {
                ResponseSender.sendCommand("Некорректный формат ID");
            }
        } else {
            ResponseSender.sendCommand("Коллекция пуста");
        }
    }

    public void info() {
        StringBuilder sb = new StringBuilder();
        sb.append("Информация о коллекции:\n\tТип: LinkedList\n\tКласс объектов: Worker\n");
        sb.append("\tКоличество элементов: ").append(collection.size()).append("\n\t");
        sb.append(LastModifyCollection.getLastTime(collection));

        ResponseSender.sendCommand(sb.toString());
        commandsList.add("info");
    }

    public void history() {
        commandsList.add("history");
        StringBuilder sb = new StringBuilder();
        if (commandsList.size() < HistoryCommand.number) {
            sb.append(commandsList);
        } else {
            sb.append(commandsList.subList(commandsList.size() - HistoryCommand.number, commandsList.size()));
        }

        ResponseSender.sendCommand(sb.toString());
    }

    //group_counting_by_name
    public void groupCountingByName() {
        if (checkerSizeCollection(collection)) {
            Stream<Worker> streamWorker = collection.stream();
            Map<String, Long> workerByName = streamWorker.collect(Collectors.groupingBy(
                    Worker::getName,
                    Collectors.counting()
            ));
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<String, Long> item : workerByName.entrySet()) {
                sb.append(item.getKey()).append(" - ").append(item.getValue()).append("\n");
            }
            ResponseSender.sendCommand(sb.toString());
        } else {
            ResponseSender.sendCommand("Коллекция пуста");
        }
        commandsList.add("group_counting_by_name");
    }

    public void save() {
        try {
            File file = new File(xmlFile);
            if (!file.exists()) {
                OutputHandler.printErr("Не сохранено");
            } else {
                PrintWriter pw = new PrintWriter(xmlFile);
                pw.close();
                JacksonXmlWriter.write(xmlMapper, xmlFile, collection);
                OutputHandler.println("Сохранено");
                commandsList.add("save");
            }
        } catch (IOException | NullPointerException e) {
            OutputHandler.printErr("Не сохранено");
        }
    }


}