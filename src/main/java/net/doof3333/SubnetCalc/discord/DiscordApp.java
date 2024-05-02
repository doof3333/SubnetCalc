package net.doof3333.SubnetCalc.discord;

import net.doof3333.SubnetCalc.Logger;
import net.doof3333.discord.commands.CommandHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DiscordApp {
    private static final Logger LOGGER = new Logger(DiscordApp.class.getSimpleName());

    public DiscordApp(String token) {

        if (token == null) {
            try {
                Path path = Path.of("token");
                token = Files.readString(path);
            } catch (IOException e) {
                LOGGER.error("Failed to read bot token.");
                return;
            }
        }

        CommandHandler cmdHandler = new CommandHandler(true);
        cmdHandler.addCommands(new SubnetCommand());

        // Light is sufficient for this app!
        JDABuilder jdaBuilder = JDABuilder.createLight(token);
        jdaBuilder.setMaxReconnectDelay(32);
        jdaBuilder.setActivity(Activity.customStatus("SubnetCalc"));
        jdaBuilder.addEventListeners(
                cmdHandler
        );

        LOGGER.info("Connecting to Discord...");
        // JDA will start its own thread.
        jdaBuilder.build();
    }

}