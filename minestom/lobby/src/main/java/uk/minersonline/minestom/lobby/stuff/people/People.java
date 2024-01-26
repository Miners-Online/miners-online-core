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
        addSyntax((sender, context) -> {
            String pName = context.get(name);
            int pAge = context.get(age);
            String pAddress = context.get(address);
            Person person = new Person(pName, pAge, pAddress);
            people.add(person);
            sender.sendMessage("Added "+pName);
        }, commandAdd, name, age, address);

        setDefaultExecutor((sender, context) -> {
            for (int i = 0; i < people.size(); i ++) {
                Person person = people.get(i);
                sender.sendMessage(person.name + " is " + person.age + " years old and lives at "+person.address+".");
            }
        });
    }

    public static void init() {
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new People());
    }
}
