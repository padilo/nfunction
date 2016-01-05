package net.pdiaz.nfunction.base;

import java.util.Iterator;

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

    private FunctionInfo(int num, String generic) {
        this(0, num, generic, "");
    }

    private FunctionInfo(int i, int num, String generic, String generics) {
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

    FunctionInfo next() {
        int nextI = i+1;
        String nextGenerics = generics.equals("")? generic + nextI: generics + ", " + generic + nextI;

        return new FunctionInfo(nextI, num, generic, nextGenerics);
    }

    public static Iterable<FunctionInfo> iterable(int num, final String genericName) {
        return () -> new FunctionInfoIterator(num, genericName);
    }

    public static class FunctionInfoIterator implements Iterator<FunctionInfo> {
        private FunctionInfo functionInfo;

        private FunctionInfoIterator(int num, String genericName) {
            this.functionInfo = new FunctionInfo(num, genericName);
        }

        @Override
        public boolean hasNext() {
            return !functionInfo.isLast();
        }

        @Override
        public FunctionInfo next() {
            functionInfo = functionInfo.next();

            return functionInfo;
        }
    }
}
