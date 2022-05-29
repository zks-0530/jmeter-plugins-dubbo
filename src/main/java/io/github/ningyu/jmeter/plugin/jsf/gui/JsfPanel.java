package io.github.ningyu.jmeter.plugin.jsf.gui;

import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;

/**
 * @Author: zengweipei
 * @CreateTime: 2021-10-19 15:09
 * @Description:
 */
public class JsfPanel extends JsfDefaultPanel {

    @Override
    public void drawPanel(JPanel parent) {
        super.drawPanel(parent);
        parent.add(drawInterfaceSettingsPanel());
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        configureInterface(element);
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        modifyInterface(element);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        clearInterface();
    }
}
