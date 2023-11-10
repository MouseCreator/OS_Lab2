package univ.lab.lib.parser;

import org.xml.sax.SAXException;
import univ.lab.lib.fill.FillableCreator;
import univ.lab.lib.fill.Filler;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class CustomSAXParser implements ReflectParser {

    private SAXParserFactory SAXFactory;

    private Filler filler;

    private FillableCreator fillableCreator;
    public CustomSAXParser() {}
    public CustomSAXParser(SAXParserFactory saxFactory, Filler filler, FillableCreator fillableCreator) {
        SAXFactory = saxFactory;
        this.filler = filler;
        this.fillableCreator = fillableCreator;
    }

    @Override
    public Object parse(String filename) {
        try {
            SAXParser saxParser = SAXFactory.newSAXParser();
            SAXPaperHandler saxPaperHandler = new SAXPaperHandler();
            saxPaperHandler.setCreator(fillableCreator);
            saxPaperHandler.setFiller(filler);
            saxParser.parse(filename, saxPaperHandler);
            return saxPaperHandler.getResult();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
