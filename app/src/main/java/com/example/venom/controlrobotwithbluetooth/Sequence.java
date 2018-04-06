package com.example.venom.controlrobotwithbluetooth;

import java.util.Vector;

public class Sequence {
    public final static int NUMBER_OF_SERVOS = 17;
    public final static int START_SEQUENCE = 200;
    public final static int STOP_SEQUENCE = 201;
    Vector<Pose> poses;
    int holdTime = 0;
    int speed = 10;

    public Sequence() {
        poses = new Vector<>();
    }

    public void setPoses(Vector<Pose> poses) {
        this.poses = poses;
    }

    public void setPose(int num, Pose pose){
        poses.set(num, pose);
    }
}
