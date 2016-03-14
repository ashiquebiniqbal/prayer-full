package net.fajarachmad.prayer.evaluation.wrapper;

import java.util.List;

/**
 * Created by user on 3/11/2016.
 */
public class ReminderData {
    private String evaluationId;
    private List<Reminder> reminders;

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }
}
