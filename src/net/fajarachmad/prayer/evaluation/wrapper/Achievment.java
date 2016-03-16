package net.fajarachmad.prayer.evaluation.wrapper;

import java.util.Date;

/**
 * Created by user on 3/12/2016.
 */
public class Achievment {

    private String id;
    private String evaluationId;
    private Date date;
    private String achievment;
    private String targentUnit;
    private String entryType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTargentUnit() {
        return targentUnit;
    }

    public void setTargentUnit(String targentUnit) {
        this.targentUnit = targentUnit;
    }

    public String getAchievment() {
        return achievment;
    }

    public void setAchievment(String achievment) {
        this.achievment = achievment;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }
}
