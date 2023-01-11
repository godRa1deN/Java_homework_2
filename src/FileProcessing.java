import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class FileProcessing {
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
            ArrayList<String> requireList = new ArrayList<>();                                   //массив всех requare в файле
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
                fileContent = firstPart + secondPart;                                           //удаление requare
                requireList.add(newPath.toString());
            }
            return requireList;
        }
}
