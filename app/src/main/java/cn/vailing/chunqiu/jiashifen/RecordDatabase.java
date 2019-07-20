package cn.vailing.chunqiu.jiashifen;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;

/**
 * Created by dream on 2018/7/4.
 */

public class RecordDatabase extends SugarRecord implements Serializable {
    @Unique
    private Long id;
    private String name;
    private String title;
    private int stage;//阶段
    private int lateVisitDay;
    private int createDay;
    private int shouldStudyTime;
    private int visitTimes;
    private int rightTimes;
    private int vagueTimes;
    private int errorTimes;
    private float carryLevel;

    public RecordDatabase() {
    }

    public RecordDatabase(String name, String title, int stage, int lateVisitDay, int createDay, int shouldStudyTime,
                          int visitTimes, int rightTimes, int vagueTimes, int errorTimes, float carryLevel) {
        this.name = name;
        this.title = title;
        this.stage = stage;
        this.lateVisitDay = lateVisitDay;
        this.createDay = createDay;
        this.shouldStudyTime = shouldStudyTime;
        this.visitTimes = visitTimes;
        this.rightTimes = rightTimes;
        this.vagueTimes = vagueTimes;
        this.errorTimes = errorTimes;
        this.carryLevel = carryLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getLateVisitDay() {
        return lateVisitDay;
    }

    public void setLateVisitDay(int lateVisitDay) {
        this.lateVisitDay = lateVisitDay;
    }

    public int getCreateDay() {
        return createDay;
    }

    public void setCreateDay(int createDay) {
        this.createDay = createDay;
    }

    public int getShouldStudyTime() {
        return shouldStudyTime;
    }

    public void setShouldStudyTime(int shouldStudyTime) {
        this.shouldStudyTime = shouldStudyTime;
    }

    public int getVisitTimes() {
        return visitTimes;
    }

    public void setVisitTimes(int visitTimes) {
        this.visitTimes = visitTimes;
    }

    public int getRightTimes() {
        return rightTimes;
    }

    public void setRightTimes(int rightTimes) {
        this.rightTimes = rightTimes;
    }

    public int getVagueTimes() {
        return vagueTimes;
    }

    public void setVagueTimes(int vagueTimes) {
        this.vagueTimes = vagueTimes;
    }

    public int getErrorTimes() {
        return errorTimes;
    }

    public void setErrorTimes(int errorTimes) {
        this.errorTimes = errorTimes;
    }

    public float getCarryLevel() {
        return carryLevel;
    }

    public void setCarryLevel(float carryLevel) {
        this.carryLevel = carryLevel;
    }

    public Long getId() {
        return id;
    }

    //    private Long id;
//    private String name;
//    private String title;
//    private int stage;//阶段
//    private int lateVisitDay;
//    private int createDay;
//    private int shouldStudyTime;
//    private int visitTimes;
//    private int rightTimes;
//    private int vagueTimes;
//    private int errorTimes;
//    private int carryLevel;
    @Override
    public String toString() {
        return "id:" + id + " name:" +
                name + " title:" + title +
                " stage:" + stage +
                " lateVisitDay:" + lateVisitDay +
                " createDay:" + createDay +
                " shouldStudyTime:" + shouldStudyTime +
                " visitTimes:" + visitTimes +
                " vagueTimes:" + vagueTimes +
                " errorTimes:" + errorTimes +
                " carryLevel:" + carryLevel +
                " rightTimes" + rightTimes;
    }
}
