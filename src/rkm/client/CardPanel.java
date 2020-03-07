package rkm.client;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A more friendly interface for a panel with a CardLayout.
 */
final class CardPanel
        extends JPanel
{

    private static final long serialVersionUID = 3729490608803418418L;

    private int count;

    private Map<JComponent,Integer> names = new HashMap<JComponent,Integer>();

    private final CardLayout layout = new CardLayout();

    CardPanel() {
        this.setLayout(layout);
    }

    void add(JComponent card) {
        count++;
        names.put(card, count);
        super.add(card,Integer.toString(count));
    }

    void show(JComponent card) {
        int number = names.get(card);
        String name = Integer.toString(number);
        layout.show(this, name);
    }

}
