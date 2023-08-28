package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static List<Employee> parseCSV(String[] map, String fileName ){
        try (CSVReader csvReader= new CSVReader(new FileReader(fileName))){
            ColumnPositionMappingStrategy <Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(map);
            CsvToBean <Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            List <Employee> staff = csv.parse();
//            staff.forEach(System.out::println);
            return staff;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static <T> String listToJson(List <T> list){

        Type listType = new TypeToken<List<T>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
//        System.out.println(gson.toJson(list));
        String json = gson.toJson(list, listType);
        return json;

    }
    public static void writeString(String adress, String json){
        try (FileWriter file = new
                FileWriter(adress)) {
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Employee> parseXML(String adress) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(adress));
        Node root = doc.getDocumentElement();
        System.out.println("Корневой элемент"+root.getNodeName());
        NodeList nodeList = doc.getElementsByTagName("employee");
        List <Employee> empList = new ArrayList<>();
        for (int i =0;i< nodeList.getLength();i++){
            empList.add(getEmployee(nodeList.item(i)) );
        }
//        for (Employee emp : empList)
//        {
//            System.out.println(emp.toString());
//        }
//        read(root);
        return empList;
    }
            public static Employee getEmployee(Node node){
        Employee emp = new Employee();
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    emp.setId((Integer.parseInt(getTagValue("id", element))));
                    emp.setFirstName((getTagValue("firstName", element)));
                    emp.setLastName((getTagValue("lastName", element)));
                    emp.setCountry((getTagValue("country", element)));
                    emp.setAge((Integer.parseInt(getTagValue("age", element))));
                }

                return emp;
            }
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
//            public static void read(Node node){
//                NodeList nodeList = node.getChildNodes();
//                for (int i = 0; i < nodeList.getLength(); i++) {
//                    Node node_ = nodeList.item(i);
//                    if (node_.getNodeType() != Node.TEXT_NODE) {
//                        System. out.println( "Текущий узел: " + node_.getNodeName());
////                        Element element = (Element) node_;
//
//                        NamedNodeMap map = element.getAttributes();
//                        for (int a = 0; a <  map.getLength(); a++) {
//                            String attrName = map.item(a).getNodeName();
//                            String attrValue = map.item(a).getNodeValue();
//                            System. out.println( "Атрибут: " + attrName + "; значение: " + attrValue);
//                        }
//                        read(node_);
//                    }
//                }}




    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List <Employee> list = parseCSV( columnMapping, fileName);
        String json = listToJson(list);
        String adress = "data.json";
        writeString(adress, json);
        String fileName2 = "data.xml";

        List <Employee> list2 = parseXML(fileName2);
        System.out.println(list2);
        String json2 = listToJson(list2);
        String adress2 = "data2.json";
        writeString(adress2, json2);



    }
}