import java.io.*;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Main {
    public static void fileExist(String path, String require, ArrayList<String> files) {
        if (!files.contains(require)) {
            throw new RuntimeException("Wrong require in " + path);
        }
    }
    public static String readFile(String path, Charset encoding) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path), encoding);
        return String.join(System.lineSeparator(), lines);
    }
    public static ArrayList<String> findPath(String fileContent) {
        ArrayList<String> requireList = new ArrayList<>();                              //массив всех requare в файле
        while (fileContent.contains("require")) {
            int requireIndex = fileContent.indexOf("require");
            int temp = requireIndex;
            requireIndex += 8;
            char requireEnd = '\'';
            StringBuilder newPath = new StringBuilder();
            while (fileContent.charAt(requireIndex) != requireEnd) {
                newPath.append(fileContent.charAt(requireIndex));                            //newPath = содержимое requare
                requireIndex++;
            }
            String firstPart = fileContent.substring(0, temp);
            String secondPart = fileContent.substring(requireIndex + 1);
            fileContent = firstPart + secondPart;                                       //удаление requare
            requireList.add(newPath.toString());
        }
        return requireList;
    }

    public static void AllFiles(ArrayList<File> files) {
        ListIterator<File> iter = files.listIterator();                                     //устанавливаем итератор на начало списка
        while(iter.hasNext()) {                                                             //идем до конца списка, пока будут файлы
            File file = iter.next();                                                        //сохраняем в переменную рассматриваемый файл или директорию
            if(file.isDirectory()) {                                                        //если файл - директория
                int count = iter.nextIndex();                                               //запоминаем положение в списке
                iter.remove();                                                              //даляем папку
                files.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));                              //добавляем в конец списка все содержимое удаленной папки
                iter = files.listIterator(count);                                           //устанавливаем итератор на след элемент сразу после удаленной директории
            }
        }
    }
    public static void getFinalFile(ArrayList<String> sortedList) throws IOException {
        StringBuilder finalFile = new StringBuilder();
        for (String s : sortedList) {
            String content = readFile(s, StandardCharsets.UTF_8);
            finalFile.append(content);
        }
        FileOutputStream fos = new FileOutputStream(".\\src\\root\\answer.txt");
        fos.write(finalFile.toString().getBytes());
        fos.flush();
        fos.close();
    }

    public static void cyclesSearch(String path, ArrayList<String> way, ArrayList<String> files) throws IOException {
        String content = readFile(path, StandardCharsets.UTF_8);
        ArrayList<String> require = findPath(content);
        for (String s : require) {
            fileExist(path, s, files);
            if (way.contains(s)) {
                throw new RuntimeException("Cycle found in " + path + " " + s);
            }
            way.add(s);
            cyclesSearch(s, way, files);
            way.remove(s);
        }
    }

    public static void main(String[] args) throws IOException {
        String pathToDir = "./src/root";                                                  //наша директория
        File dir = new File(pathToDir);

        ArrayList<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(dir.listFiles())));        //получаем все файлы из текущей директории

        AllFiles(files);                                                                //здесь возвращаем список всех файлов из директории и поддерикторий

        Iterator<File> iter = files.iterator();
        String content;
        ArrayList<String> sortedList = new ArrayList<>();
        ArrayList<String> allPaths = new ArrayList<>();
        ArrayList<String> require;
        ArrayList<String> way = new ArrayList<>();
        while (iter.hasNext()) {                                                            //выводим абсолютные пути к файлам
            String path = iter.next().getPath();
            allPaths.add(path);
        }
        iter = files.iterator();
        System.out.println(allPaths);
        while(iter.hasNext()) {                                                            //выводим абсолютные пути к файлам
            String path = iter.next().getPath();
            try {
                content = readFile(path, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("File is not found!");
            }
            allPaths.add(path);
            require = findPath(content);
            System.out.println(require);
            for (String req : require) {
                fileExist(path, req, allPaths);
                if (!req.equals("none")) {
                    fileExist(path, req, allPaths);
                    if (sortedList.contains(req) && sortedList.contains(path)) {           //если req < path то swap
                        if (sortedList.indexOf(req) > sortedList.indexOf(path)) {
                            Collections.swap(sortedList, sortedList.indexOf(req), sortedList.indexOf(path));
                        }
                    }
                    if (!sortedList.contains(req)) {
                        sortedList.add(0, req);
                    }
                    if (!sortedList.contains(path)) {
                        sortedList.add(sortedList.size(), path);
                    }
                }
            }
            if (require.isEmpty()) {
                if (!sortedList.contains(path)) {
                    sortedList.add(0, path);
                } else {
                    sortedList.remove(path);
                    sortedList.add(0, path);
                }
            }
            way.clear();
            way.add(path);
            cyclesSearch(path, way, allPaths);
        }
        System.out.println(sortedList);
        getFinalFile(sortedList);
    }
}
