package uk.minersonline.minestom.lobby.stuff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.util.ArrayList;
import java.util.List;

public class People extends Command {
    private final List<String> names = new ArrayList<>();
    private final List<Integer> ages = new ArrayList<>();
    private final List<String> addresses = new ArrayList<>();

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
            names.add(pName);
            ages.add(pAge);
            addresses.add(pAddress);
            sender.sendMessage("Added "+pName);
        }, commandAdd, name, age, address);

        setDefaultExecutor((sender, context) -> {
            for (int i = 0; i < names.size(); i ++) {
                String pName = names.get(i);
                int pAge = ages.get(i);
                String pAddress = addresses.get(i);
                sender.sendMessage(pName + " is " + pAge + " years old and lives at "+pAddress+".");
            }
        });
    }

    public static void init() {
        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new People());
    }
}
