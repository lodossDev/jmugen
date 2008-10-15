/*
 * Copyright (c) 2006 Romain Guy <romain.guy@mac.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package composite;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author gfx
 */
public class CompositesTestFrame extends JFrame {
    private CompositeTestPanel compositeTestPanel;
    private JComboBox combo;
    private JSlider slider;

    /** Creates a new instance of CompositesTestFrame */
    public CompositesTestFrame() {
        super("Composites");

        compositeTestPanel = new CompositeTestPanel();
        compositeTestPanel.setComposite(BlendComposite.Normal);
        add(compositeTestPanel);

        combo = new JComboBox(BlendComposite.BlendingMode.values());
        combo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compositeTestPanel.setComposite(
                    BlendComposite.getInstance(
                        BlendComposite.BlendingMode.valueOf(combo.getSelectedItem().toString()),
                        slider.getValue() / 100.0f
                    ));
            }
        });

        slider = new JSlider(0, 100, 100);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                BlendComposite blend = (BlendComposite) compositeTestPanel.getComposite();
                blend = blend.derive(slider.getValue() / 100.0f);
                compositeTestPanel.setComposite(blend);
            }
        });

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(combo);
        controls.add(new JLabel("0%"));
        controls.add(slider);
        controls.add(new JLabel("100%"));
        add(controls, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CompositesTestFrame().setVisible(true);
            }
        });
    }
}
