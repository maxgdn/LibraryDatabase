package sample;

import sample.models.SignIn;
import sample.models.Student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by Max on 5/12/2016.
 */
public class DBToFileWriter {

    private static LocalDate currentTime = LocalDate.now();
    private static String folderName = "C:\\Users\\user\\textfilearea\\" + currentTime;

    public static void initFolder(){
         new File(folderName).mkdir();
    }
    public static void writeSignIns() throws IOException {
        Counter counter = new Counter();

        String fileName = folderName +"\\" + "Sign In's on " + currentTime + ".txt";

        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        BufferedWriter writerBuffer = new BufferedWriter(writer);

        List<SignIn> all = SignIn.find.all();
        try {
            writerBuffer.write("Entries for " + currentTime + "\n");
            writerBuffer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        all.stream().forEach(signIn -> {
            try {
                counter.increment();
                if(getEntryDate(String.valueOf(signIn.getTimeOut())).equals(currentTime.toString())){
                    writerBuffer.write("#" + counter.toString() + " Student: " +
                            formatStudentID(String.valueOf(signIn.getStudent()))
                            + " Time In: " + formatDisplayedTime(String.valueOf(signIn.getTimeIn()))
                            + " Time Out: " + formatDisplayedTime(String.valueOf(signIn.getTimeOut()))
                            + " Manual: " + String.valueOf(signIn.isWasManual())
                            + "  Name: " +  signIn.getStudent().getFirstName()+"," + signIn.getStudent().getLastName() + "\n");

                    writerBuffer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writerBuffer.close();
    }

    public static void writeStudents() throws IOException {

        String fileName = folderName + "\\" + "Students on " + currentTime + ".txt";

        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        BufferedWriter writerBuffer = new BufferedWriter(writer);

        List<Student> students = Student.find.all();

        students.stream().forEach(student1 -> {
            try {
                writerBuffer.write(student1.getFirstName() + ", " + student1.getLastName()
                        + " ID: " + student1.getStudentID() + " Amount of Logins: " + student1.signIns.size()
                        + " Specific ID: Student@" + student1.id);
                writerBuffer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writerBuffer.close();
    }

    public static void terminateAllLiveClients() {
        List<SignIn> all = SignIn.find.all();
        all.stream().forEach(signIn3 -> {
            if (signIn3.getTimeOut() == null) {
                signIn3.setTimeOut(LocalDateTime.now());
                signIn3.setWasManual(true);
                signIn3.save();
            }
        });
    }

    public static String formatStudentID(String string) {
        string = string.substring(14, 23);
        return string;
    }

    public static String formatDisplayedTime(String string) {
                StringBuilder sb;
                String[] array = string.split("T");
                String s1 = array[0];
                String s2 = array[1];
                sb = new StringBuilder(s1);
                s2 = s2.substring(0, 8);
                string = String.valueOf(sb.append(" " + militaryToOrdinaryTime(s2)));
                return string;
    }

    public static String getEntryDate(String string) {
        String[] array = string.split("T");
        String s1 = array[0];
        return s1;
    }

    public static String militaryToOrdinaryTime(String time) {
        String newTime = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
            Date timeToChange = formatter.parse(time);
            newTime = formatter.format(timeToChange);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newTime;
    }
}
// I'm including this to remind myself that sometimes when you program you can always do better as formatStudentID(String.valueOf(student1.signIns.stream().map(signIn -> {return signIn.getStudent().toString();}).collect(Collectors.joining(", ")))));
// was just replaced with student.id      the irony lol .................