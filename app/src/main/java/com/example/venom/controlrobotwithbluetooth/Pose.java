package com.example.venom.controlrobotwithbluetooth;

public class Pose {
    byte[] position = new byte[17];

    public Pose() {
        for (int i = 0; i < MainActivity.NUMBER_OF_SERVOS; i++) {
            position[i] = 0;
        }
    }

    public Pose(byte[] listOfPositions) {
        position = listOfPositions;
    }
}
