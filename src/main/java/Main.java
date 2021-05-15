import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> emps = parseCSV(columnMapping, fileName);
        String employs = listToJson(emps);
        writeString(employs);
    }

    // Получаем список сотрудников:
    public static List<Employee> parseCSV(String[] columnMapping, String fileName) throws Exception {
        File csvFile = new File(fileName);
        if (csvFile.exists()) {
            try (CSVReader reader = new CSVReader(new FileReader(csvFile))){

                /** ColumnPositionMappingStrategy определяет класс, к которому будут привязывать
                данные из CSV документа, а также порядок расположения полей в этом документе */

                ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
                strategy.setType(Employee.class);
                strategy.setColumnMapping(columnMapping);

                /** CsvToBean создает инструмент для взаимодействия CSV документа и выбранной ранее стратегии
                 * CsvToBean позволяет распарсить CSV файл в список объектов */

                CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                        .withMappingStrategy(strategy)
                        .build();
                return csvToBean.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new FileNotFoundException("File now found");
        }
        return null;
    }

    /** Полученный список преобразуем в строчку в формате JSON - Конвертируем объект созданного класса в JSON при помощи метода toJson()  */
    public static String listToJson(List<Employee> object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(object, listType);
    }

    /** Далее запишем полученный JSON в файл с помощью метода writeString() */
    public static void writeString(String employee){
        try (FileWriter file = new FileWriter("data.json")){
            file.write(employee.toString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


