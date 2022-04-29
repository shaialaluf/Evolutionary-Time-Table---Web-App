package XmlReader;

import CoreEvolution.SystemManager;

import ETTgenerated.*;
import exception.FileErrorException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SchemaBasedJAXB {
    public static SystemManager readFromXml(String fileName) {
        try {
            InputStream inputStream = new FileInputStream(new File(fileName));
            if(!getFileExtension(fileName).equals("xml")){
                throw new FileErrorException("The file you gave is not xml file");
            }
            ETTDescriptor descriptor = deserializeFrom(inputStream);
            SchemaBasedConvertor convertor=new SchemaBasedConvertor(descriptor);
           return convertor.createDB();////init our own descriptor and return it

        } catch (JAXBException e) {
            throw new FileErrorException("There was a problem with JAXB");
        } catch (FileNotFoundException e) {
            throw new  FileErrorException("The file in path you gave is not found");
        }
    }

    public static SystemManager readFromXml(InputStream inputStream) {
        try {

//            if(!getFileExtension(fileName).equals("xml")){
//                throw new FileErrorException("ERROR:The file you gave is not xml file",fileName);
//            }

            ETTDescriptor descriptor = deserializeFrom(inputStream);
            SchemaBasedConvertor convertor=new SchemaBasedConvertor(descriptor);
            return convertor.createDB();////init our own descriptor and return it

        } catch (JAXBException e) {
            throw new FileErrorException("There was a problem with JAXB");
        }
//        } catch (FileNotFoundException e) {
//            throw new  FileErrorException("ERROR:The file in path you gave is not found");
//        }
    }

    public static String getFileExtension(String fileName){
        int dot =fileName.lastIndexOf('.');
        return (dot==-1?"":fileName.substring(dot+1));
}
    public static ETTDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("ETTgenerated");
        Unmarshaller u = jc.createUnmarshaller();
        return (ETTDescriptor) u.unmarshal(in);
    }

}

