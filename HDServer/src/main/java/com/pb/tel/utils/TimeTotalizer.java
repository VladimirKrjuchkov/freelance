//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pb.tel.utils;

public class TimeTotalizer {
    private final long timiestamp;
    private long start;
    private final long createTime;

    private TimeTotalizer() {
        this.setStart(System.currentTimeMillis());
        this.createTime = this.getStart();
        this.timiestamp = System.nanoTime();
    }

    public long checkPoint() {
        return System.currentTimeMillis() - this.getStart();
    }

    public long checkPointWithReset() {
        long result = System.currentTimeMillis() - this.getStart();
        this.setStart(System.currentTimeMillis());
        return result;
    }

    public String checkPointWithResetAsString() {
        return " at " + this.checkPointWithReset() + " ms";
    }

    public long getTimestamt() {
        return this.timiestamp;
    }

    public long createTime() {
        return this.createTime;
    }

    public static TimeTotalizer create() {
        return new TimeTotalizer();
    }

    public static String timeAnswerNs(long timestampNs) {
        return timestampNs > 1L?"" + (System.nanoTime() - timestampNs) / 1000000L + " ms":"";
    }

    public static String timeAnswerMs(long timestampMs) {
        return "" + (System.currentTimeMillis() - timestampMs) + " ms";
    }

    public static void main(String... strings) throws InterruptedException {
        TimeTotalizer time = create();
        System.out.println(StringUtil.logRequest(time.getTimestamt(), (Object)null, "req data"));
        Thread.sleep(100L);
        System.out.println(StringUtil.logResponse(time.getTimestamt(), (Object)null, "resp data"));
    }

    public long getStart() {
        return this.start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public int hashCode() {
        boolean prime = true;
        int result = 1;
        result = 31 * result + (int)(this.createTime ^ this.createTime >>> 32);
        result = 31 * result + (int)(this.start ^ this.start >>> 32);
        result = 31 * result + (int)(this.timiestamp ^ this.timiestamp >>> 32);
        return result;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            TimeTotalizer other = (TimeTotalizer)obj;
            return this.createTime != other.createTime?false:(this.start != other.start?false:this.timiestamp == other.timiestamp);
        }
    }
}
