package net.doof3333.SubnetCalc;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

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
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cidr_suffix", networkAddressBits);
        jsonObject.put("max_num_of_clients", maxClients);
        jsonObject.put("ip_address", formatAddress(ipAddress));
        jsonObject.put("network_address", formatAddress(networkAddress));
        jsonObject.put("subnet_mask", formatAddress(subnetMask));
        jsonObject.put("broadcast_address", formatAddress(broadcastAddress));
        jsonObject.put("first_client", formatAddress(firstClient));
        jsonObject.put("last_client", formatAddress(lastClient));
        return jsonObject;
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
