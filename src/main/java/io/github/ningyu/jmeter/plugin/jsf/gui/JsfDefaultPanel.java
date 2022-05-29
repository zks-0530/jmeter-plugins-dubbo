package io.github.ningyu.jmeter.plugin.jsf.gui;

import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;

/**
 * @Author: zengweipei
 * @CreateTime: 2021-10-19 15:10
 * @Description:
 */
public class JsfDefaultPanel extends JsfCommonPanel {

    public void drawPanel(JPanel parent) {
        parent.add(drawRegistrySettingsPanel());
        parent.add(drawProtocolSettingsPanel());
        parent.add(drawConsumerSettingsPanel());
    }

    public void configure(TestElement element) {
        configureRegistry(element);
        configureProtocol(element);
        configureConsumer(element);
    }

    public void modifyTestElement(TestElement element) {
        modifyRegistry(element);
        modifyProtocol(element);
        modifyConsumer(element);
    }

    public void clearGui() {
        clearRegistry();
        clearProtocol();
        clearConsumer();
    }
}
