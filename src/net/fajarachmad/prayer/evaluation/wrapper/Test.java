package net.fajarachmad.prayer.evaluation.wrapper;

/**
 * Created by user on 3/11/2016.
 */
public class Test {
    private static Test ourInstance = new Test();

    public static Test getInstance() {
        return ourInstance;
    }

    private Test() {
    }
}
