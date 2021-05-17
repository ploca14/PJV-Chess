package cz.cvut.fel.pjv.model;

public class JoinRequest {
    private final String name;
    private final String ip;
    private final int port;

    public JoinRequest(String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.port = Integer.parseInt(port);
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
