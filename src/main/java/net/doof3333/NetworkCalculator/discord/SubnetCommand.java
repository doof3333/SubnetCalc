package net.doof3333.NetworkCalculator.discord;

import net.doof3333.NetworkCalculator.NetworkData;
import net.doof3333.NetworkCalculator.SubnetCalculator;
import net.doof3333.discord.commands.GuildSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

public class SubnetCommand implements GuildSlashCommand {

    private final SlashCommandData data;

    // Option names have to be lowercase.
    private final String ipOptionName = "ipv4";
    private final String numOfClientsOptionName = "number-of-clients";

    public SubnetCommand() {
        data = Commands.slash("subnet","Calculate IPv4 Addresses.")
                .setGuildOnly(true);

        OptionData ipOption = new OptionData(
                OptionType.STRING,
                ipOptionName,
                "In CIDR-notation if number of clients is not specified.",
                true);

        OptionData numOfClientsOption = new OptionData(
                OptionType.INTEGER,
                numOfClientsOptionName,
                "Optional",
                false);
        numOfClientsOption.setRequiredRange(1, SubnetCalculator.MAX_NUMBER_OF_CLIENTS);

        data.addOptions(ipOption, numOfClientsOption);
    }

    @Override
    public SlashCommandData getData() {
        return data;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {

        OptionMapping ipInput = event.getOption(ipOptionName);
        OptionMapping numOfClientsInput = event.getOption(numOfClientsOptionName);

        String ip = ipInput.getAsString();
        SubnetCalculator calculator;
        NetworkData network;

        try {
            if (numOfClientsInput != null) {
                // if provided
                int numOfClients = numOfClientsInput.getAsInt();
                calculator = new SubnetCalculator(ip, numOfClients);
            } else {
                calculator = new SubnetCalculator(ip);
            }

            network = calculator.calculate();

        } catch (IllegalArgumentException e) {

            event.reply("Invalid Input: " + e.getMessage())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        event.reply("```JSON\n" +
                        network.toJSON().toString(3) +
                        "```")
                .queue();
    }
}
