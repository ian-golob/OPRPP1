package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * A node representing a command which generates some textual output dynamically. It inherits
 * from Node class.
 *
 * @author Ian Golob
 */
public class EchoNode extends Node {

    private final Element[] elements;

    public EchoNode(Element[] elements) {
        this.elements = elements;
    }

    public Element[] getElements() {
        return elements;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("{$= ");

        for(Element element: elements){
            builder.append(element.asText());
            builder.append(' ');
        }

        builder.append("$}");

        return builder.toString();
    }

}
