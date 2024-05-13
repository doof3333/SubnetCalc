package net.doof3333.SubnetCalc;

import net.doof3333.SubnetCalc.utils.Ansi;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public record NetworkData(
        int ipAddress,
        int networkAddress,
        int subnetMask,
        int broadcastAddress,
        int firstClient,
        int lastClient,
        int networkAddressBits,
        int maxClients
) {

    @NotNull
    public String format() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(line("CIDR-Suffix", networkAddressBits));
        joiner.add(line("Max. Clients", maxClients));
        joiner.add(line("IP Address", formatAddress(ipAddress)));
        joiner.add(line("Network ID", formatAddress(networkAddress)));
        joiner.add(line("Subnet Mask", formatAddress(subnetMask)));
        joiner.add(line("Broadcast", formatAddress(broadcastAddress)));
        joiner.add(line("First Client", formatAddress(firstClient)));
        joiner.add(line("Last Client", formatAddress(lastClient)));
        return joiner.toString();
    }

    private String line(String key, Object value) {
        return Ansi.WHITE + String.format("%1$-15s", key + ": ") + Ansi.GREEN + value;
    }

    private String formatAddress(int bitmask) {
        StringJoiner addressJoiner = new StringJoiner(".");

        for (int i = 0; i < 4; i++) {
            int value = bitmask << (i * 8) >>> 24;
            addressJoiner.add(String.valueOf(value));
        }

        return addressJoiner.toString();
    }
}
