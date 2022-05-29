package io.github.ningyu.jmeter.plugin.jsf.gui;

import io.github.ningyu.jmeter.plugin.jsf.sample.JsfSample;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: zengweipei
 * @CreateTime: 2021-10-19 15:09
 * @Description:
 */
public class JsfSampleGui extends AbstractSamplerGui {


    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final long serialVersionUID = -3248204995359935007L;
    private final JsfPanel panel;

    public JsfSampleGui() {
        super();
        panel = new JsfPanel();
        init();
    }

    /**
     * Initialize the interface layout and elements
     */
    private void init() {
        //所有设置panel，垂直布局
        JPanel settingPanel = new VerticalPanel(5, 0);
        settingPanel.setBorder(makeBorder());
        Container container = makeTitlePanel();
        settingPanel.add(container);
        //所有设置panel
        panel.drawPanel(settingPanel);
        //全局布局设置
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(settingPanel,BorderLayout.CENTER);
    }

    /**
     * component title/name
     */
    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    /**
     * this method sets the Sample's data into the gui
     */
    @Override
    public void configure(TestElement element) {
        super.configure(element);
        log.debug("sample赋值给gui");
        panel.configure(element);
        panel.bundleElement(element);
    }

    /**
     * Create a new sampler. And pass it to the modifyTestElement(TestElement) method.
     */
    @Override
    public TestElement createTestElement() {
        log.debug("创建sample对象");
        //创建sample对象
        JsfSample sample = new JsfSample();
        modifyTestElement(sample);
        return sample;
    }

    /**
     * this method sets the Gui's data into the sample
     */
    @SuppressWarnings("unchecked")
    @Override
    public void modifyTestElement(TestElement element) {
        log.debug("gui数据赋值给sample");
        //给sample赋值
        super.configureTestElement(element);
        panel.modifyTestElement(element);
        panel.bundleElement(element);
    }

    /**
     * sample's name
     */
    @Override
    public String getStaticLabel() {
        return "JSF Sample";
    }

    /**
     * clear gui's data
     */
    @Override
    public void clearGui() {
        log.debug("清空gui数据");
        super.clearGui();
        panel.clearGui();
    }

}
