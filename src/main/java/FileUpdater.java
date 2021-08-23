import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class FileUpdater {
    private File f;
    private TaskList lst;

    FileUpdater(File f, TaskList lst) throws FileNotFoundException {
        this.f = f;
        this.lst = lst;

        if (!f.exists()) {
            throw new FileNotFoundException("file does not exist!");
        }

    }


    public File getFile(){
        return this.f;
    }

    public void updateListFile(){
        String s ="";
        for(int i = 0; i < lst.size(); i++){
            s = s + lst.get(i).toStringConvert() + "\n";
        }
        try {
            FileWriter fw = new FileWriter(this.f);
            fw.write(s);
            fw.close();
        } catch (IOException e){
            System.out.println("error: " + e.getMessage());
        }
    }

    public void updateLine(String s){
        String fs = "";
        boolean isCompleted = false;
        if(findFirstSection(cut(s)).equals("1")) isCompleted = true;

        if(s.charAt(0) == 'T'){
            fs = "todo " + (cut(cut(s)));
            lst.addTodo(fs, isCompleted);
        } else if(s.charAt(0) == 'E'){
            fs = "event " + findFirstSection(cut(cut(s))) + " /at " + (cut(cut(cut(s))));
            lst.addEvent(fs, isCompleted);
        } else if(s.charAt(0) == 'D'){
            fs = "deadline " + findFirstSection(cut(cut(s))) + " /by " + (cut(cut(cut(s))));
            lst.addDeadline(fs, isCompleted);
        }
    }

    public void showError(){
        System.out.println("file not found!");
    }

    public void load(){
        try {
            Scanner sc = new Scanner(f);
            while (sc.hasNext()) {
                String line = sc.nextLine(); //scanning file's first input
                this.updateLine(line); // read each line, convert the line into a command and input it to Duke
            }
        } catch (FileNotFoundException e){
            showError();
        }
    }


    public String cut(String s) {
        return s.substring(s.indexOf('|') + 2);
    }

    public String findFirstSection(String s){
        return s.substring(0, s.indexOf('|') - 1);
    }


    public static void main(String[] args) {
        try {
            File f = new File("data/Duke.txt");
            TaskList tl = new TaskList();
            FileUpdater fu = new FileUpdater(f, tl);
            fu.load();
            fu.updateListFile();
            System.out.println(tl.size());
        } catch (FileNotFoundException e){
            System.out.println("ohno");
        }
    }

}