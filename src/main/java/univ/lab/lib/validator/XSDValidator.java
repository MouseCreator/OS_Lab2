package univ.lab.lib.validator;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XSDValidator {
    public boolean isValid(String xsd, String xml) {
        try {
           validate(xsd, xml);
        } catch (SAXException | IOException e) {
            return false;
        }
        return true;
    }

    public void validate(String xsd, String xml) throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(xsd));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new File(xml)));
    }
}
