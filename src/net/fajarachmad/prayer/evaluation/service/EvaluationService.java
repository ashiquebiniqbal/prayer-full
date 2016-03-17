package net.fajarachmad.prayer.evaluation.service;

import android.content.Context;

import com.google.gson.Gson;

import net.fajarachmad.prayer.common.util.IDGenerator;
import net.fajarachmad.prayer.common.util.StringUtil;
import net.fajarachmad.prayer.evaluation.wrapper.Achievment;
import net.fajarachmad.prayer.evaluation.wrapper.AchievmentData;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationData;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItem;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;
import net.fajarachmad.prayer.evaluation.wrapper.Reminder;
import net.fajarachmad.prayer.evaluation.wrapper.ReminderData;
import net.fajarachmad.prayer.evaluation.wrapper.ReminderItemWrapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by user on 3/6/2016.
 */
public class EvaluationService {

    private static String EVALUATION_FILE_NAME = "evaluationitem";
    private static String REMINDER_FILE_NAME = "_reminderdata";
    private static String ACHIEVMENT_FILE_NAME = "_achievmentdata";

    private static EvaluationService service;
    private static Context context;
    private static Gson gson;
    private static SimpleDateFormat dateFormatter;
    private static IDGenerator idGenerator;

    public static EvaluationService getInstance(Context context) {
        if (service == null) {
            service.context = context;
            gson = new Gson();
            dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            idGenerator = IDGenerator.getInstance();
            return new EvaluationService();
        } else {
            return service;
        }
    }

    public void delete(Achievment data) {
        String documentId = data.getEvaluationId()+ACHIEVMENT_FILE_NAME;
        String fileContent = readFileContent(documentId);
        AchievmentData dataObj = gson.fromJson(fileContent, AchievmentData.class);

        if (dataObj == null) {
            dataObj = new AchievmentData();
            dataObj.setEvaluationId(data.getEvaluationId());
            dataObj.setAchievments(new ArrayList<Achievment>());
        }

        for (Achievment item : dataObj.getAchievments()) {
            if (item.getId().equals(data.getId())) {
                dataObj.getAchievments().remove(item);
                break;
            }
        }

        String dataStr = gson.toJson(dataObj);
        writeDataToFile(documentId, dataStr);
    }

    public void save(Achievment data) {
        String documentId = data.getEvaluationId()+ACHIEVMENT_FILE_NAME;
        String fileContent = readFileContent(documentId);
        AchievmentData dataObj = gson.fromJson(fileContent, AchievmentData.class);

        if (dataObj == null) {
            dataObj = new AchievmentData();
            dataObj.setEvaluationId(data.getEvaluationId());
            dataObj.setAchievments(new ArrayList<Achievment>());
        }

        if (data.getId() == null) {
            data.setId(idGenerator.generate());
            dataObj.getAchievments().add(data);
        } else {
            for (Achievment achievment: dataObj.getAchievments() ) {
                if (achievment.getId().equals(data.getId())) {
                    dataObj.getAchievments().remove(achievment);
                    break;
                }
            }
            dataObj.getAchievments().add(data);
        }

        String dataStr = gson.toJson(dataObj);
        writeDataToFile(documentId, dataStr);
    }

    public void save(Reminder data) {
        String documentId = data.getEvaluationId()+REMINDER_FILE_NAME;
        String fileContent = readFileContent(documentId);
        ReminderData dataObj = gson.fromJson(fileContent, ReminderData.class);

        if (dataObj == null) {
            dataObj = new ReminderData();
            dataObj.setEvaluationId(data.getEvaluationId());
            dataObj.setReminders(new ArrayList<Reminder>());
        }

        if (data.getId() == null) {
            data.setId(idGenerator.generate());
            dataObj.getReminders().add(data);
        } else {
            for (Reminder reminder : dataObj.getReminders() ) {
                if (reminder.getId().equals(data.getId())) {
                    dataObj.getReminders().remove(reminder);
                    break;
                }
            }
            dataObj.getReminders().add(data);
        }

        String dataStr = gson.toJson(dataObj);
        writeDataToFile(documentId, dataStr);
    }

    public void delete(String evaluationId, String reminderId) {
        String documentId = evaluationId+REMINDER_FILE_NAME;
        String fileContent = readFileContent(documentId);
        ReminderData dataObj = gson.fromJson(fileContent, ReminderData.class);

        if (dataObj == null) {
            dataObj = new ReminderData();
            dataObj.setEvaluationId(evaluationId);
            dataObj.setReminders(new ArrayList<Reminder>());
        }

        for (Reminder reminder : dataObj.getReminders() ) {
            if (reminder.getId().equals(reminderId)) {
                dataObj.getReminders().remove(reminder);
                break;
            }
        }

        String dataStr = gson.toJson(dataObj);
        writeDataToFile(documentId, dataStr);
    }

    private void writeDataToFile(String fileName, String data) {
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void save(EvaluationItem data) {
        String fileContent = readFileContent(EVALUATION_FILE_NAME);
        EvaluationData dataObj = gson.fromJson(fileContent, EvaluationData.class);

        if (dataObj == null) {
            dataObj = new EvaluationData();
            dataObj.setEvaluationItems(new ArrayList<EvaluationItem>());
        }

        if (data.getId() == null) {
            data.setId(idGenerator.generate());
        } else {
            for (EvaluationItem item: dataObj.getEvaluationItems() ) {
                if (item.getId().equals(data.getId())) {
                    dataObj.getEvaluationItems().remove(item);
                    break;
                }
            }
        }
        dataObj.getEvaluationItems().add(data);
        String dataStr = gson.toJson(dataObj);
        writeDataToFile(EVALUATION_FILE_NAME, dataStr);

    }

    public void delete(String evaluationItemId) {
        String fileContent = readFileContent(EVALUATION_FILE_NAME);
        EvaluationData dataObj = gson.fromJson(fileContent, EvaluationData.class);

        if (dataObj == null) {
            dataObj = new EvaluationData();
            dataObj.setEvaluationItems(new ArrayList<EvaluationItem>());
        }

        for (EvaluationItem item: dataObj.getEvaluationItems()) {
            if (item.getId().equals(evaluationItemId)) {
                dataObj.getEvaluationItems().remove(item);
                break;
            }
        }

        //delete reminders
        context.deleteFile(evaluationItemId+EVALUATION_FILE_NAME);

        //delete achievement;
        context.deleteFile(evaluationItemId+ACHIEVMENT_FILE_NAME);

        String dataStr = gson.toJson(dataObj);
        writeDataToFile(EVALUATION_FILE_NAME, dataStr);
    }

    private String readFileContent(String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<EvaluationItemWrapper> getListData() {
        List<EvaluationItemWrapper> list = new ArrayList<EvaluationItemWrapper>();

        String fileContent = readFileContent(EVALUATION_FILE_NAME);

        if (fileContent != null) {
            EvaluationData data = gson.fromJson(fileContent, EvaluationData.class);
            SimpleDateFormat format = new SimpleDateFormat("E, MMM d yyyy", context.getResources().getConfiguration().locale);
            for (EvaluationItem item: data.getEvaluationItems()) {
                EvaluationItemWrapper wrapper = new EvaluationItemWrapper(item.getId(), item.getGoalName(),null, item.getStartDate() != null ? format.format(item.getStartDate()) : "");
                wrapper.setStartDate(item.getStartDate());
                wrapper.setTargetUnit(item.getTargetUnit());
                wrapper.setTarget(item.getTarget());
                wrapper.setEntryType(item.getEntryType());

                float pct = calculateCurrentProgress(item.getId(), item.getTarget());
                wrapper.setProgress(pct);
                wrapper.setProgressString(pct+"%");

                list.add(wrapper);
            }
        }



        return list;
    }

    public float calculateCurrentProgress(String evaluationItemId, String targetStr) {
        List<Achievment> achievments = getListAchievment(evaluationItemId);
        Integer target = !StringUtil.isBlank(targetStr) ? Integer.valueOf(targetStr) : 1;
        Integer totalAchievment = 0;
        for (Achievment achievment: achievments ) {
            totalAchievment = totalAchievment + (!StringUtil.isBlank(achievment.getAchievment()) ? Integer.valueOf(achievment.getAchievment()) : 0);
        }

        float pct = Math.round((totalAchievment * 100.0f)/target);
        return pct;
    }

    public Achievment getAchievmentByDate(String evaluationItemId, Date date) {
        String documentId = evaluationItemId+ACHIEVMENT_FILE_NAME;
        String fileContent = readFileContent(documentId);

        if (fileContent != null) {
            AchievmentData data = gson.fromJson(fileContent, AchievmentData.class);

            for (Achievment achievment : data.getAchievments() ) {
                if (achievment.getDate().compareTo(date) == 0) {
                    return achievment;
                }
            }
        }

        return null;
    }

    public List<Achievment> getListAchievment(String evaluationItemId) {
        String documentId = evaluationItemId+ACHIEVMENT_FILE_NAME;
        String fileContent = readFileContent(documentId);

        if (fileContent != null) {
            AchievmentData data = gson.fromJson(fileContent, AchievmentData.class);
            return data.getAchievments();
        }

        return new ArrayList<Achievment>();
    }

    public Map<String, Object> getAchievmentMap(String evaluationItemId) {
        List<Achievment> list = getListAchievment(evaluationItemId);
        Map<String, Object> map = new HashMap<String, Object>();
        for (Achievment achievment: list) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(achievment.getDate());
            String key = new StringBuilder().append(cal.get(Calendar.DAY_OF_MONTH)).append(cal.get(Calendar.MONTH)+1).append(cal.get(Calendar.YEAR)).toString();
            map.put(key, achievment);
        }

        return map;
    }

    public List<ReminderItemWrapper> getListReminder(String evaluationItemId) {
        List<ReminderItemWrapper> list = new ArrayList<ReminderItemWrapper>();
        String documentId = evaluationItemId+REMINDER_FILE_NAME;
        String fileContent = readFileContent(documentId);

        if (fileContent != null) {
            ReminderData data = gson.fromJson(fileContent, ReminderData.class);

            for (Reminder reminder: data.getReminders()) {
                ReminderItemWrapper wrapper = new ReminderItemWrapper();
                wrapper.setId(reminder.getId());
                wrapper.setEvaluationId(reminder.getEvaluationId());
                wrapper.setMessage(reminder.getMessage());
                wrapper.setTime(reminder.getTime());
                wrapper.setTone(reminder.getTone());
                wrapper.setToneURI(reminder.getToneUri());
                wrapper.setSoundEnable(reminder.getTone() != null ? true : false);
                wrapper.setRepeatSun(reminder.isRepeatSun());
                wrapper.setRepeatMon(reminder.isRepeatMon());
                wrapper.setRepeatTue(reminder.isRepeatTue());
                wrapper.setRepeatWed(reminder.isRepeatWed());
                wrapper.setRepeatThu(reminder.isRepeatThu());
                wrapper.setRepeatFri(reminder.isRepeatFri());
                wrapper.setRepeatSat(reminder.isRepeatSat());
                list.add(wrapper);
            }
        }

        return list;
    }



}
