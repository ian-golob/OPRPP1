package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class PrimListModel implements ListModel<Integer> {

    private final List<Integer> primeNumbers;

    private final List<ListDataListener> listeners;

    public PrimListModel(){
        primeNumbers = new ArrayList<>();
        listeners = new ArrayList<>();
        primeNumbers.add(1);
    }

    public void next(){
        int next = primeNumbers.get(primeNumbers.size()-1) + 1;
        while(true){
            boolean isPrime = true;

            for(int i = 2; i < next / 2; i++){
                if(next % i == 0){
                    isPrime = false;
                    break;
                }
            }

            if(isPrime){
                primeNumbers.add(next);
                break;
            }

            next++;
        }

        listeners.forEach(l -> l.intervalAdded(
                new ListDataEvent(
                        this,
                        ListDataEvent.INTERVAL_ADDED,
                        primeNumbers.size(),
                        primeNumbers.size()
                )));
    }

    @Override
    public int getSize() {
        return primeNumbers.size();
    }

    @Override
    public Integer getElementAt(int index) {
        return primeNumbers.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
