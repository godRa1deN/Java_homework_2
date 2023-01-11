import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

class Handler {
    public static void getFinalFile(ArrayList<String> sortedList) throws IOException {
        StringBuilder finalFile = new StringBuilder();
        for (String s : sortedList) {
            String content = FileProcessing.readFile(s, StandardCharsets.UTF_8);
            finalFile.append(content);
        }
        FileOutputStream fos = new FileOutputStream(".\\src\\root\\answer.txt");
        fos.write(finalFile.toString().getBytes());
        fos.flush();
        fos.close();
    }
    public static void getAnswer() throws IOException {
        String pathToDir = "./src/root";                                                  //наша директория
        File dir = new File(pathToDir);
        ArrayList<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(dir.listFiles())));        //получаем все файлы из текущей директории
        DirectoryProcessing.AllFiles(files);                                                                //здесь возвращаем список всех файлов из директории и поддерикторий
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
        while(iter.hasNext()) {                                                            //выводим абсолютные пути к файлам
            String path = iter.next().getPath();
            try {
                content = FileProcessing.readFile(path, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("File is not found!");
            }
            allPaths.add(path);
            require = FileProcessing.findPath(content);
            for (String req : require) {
                FileProcessing.fileExist(path, req, allPaths);
                if (!req.equals("none")) {
                    FileProcessing.fileExist(path, req, allPaths);
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
            DirectoryProcessing.cyclesSearch(path, way, allPaths);
        }
        getFinalFile(sortedList);
    }
}

