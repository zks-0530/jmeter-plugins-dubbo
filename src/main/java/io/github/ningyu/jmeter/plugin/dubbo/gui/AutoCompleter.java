/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ningyu.jmeter.plugin.dubbo.gui;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;

/**
 * AutoCompleter  自动完成
 *
 *  KeyListener 解释
 * The listener interface for receiving keyboard events (keystrokes). The class that is interested in processing a keyboard event either implements this interface (and all the methods it contains) or extends the abstract KeyAdapter class (overriding only the methods of interest).
 * The listener object created from that class is then registered with a component using the component's addKeyListener method. A keyboard event is generated when a key is pressed, released, or typed. The relevant method in the listener object is then invoked, and the KeyEvent is passed to it.
 * 用于接收键盘事件(击键)的侦听器接口。 对处理键盘事件感兴趣的类要么实现这个接口(以及它包含的所有方法)，要么扩展抽象的KeyAdapter类(只覆盖感兴趣的方法)。
 * 然后，使用组件的addKeyListener方法将从该类创建的侦听器对象注册到组件中。 当一个键被按下、释放或键入时，会生成一个键盘事件。 然后调用侦听器对象中的相关方法，并将KeyEvent传递给该方法。
 *
 *
 */
public class AutoCompleter implements KeyListener {

    private JComboBox owner = null;
    private JTextField editor = null;

    private ComboBoxModel model = null;

    public AutoCompleter(JComboBox comboBox) {
        owner = comboBox;
//        JComboBox  组合了按钮或可编辑字段和下拉列表的组件。用户可以从下拉列表中选择一个值，该列表会根据用户的请求出现。如果您使组合框可编辑，那么组合框包含一个可编辑字段，用户可以在其中键入值。
//        getEditorComponent（） 返回应该添加到此编辑器的树层次结构中的组件
//        getEditor（） 返回用于在JComboBox字段中绘制和编辑所选项目的编辑器，返回:显示选定项的ComboBoxEditor
        editor = (JTextField) comboBox.getEditor().getEditorComponent();
        editor.addKeyListener(this);
        model = comboBox.getModel();
        // owner.addItemListener(this);
    }

    public AutoCompleter(JComboBox comboBox, ItemListener itemListener) {
        owner = comboBox;
        editor = (JTextField) comboBox.getEditor().getEditorComponent();
        editor.addKeyListener(this);
        model = comboBox.getModel();
        owner.addItemListener(itemListener);
    }
//    键盘点击事件
    public void keyTyped(KeyEvent e) {
    }
//     键盘点击事件
    public void keyPressed(KeyEvent e) {
//        char ch = e.getKeyChar();
//        if (ch == KeyEvent.VK_ENTER) {
//            return;
//        }
//        editor.setText("");
    }

//通过在监听器-a和监听器-b上调用keyReleased方法来处理keyReleased事件。
//参数:e -关键事件
    public void keyReleased(KeyEvent e) {
        char ch = e.getKeyChar();
        if (ch == KeyEvent.VK_ENTER) {
            int caretPosition = editor.getCaretPosition();
            String str = editor.getText();
            if (str.length() == 0)
                return;
            autoComplete(str, caretPosition);
        }
//        if (ch == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch)
//                || ch == KeyEvent.VK_DELETE)
//            return;
    }

    /**
     * 自动完成。根据输入的内容，在列表中找到相似的项目.
     */
    protected void autoComplete(String strf, int caretPosition) {
        Object[] opts;
        opts = getMatchingOptions(strf.substring(0, caretPosition));
        if (owner != null) {
            model = new DefaultComboBoxModel(opts);
            owner.setModel(model);
        }
        if (opts.length > 0) {
            String str = opts[0].toString();
            // editor.setCaretPosition(caretPosition);
            if (owner != null) {
                try {
                    owner.showPopup();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 找到相似的项目, 并且将之排列到数组的最前面。
     *
     * @param str
     * @return 返回所有项目的列表。
     */
    protected Object[] getMatchingOptions(String str) {
        List v = new Vector();
        List v1 = new Vector();
        model = owner.getModel();
        for (int k = 0; k < model.getSize(); k++) {
            Object itemObj = model.getElementAt(k);
            if (itemObj != null) {
                String item = itemObj.toString();
                if (item.toUpperCase().indexOf(str.toUpperCase()) != -1)
                    v.add(model.getElementAt(k));
                else
                    v1.add(model.getElementAt(k));
            } else
                v1.add(model.getElementAt(k));
        }
        for (int i = 0; i < v1.size(); i++) {
            v.add(v1.get(i));
        }
        if (v.isEmpty())
            v.add(str);
        return v.toArray();
    }
}
