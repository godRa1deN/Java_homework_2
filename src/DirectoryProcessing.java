import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Objects;

class DirectoryProcessing {
    public static void cyclesSearch(String path, ArrayList<String> way, ArrayList<String> files) throws IOException {
        String content = FileProcessing.readFile(path, StandardCharsets.UTF_8);
        ArrayList<String> require = FileProcessing.findPath(content);
        for (String s : require) {
            FileProcessing.fileExist(path, s, files);
            if (way.contains(s)) {
                throw new RuntimeException("Cycle found in " + path + " " + s);
            }
            way.add(s);
            cyclesSearch(s, way, files);
            way.remove(s);
        }
    }
    public static void AllFiles(ArrayList<File> files) {
        ListIterator<File> iter = files.listIterator();                                     //устанавливаем итератор на начало списка
        while(iter.hasNext()) {                                                             //идем до конца списка, пока будут файлы
            File file = iter.next();                                                        //сохраняем в переменную рассматриваемый файл или директорию
            if(file.isDirectory()) {                                                        //если файл - директория
                int count = iter.nextIndex();                                               //запоминаем положение в списке
                iter.remove();                                                              //даляем папку
                files.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));      //добавляем в конец списка все содержимое удаленной папки
                iter = files.listIterator(count);                                           //устанавливаем итератор на след элемент сразу после удаленной директории
            }
        }
    }
}
