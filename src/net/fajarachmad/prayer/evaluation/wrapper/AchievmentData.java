package net.fajarachmad.prayer.evaluation.wrapper;

import java.util.List;

/**
 * Created by user on 3/12/2016.
 */
public class AchievmentData {

    private String evaluationId;
    private List<Achievment> achievments;

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public List<Achievment> getAchievments() {
        return achievments;
    }

    public void setAchievments(List<Achievment> achievments) {
        this.achievments = achievments;
    }
}
