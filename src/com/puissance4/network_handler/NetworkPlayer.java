package com.puissance4.network_handler;

/**
 * Created by fred on 10/01/15.
 */
public class NetworkPlayer {
    private String name;
    private Long latitude;
    private Long longitude;
    private int status; // 0 : AVAILABLE, 1 : BUSY, 2 : DISCONNECTED (only for friends)

    public NetworkPlayer(String name, Long latitude, Long longitude, int status) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }
}
