import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.util.Timer;

public class RoundRobin {

    Map<String,Integer> users;
    String[][] tasks;
    Integer[][] complexity;

    Timer timer; TimerTask timerTask;
    int currentPeriod;
    int doneTasks;

    int CountUsers, CountTasks, CountEven;

    int minUsers; int maxUsers;
    int minTasks; int maxTasks;
    int minProductivity, maxProductivity;
    int minComplexity, maxComplexity;

    public RoundRobin(Integer[][] data)
    {
        minUsers = data[0][0]; maxUsers = data[0][1];
        minTasks = data[1][0]; maxTasks = data[1][1];
        minProductivity = data[2][0]; maxProductivity = data[2][1];
        minComplexity = data[3][0]; maxComplexity = data[3][1];

        CountUsers = new Random().nextInt((maxUsers - minUsers) + 1) + minUsers;
        CountTasks = new Random().nextInt((maxTasks - minTasks) + 1) + minTasks;
        CountEven = (int) Math.ceil((double) CountTasks / CountUsers);

        users = new LinkedHashMap<>();
        tasks = new String [CountUsers][CountEven];
        complexity = new Integer [CountUsers][CountEven];

        createUsers();
        createTasks();
    }

    public void startTimer(int period, DefaultListModel listModel1, DefaultListModel listModel2, DefaultListModel listModel3)
    {
        timer = new Timer();
        currentPeriod = 0;
        doneTasks = 0;

        timerTask = new TimerTask() {
            @Override
            public void run() {
                int i = 0; currentPeriod++;
                if (doneTasks == CountTasks)
                    timer.cancel();
                for (Map.Entry<String,Integer> pair: users.entrySet()) {
                    if (tasks[i][0] != null) {
                        complexity[i][0] -= pair.getValue();
                        listModel1.remove(i); listModel1.add(i,tasks[i][0] + "   " + complexity[i][0]);
                        if (complexity[i][0] <= 0) {
                            listModel3.add(0, pair.getKey() + "  " + tasks[i][0] + "            " + currentPeriod);
                            doneTasks++;
                            addCurrentTask(i); updateCurrentTasks();
                            getCurrentTasks(listModel1);
                        }
                    }
                    i++;
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, period*1000);
    }

    private void addCurrentTask(int index){
        for (int i = 0; i < CountEven - 1; i++) {
            tasks[index][i] = tasks[index][i+1];
            complexity[index][i] = complexity[index][i+1];
        }
        tasks[index][CountEven - 1] = null; complexity[index][CountEven - 1] = 0;
    }

    private void updateCurrentTasks(){
        String tempS = tasks[0][0]; int tempC = complexity[0][0];
        for (int i = 0; i < CountUsers - 1; i++) {
            tasks[i][0] = tasks[i+1][0];
            complexity[i][0] = complexity[i+1][0];
        }
        tasks[CountUsers-1][0] = tempS; complexity[CountUsers-1][0] = tempC;

        if (CountTasks-doneTasks >= CountUsers)
            CheckNull();
    }

    private void CheckNull(){
        int index = -1;
        for (int i = 0; i < CountUsers; i++) {
            if (tasks[i][0] == null)
                index = i;
        }

        if (index != -1)
            addCurrentTask(index);
    }

    private void createUsers()
    {
        for (int i = 0; i < CountUsers; i++) {
            int productivity = new Random().nextInt((maxProductivity - minProductivity) + 1) + minProductivity;
            users.put("User" + i, productivity);
        }
    }

    private void createTasks()
    {
        int c = 0;
        for (int i = 0; i < CountUsers; i++) {
            for (int j = 0; j < CountEven; j++) {
                if (c < CountTasks) {
                    tasks[i][j] = "Task" + c; c++;
                }
            }
        }

        for (int i = 0; i < CountUsers; i++) {
            for (int j = 0; j < CountEven; j++) {
                complexity[i][j] = new Random().nextInt((maxComplexity - minComplexity)+ 1) + minComplexity;
            }
        }

    }

    public void getUsers(DefaultListModel listModel) {
        int d = 0;
        for (Map.Entry<String,Integer> pair: users.entrySet()) {
            String s = pair.getKey() + "  " + pair.getValue();
            listModel.add(d++,s);
        }
    }

    public void getCurrentTasks(DefaultListModel listModel) {
        listModel.clear();
        int d = 0;
        for (int i = 0; i < CountUsers; i++) {
            if (tasks[i][0] != null) {
                String s = tasks[i][0] + "     " + complexity[i][0];
                listModel.add(d++, s);
            }
        }
    }

    public void getAllTasks(DefaultListModel listModel, int i) {
        listModel.clear();

        int d = 0;
        for (int j = 0; j < CountEven; j++) {
            if (tasks[i][j] != null) {
                String s = tasks[i][j] + "     " + complexity[i][j];
                listModel.add(d++, s);
            }
        }
    }

}
