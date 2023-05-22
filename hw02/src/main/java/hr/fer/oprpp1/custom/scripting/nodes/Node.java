package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.List;
import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * Base class for all graph nodes.
 *
 * @author Ian Golob
 */
public class Node {

    private List children;

    /**
     * Adds the given child to an internally managed collection of children.
     * Null values are not allowed.
     * @param child Child to add to the node.
     * @throws NullPointerException If the given child is null.
     */
    public void addChildNode(Node child){
        if(child == null){
            throw new NullPointerException();
        }

        if(children == null){
            children = new ArrayIndexedCollection();
        }

        children.add(child);
    }

    /**
     * Returns the number of (direct) children.
     * @return The number of children.
     */
    public int numberOfChildren(){
        if(children == null){
            return 0;
        }

        return children.size();
    }

    /**
     * Returns the child at the given index.
     * @param index Given index.
     * @return Child at the given index.
     * @throws IndexOutOfBoundsException If the index is out of bounds (less than 0 or more than size-1).
     */
    public Node getChild(int index){
        if(children == null || index < 0 || index >= children.size()){
            throw new IndexOutOfBoundsException();
        }

        return (Node) children.get(index);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        children.forEach(child -> {
            if(child instanceof Element){
                builder.append(((Element) child).asText());
            }else{
                builder.append(child.toString());
            }
        });

        return builder.toString();
    }

}
