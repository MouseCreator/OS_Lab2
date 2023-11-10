package univ.lab.lib.parser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import univ.lab.lib.fill.FillableCreator;
import univ.lab.lib.fill.Filler;

import java.util.Stack;

public class SAXPaperHandler extends DefaultHandler {
    private Object result = null;
    private Object currentElement;
    private final Stack<Object> stack = new Stack<>();
    private FillableCreator creator;
    private Filler filler;
    private String bufferedTag;
    public void setCreator(FillableCreator creator) {
        this.creator = creator;
    }
    public void setFiller(Filler filler) {
        this.filler = filler;
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (isElementDeclaration(qName)) {
            processNewElement(qName, attributes);
            stack.push(currentElement);
        }

    }
    @Override
    public void characters(char[] chars, int start, int length) {
        bufferedTag = new String(chars, start, length);
    }
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (isElementDeclaration(qName)) {
            Object finishedElement = stack.pop();
            if (stack.empty()) {
                result = finishedElement;
                return;
            }
            currentElement = stack.peek();

            filler.fill(currentElement, qName, finishedElement);
            return;
        }
        filler.fill(currentElement, qName, bufferedTag);
    }

    private void processNewElement(String qName, Attributes attributes) {
        currentElement = createNewCurrent(qName);
        for (int i = 0; i < attributes.getLength(); i++) {
            String attributeName = attributes.getQName(i);
            String attributeValue = attributes.getValue(i);
            filler.fill(currentElement, attributeName, attributeValue);
        }
    }

    private Object createNewCurrent(String qName) {
        return creator.createNew(qName);
    }

    private boolean isElementDeclaration(String qName) {
        return creator.isElementDeclaration(qName);
    }

    public Object getResult() {
        return result;
    }
}
