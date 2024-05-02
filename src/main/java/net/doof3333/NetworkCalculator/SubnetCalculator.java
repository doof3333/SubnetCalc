package net.doof3333.NetworkCalculator;

import org.jetbrains.annotations.NotNull;

public class SubnetCalculator {
    public static final Logger LOGGER = new Logger(SubnetCalculator.class.getSimpleName());
    public static final int MAX_NUMBER_OF_CLIENTS = 0xFFFFFFFF >>> 8;

    // Dunno how this pattern works... Thanks SOF
    private static final String IP_PATTERN =
            "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
    int networkAddressBits;
    String ipv4;

    /**
     * @param ipv4CIDR A valid IPv4 address in CIDR-notation (e.g. 192.168.0.42/24)
     * @throws IllegalArgumentException if ipv4CIDR cannot be parsed.
     */
    public SubnetCalculator(@NotNull String ipv4CIDR) throws IllegalArgumentException {
        int slashIndex = ipv4CIDR.indexOf("/");
        try {
            networkAddressBits = Integer.parseInt(ipv4CIDR.substring(slashIndex + 1));
            ipv4 = ipv4CIDR.substring(0, slashIndex);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Malformed CIDR");
        }
    }

    /**
     * @param ipv4 A valid IPv4 address (e.g. 192.168.0.42)
     * @param numberOfClients The number of clients in this network
     * @throws IllegalArgumentException if the address cannot be parsed
     */
    public SubnetCalculator(@NotNull String ipv4, int numberOfClients) throws IllegalArgumentException {
        if (numberOfClients < 1 || numberOfClients > MAX_NUMBER_OF_CLIENTS) {
            throw new IllegalArgumentException("Number of clients out of range (1 - " + MAX_NUMBER_OF_CLIENTS);
        }

        this.ipv4 = ipv4;
        networkAddressBits = Integer.numberOfLeadingZeros(numberOfClients);
    }


    /**
     * @return The calculated network data
     * @throws IllegalArgumentException If the arguments provided to the constructor are out of range
     */
    @NotNull
    public NetworkData calculate() throws IllegalArgumentException {
//        LOGGER.debug("IP: " + ipv4 + " netAddressLength: " + networkAddressBits);

        int hostBits = 32 - networkAddressBits;

        int ipAddress = getIpBitmask(ipv4, networkAddressBits);
        int networkAddress = ipAddress >> hostBits << hostBits;
        int subnetMask = 0xFFFFFFFF >> hostBits << hostBits;
        int firstClient = networkAddress + 1;
        int broadcastAddress = (0xFFFFFFFF << networkAddressBits >>> networkAddressBits) | networkAddress;
        int lastClient = broadcastAddress - 1;
        int numClients = lastClient - firstClient + 1;

//        LOGGER.debug("hostBits:         " + hostBits);
//        LOGGER.debug("ipAddress:        " + Integer.toBinaryString(ipAddress | Integer.MIN_VALUE));
//        LOGGER.debug("networkAddress:   " + Integer.toBinaryString(networkAddress | Integer.MIN_VALUE));
//        LOGGER.debug("subnetMask:       " + Integer.toBinaryString(subnetMask | Integer.MIN_VALUE));
//        LOGGER.debug("firstClient:      " + Integer.toBinaryString(firstClient | Integer.MIN_VALUE));
//        LOGGER.debug("broadcastAddress: " + Integer.toBinaryString(broadcastAddress | Integer.MIN_VALUE));
//        LOGGER.debug("lastClient:       " + Integer.toBinaryString(lastClient | Integer.MIN_VALUE));

        return new NetworkData(
                ipAddress,
                networkAddress,
                subnetMask,
                broadcastAddress,
                firstClient,
                lastClient,
                networkAddressBits,
                numClients);
    }

    private int getIpBitmask(@NotNull String ipv4, int networkAddressBits) throws IllegalArgumentException {
        if (!ipv4.matches(IP_PATTERN)) {
            throw new IllegalArgumentException("Invalid IPv4 address");
        }
        if (networkAddressBits < 1 || networkAddressBits > 30) {
            throw new IllegalArgumentException("Network address length out of range (1-30)");
        }

        String[] valueStrings = ipv4.split("\\.", 4);
        int ipAddress = 0;

        // Convert input to 32 bit bitmask
        // Loop from leftmost to rightmost bits
        for (int i = 0; i < 4; i++) {
            int value = Integer.parseInt(valueStrings[i]);

            value = value << ((3 - i) * 8);
            ipAddress |= value;
        }

        return ipAddress;
    }

}