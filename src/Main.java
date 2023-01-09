import java.io.*;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Main {
    public static boolean fileExsist(String path, String requare, ArrayList<String> files) {
        if (files.contains(requare)) {
            return true;
        } else {
            throw new RuntimeException("Wrong requare in " + path);
        }
    }
    public static String readFile(String path, Charset encoding) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path), encoding);
        return String.join(System.lineSeparator(), lines);
    }
    public static String findPath (String fileContent, String filePath) {
        if (fileContent.contains("requare")) {
            System.out.println("requare exsist" + " in " + filePath);
            int requareIndex = fileContent.indexOf("requare");
            requareIndex += 8;
            char requareEnd = '\'';
            String newPath = "";
            while (fileContent.charAt(requareIndex) != requareEnd) {
                newPath += fileContent.charAt(requareIndex);
                requareIndex++;
            }
            System.out.println(newPath);
            return newPath;
        } else {
            throw new RuntimeException("requare is not found" + " in " + filePath);
        }
    }

    public static ArrayList<File> AllFiles(ArrayList<File> files) {

        ListIterator<File> iter = files.listIterator();                                     //устанавливаем итератор на начало списка

        while(iter.hasNext()) {                                                             //идем до конца списка, пока будут файлы
            File file = iter.next();                                                        //сохраняем в переменную рассматриваемый файл или директорию
            if(file.isDirectory()) {                                                        //если файл - директория
                int count = iter.nextIndex();                                               //запоминаем положение в списке
                iter.remove();                                                              //даляем папку
                files.addAll(Arrays.asList(file.listFiles()));                              //добавляем в конец списка все содержимое удаленной папки
                iter = files.listIterator(count);                                           //устанавливаем итератор на след элемент сразу после удаленной директории
            }
        }

        return files;
    }

    public static void main(String[] args) {
        String pathToDir = "./src/root";                                                  //наша директория
        File dir = new File(pathToDir);

        ArrayList<File> files = new ArrayList<File>(Arrays.asList(dir.listFiles()));        //получаем все файлы из текущей директории

        files = AllFiles(files);                                                            //здесь возвращаем список всех файлов из директории и поддерикторий

        Iterator<File> iter = files.iterator();
        String content = "";
        String requare = "";
        List<String> list = new ArrayList<String>();
        ArrayList<String> allPathes = new ArrayList<String>();
        while(iter.hasNext()) {                                                            //выводим абсолютные пути к файлам
            String path = iter.next().getPath();
            allPathes.add(path);
        }
        iter = files.iterator();
        System.out.println(allPathes);
        while(iter.hasNext()) {                                                            //выводим абсолютные пути к файлам
            String path = iter.next().getPath();
            try {
                content = readFile(path, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("File is not found!");
            }
            allPathes.add(path);
            requare = findPath(content, path);
            fileExsist(path, requare, allPathes);
            //list.add(0, requare);
        }
        //System.out.println(list);
    }
}
