package net.fajarachmad.prayer.evaluation.wrapper;

import java.util.Date;

/**
 * Created by user on 3/3/2016.
 */
public class EvaluationItemWrapper {

    private String id;
    private String goalName;
    private Date startDate;
    private float progress;
    private String progressString;
    private String startDateStr;
    private String targetUnit;
    private String target;
    private String entryType;

    public EvaluationItemWrapper(){}

    public EvaluationItemWrapper(String id, String goalName, String progress, String dueDate) {
        this.id = id;
        this.goalName = goalName;
        this.progressString = progress;
        this.startDateStr = dueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getProgressString() {
        return progressString;
    }

    public void setProgressString(String progressString) {
        this.progressString = progressString;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }
}
