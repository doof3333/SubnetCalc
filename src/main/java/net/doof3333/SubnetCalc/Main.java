package net.doof3333.SubnetCalc;

import net.doof3333.SubnetCalc.discord.DiscordApp;

public class Main {

    public static void main(String[] args) {

        String token = null;
        if (args.length != 0) {
            token = args[0];
        }

        new DiscordApp(token);

    }
}
