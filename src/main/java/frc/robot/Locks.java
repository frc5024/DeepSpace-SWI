package frc.robot;

public class Locks {
    public static Locks mInstance = null;

    public static Locks getInstance() {
        if (mInstance == null) {
            mInstance = new Locks();
        }

        return mInstance;
    }

    /* DATA */
    boolean isClimbing = false;
    boolean chinupSuccess = false;

    

}