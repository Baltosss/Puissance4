package com.puissance4.network_handler;

/**
 * Created by fred on 10/01/15.
 */
public class NetworkPlayer {
    private String name;
    private Double latitude;
    private Double longitude;
    private int status; // 0 : AVAILABLE, 1 : BUSY, 2 : DISCONNECTED (only for friends)

    public NetworkPlayer(String name, Double latitude, Double longitude, int status) {
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
