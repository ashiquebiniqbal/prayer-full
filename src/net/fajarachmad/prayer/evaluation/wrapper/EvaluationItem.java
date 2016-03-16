package net.fajarachmad.prayer.evaluation.wrapper;

import java.util.Date;

/**
 * Created by user on 3/6/2016.
 */
public class EvaluationItem {

    public static String ENTRY_TYPE_YES_NO = "yesnno";
    public static String ENTRY_TYPE_FREE = "free";

    private String id;
    private String goalName;
    private String entryType;
    private String target;
    private String targetUnit;
    private String targetFrequency;
    private Date startDate;
    private Date creationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }

    public String getTargetFrequency() {
        return targetFrequency;
    }

    public void setTargetFrequency(String targetFrequency) {
        this.targetFrequency = targetFrequency;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
