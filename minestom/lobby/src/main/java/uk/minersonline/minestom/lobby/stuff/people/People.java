package uk.minersonline.minestom.lobby.stuff.people;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.util.ArrayList;
import java.util.List;

public class People extends Command {
    private final List<Person> people = new ArrayList<>();

    public People() {
        super("people");

        var commandAdd = ArgumentType.Literal("add");
        var name = ArgumentType.String("name");
        var age = ArgumentType.Integer("age");
        var address = ArgumentType.String("address");

        var commandSpeak = ArgumentType.Literal("speak");
        var uuid = ArgumentType.UUID("uuid");
        var phrase = ArgumentType.String("phrase");

        addSyntax((sender, context) -> {
            String pName = context.get(name);
            int pAge = context.get(age);
            String pAddress = context.get(address);
            Person person = new Person(pName, pAge, pAddress);
            people.add(person);
            sender.sendMessage("Added "+pName);
        }, commandAdd, name, age, address);

        addSyntax((sender, context) -> {
            for (Person person : people) {
                if (person.getUuid().equals(context.get(uuid))) {
                    sender.sendMessage(person.speak(context.get(phrase)));
                    break;
                }
            }
        }, commandSpeak, uuid, phrase);

        addSyntax((sender, context) -> {
            for (Person person : people) {
                if (person.getName().equals(context.get(name))) {
                    sender.sendMessage(person.speak(context.get(phrase)));
                    break;
                }
            }
        }, commandSpeak, name, phrase);

        setDefaultExecutor((sender, context) -> {
            for (Person person : people) {
                sender.sendMessage(person.getName() + " is " + person.getAge() + " years old and lives at " + person.getAddress() + ". They have the UUID " + person.getUuid() + ".");
            }
        });
    }

    public static void init() {
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new People());
    }
}
