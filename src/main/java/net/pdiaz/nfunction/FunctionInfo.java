package net.pdiaz.nfunction;

/**
 * Created by pablo on 9/20/15.
 */
public class FunctionInfo {
    private final int i;
    private final int prevI;
    private final int nextI;
    private final int num;
    private final String generic;
    private final String generics;
    private final boolean last;

    public FunctionInfo(int i, int num, String generic, String generics) {
        this.i = i;
        this.num = num;
        this.generic = generic;
        this.generics = generics;
        this.prevI = i - 1;
        this.nextI = i + 1;
        this.last = i==num;
    }

    public int getI() {
        return i;
    }

    public int getPrevI() {
        return prevI;
    }

    public int getNextI() {
        return nextI;
    }

    public int getNum() {
        return num;
    }

    public String getGeneric() {
        return generic;
    }

    public String getGenerics() {
        return generics;
    }

    public boolean isLast() {
        return last;
    }

    @Override
    public String toString() {
        return "FunctionInfo{" +
                "i=" + i +
                ", prevI=" + prevI +
                ", nextI=" + nextI +
                ", num=" + num +
                ", generic='" + generic + '\'' +
                ", generics='" + generics + '\'' +
                ", last=" + last +
                '}';
    }
}
