package io.github.ningyu.jmeter.plugin.jsf.gui;

import io.github.ningyu.jmeter.plugin.dubbo.gui.JAutoCompleteComboBox;
import io.github.ningyu.jmeter.plugin.dubbo.sample.MethodArgument;
import io.github.ningyu.jmeter.plugin.dubbo.sample.ProviderService;
import io.github.ningyu.jmeter.plugin.jsf.sample.JsfProviderService;
import io.github.ningyu.jmeter.plugin.util.Constants;
import io.github.ningyu.jmeter.plugin.util.StringUtils;
import org.apache.dubbo.common.URL;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * @Author: zengweipei
 * @CreateTime: 2021-10-19 15:11
 * @Description:
 */
public class JsfCommonPanel {

        /** Registry */
        private JComboBox<String> registryProtocolText;
        private JTextField addressText;
        private JTextField registryGroupText;
        private JTextField registryUserNameText;
        private JTextField registryPasswordText;
        private JTextField registryTimeoutText;

        /** Rpc Protocol */
        private JComboBox<String> rpcProtocolText;
        /** Consumer & Service */
        private JTextField timeoutText;
        private JTextField versionText;
        private JTextField retriesText;
        private JTextField clusterText;
        private JTextField groupText;
        private JTextField connectionsText;
        private JComboBox<String> loadbalanceText;
        private JComboBox<String> asyncText;
        /** Interface */
        private JTextField interfaceText;
        private JTextField methodText;
        /** Method Args Table */
        private DefaultTableModel model;
        private String[] columnNames = {"paramType", "paramValue"};
        private String[] tmpRow = {"", ""};
        /** Attachment Table */
        private DefaultTableModel modelAttachment;
        private String[] columnNamesAttachment = {"key", "value"};
        private int textColumns = 2;
        private JAutoCompleteComboBox<String> interfaceList;
        private JAutoCompleteComboBox<String> methodList;
        private TestElement element;

        public void bundleElement(TestElement element) {
            this.element = element;
        }

        public JPanel drawRegistrySettingsPanel() {
            //Registry Settings
            JPanel registrySettings = new VerticalPanel();
            registrySettings.setBorder(BorderFactory.createTitledBorder("Registry Center"));
            //Protocol
            JPanel ph = new HorizontalPanel();
            JLabel protocolLable = new JLabel("Protocol:", SwingConstants.RIGHT);
            registryProtocolText = new JComboBox<String>(new String[]{"jsfRegistry"});
            registryProtocolText.setToolTipText("\"none\" is direct connection");
            protocolLable.setLabelFor(registryProtocolText);
            ph.add(protocolLable);
            ph.add(registryProtocolText);
            ph.add(makeHelper("Registry center address protocol, The 'none' is direct connection. "));
            //Group
            JLabel registryGroupLable = new JLabel("Group:", SwingConstants.RIGHT);
            registryGroupText = new JTextField();
            registryGroupLable.setLabelFor(registryGroupText);
            ph.add(registryGroupLable);
            ph.add(registryGroupText);
            ph.add(makeHelper("Service registration grouping, cross-group services will not affect each other, and can not be called each other, suitable for environmental isolation."));
            //UserName
            JLabel registryUserNameLable = new JLabel("UserName:", SwingConstants.RIGHT);
            registryUserNameText = new JTextField();
            registryUserNameLable.setLabelFor(registryUserNameText);
            ph.add(registryUserNameLable);
            ph.add(registryUserNameText);
            ph.add(makeHelper("The usename of the registry. Do not set it if the registry doesn't need validation."));
            //Password
            JLabel registryPasswordLable = new JLabel("Password:", SwingConstants.RIGHT);
            registryPasswordText = new JTextField();
            registryPasswordLable.setLabelFor(registryPasswordText);
            ph.add(registryPasswordLable);
            ph.add(registryPasswordText);
            ph.add(makeHelper("The password of the registry. Do not set it if the registry doesn't need validation."));
            registrySettings.add(ph);
            //Address
            JPanel ah = new HorizontalPanel();
            JLabel addressLable = new JLabel("Address:", SwingConstants.RIGHT);
            addressText = new JTextField(textColumns);
            addressLable.setLabelFor(addressText);
            ah.add(addressLable);
            ah.add(addressText);
            ah.add(makeHelper("Use the registry to allow multiple addresses, Use direct connection to allow only one address! Multiple address format: ip1:port1,ip2:port2 . Direct address format: ip:port . "));
            //Timeout
            JLabel registryTimeoutLable = new JLabel("Timeout:", SwingConstants.RIGHT);
            registryTimeoutText = new JTextField();
            registryTimeoutLable.setLabelFor(registryTimeoutText);
            ah.add(registryTimeoutLable);
            ah.add(registryTimeoutText);
            ah.add(makeHelper("The timeout(ms) of the request to registry."));
            registrySettings.add(ah);
            return registrySettings;
        }


        public JPanel drawProtocolSettingsPanel() {
            //RPC Protocol Settings
            JPanel protocolSettings = new VerticalPanel();
            protocolSettings.setBorder(BorderFactory.createTitledBorder("RPC Protocol"));
            //RPC Protocol
            JPanel rpcPh = new HorizontalPanel();
            JLabel rpcProtocolLable = new JLabel("Protocol:", SwingConstants.RIGHT);
            rpcProtocolText = new JComboBox<String>(new String[]{"jsf://"});
            rpcProtocolLable.setLabelFor(rpcProtocolText);
            rpcPh.add(rpcProtocolLable);
            rpcPh.add(rpcProtocolText);
            rpcPh.add(makeHelper("RPC protocol name."));
            protocolSettings.add(rpcPh);
            return protocolSettings;
        }

        public JPanel drawConsumerSettingsPanel() {
            //Consumer Settings
            JPanel consumerSettings = new VerticalPanel();
            consumerSettings.setBorder(BorderFactory.createTitledBorder("Consumer & Service"));
            JPanel h = new HorizontalPanel();
            //Timeout
            JLabel timeoutLable = new JLabel(" Timeout:", SwingConstants.RIGHT);
            timeoutText = new JTextField(textColumns);
            timeoutText.setText(Constants.DEFAULT_TIMEOUT);
            timeoutLable.setLabelFor(timeoutText);
            h.add(timeoutLable);
            h.add(timeoutText);
            h.add(makeHelper("Invoking timeout(ms)"));
            //Version
            JLabel versionLable = new JLabel("Alias:", SwingConstants.RIGHT);
            versionText = new JTextField(textColumns);
            versionText.setText(Constants.DEFAULT_ALAIS);
            versionLable.setLabelFor(versionText);
            h.add(versionLable);
            h.add(versionText);
            h.add(makeHelper("Service version."));
            //Retries
            JLabel retriesLable = new JLabel("Retries:", SwingConstants.RIGHT);
            retriesText = new JTextField(textColumns);
            retriesText.setText(Constants.DEFAULT_RETRIES);
            retriesLable.setLabelFor(retriesText);
            h.add(retriesLable);
            h.add(retriesText);
            h.add(makeHelper("The retry count for RPC, not including the first invoke. Please set it to 0 if don't need to retry."));
            //Cluster
            JLabel clusterLable = new JLabel("Cluster:", SwingConstants.RIGHT);
            clusterText = new JTextField(textColumns);
            clusterText.setText(Constants.DEFAULT_CLUSTER);
            clusterLable.setLabelFor(clusterText);
            h.add(clusterLable);
            h.add(clusterText);
            h.add(makeHelper("failover/failfast/failsafe/failback/forking are available."));
            //Group
            JLabel groupLable = new JLabel("Group:", SwingConstants.RIGHT);
            groupText = new JTextField(textColumns);
            groupLable.setLabelFor(groupText);
            h.add(groupLable);
            h.add(groupText);
            h.add(makeHelper("The group of the service providers. It can distinguish services when it has multiple implements."));
            //Connections
            JLabel connectionsLable = new JLabel("Connections:", SwingConstants.RIGHT);
            connectionsText = new JTextField(textColumns);
            connectionsText.setText(Constants.DEFAULT_CONNECTIONS);
            connectionsLable.setLabelFor(connectionsText);
            h.add(connectionsLable);
            h.add(connectionsText);
            h.add(makeHelper("The maximum connections of every provider. For short connection such as rmi, http and hessian, it's connection limit, but for long connection such as dubbo, it's connection count."));
            consumerSettings.add(h);

            JPanel hp1 = new HorizontalPanel();
            //Async
            JLabel asyncLable = new JLabel("     Async:", SwingConstants.RIGHT);
            asyncText = new JComboBox<String>(new String[]{"sync", "async"});
            asyncLable.setLabelFor(asyncText);
            hp1.add(asyncLable);
            hp1.add(asyncText);
            hp1.add(makeHelper("Asynchronous execution, not reliable. It does not block the execution thread just only ignores the return value."));
            //Loadbalance
            JLabel loadbalanceLable = new JLabel("Loadbalance:", SwingConstants.RIGHT);
            loadbalanceText = new JComboBox<String>(new String[]{"random", "roundrobin", "leastactive", "consistenthash"});
            loadbalanceLable.setLabelFor(loadbalanceText);
            hp1.add(loadbalanceLable);
            hp1.add(loadbalanceText);
            hp1.add(makeHelper("Strategy of load balance, random, roundrobin and leastactive are available."));
            consumerSettings.add(hp1);
            return consumerSettings;
        }

        public JPanel drawInterfaceSettingsPanel() {
            //Interface Settings
            JPanel interfaceSettings = new VerticalPanel();
            interfaceSettings.setBorder(BorderFactory.createTitledBorder("Interface"));
            //Selection Interface
            JPanel sh = new HorizontalPanel();

            interfaceSettings.add(sh);
            //Interface
            JPanel ih = new HorizontalPanel();
            JLabel interfaceLable = new JLabel("Interface:", SwingConstants.RIGHT);
            interfaceText = new JTextField(textColumns);
            interfaceLable.setLabelFor(interfaceText);
            ih.add(interfaceLable);
            ih.add(interfaceText);
            ih.add(makeHelper("The service interface name."));
            interfaceSettings.add(ih);
            //Method
            JPanel mh = new HorizontalPanel();
            JLabel methodLable = new JLabel("   Method:", SwingConstants.RIGHT);
            methodText = new JTextField(textColumns);
            methodLable.setLabelFor(methodText);
            mh.add(methodLable);
            mh.add(methodText);
            mh.add(makeHelper("The service method name"));
            interfaceSettings.add(mh);

            //选项卡
            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            //接口参数表格
            JPanel tablePanel = new HorizontalPanel();
            model = new DefaultTableModel();
            model.setDataVector(null, columnNames);
            final JTable table = new JTable(model);
            table.setRowHeight(40);
            //失去光标退出编辑
            table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
            //添加按钮
            JButton addBtn = new JButton("增加");
            addBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    model.addRow(tmpRow);
                }
            });
            JButton delBtn = new JButton("删除");
            delBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    int rowIndex = table.getSelectedRow();
                    if(rowIndex != -1) {
                        model.removeRow(rowIndex);
                    }
                }
            });
            //表格滚动条
            JScrollPane scrollpane = new JScrollPane(table);
            tablePanel.add(scrollpane);
            tablePanel.add(addBtn);
            tablePanel.add(delBtn);
            tabbedPane.add("Args",tablePanel);

            //隐式参数表格
            JPanel tablePanelAttachment = new HorizontalPanel();
            modelAttachment = new DefaultTableModel();
            modelAttachment.setDataVector(null, columnNamesAttachment);
            final JTable tableAttachment = new JTable(modelAttachment);
            tableAttachment.setRowHeight(40);
            //失去光标退出编辑
            tableAttachment.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
            //添加按钮
            JButton addBtnAttachment = new JButton("增加");
            addBtnAttachment.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    modelAttachment.addRow(tmpRow);
                }
            });
            JButton delBtnAttachment = new JButton("删除");
            delBtnAttachment.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    int rowIndex = tableAttachment.getSelectedRow();
                    if(rowIndex != -1) {
                        modelAttachment.removeRow(rowIndex);
                    }
                }
            });
            //表格滚动条
            JScrollPane scrollpaneAttachment = new JScrollPane(tableAttachment);
            tablePanelAttachment.add(scrollpaneAttachment);
            tablePanelAttachment.add(addBtnAttachment);
            tablePanelAttachment.add(delBtnAttachment);
            tabbedPane.add("Attachment Args",tablePanelAttachment);

            interfaceSettings.add(tabbedPane);
            return interfaceSettings;
        }

        public void configureRegistry(TestElement element) {
            registryProtocolText.setSelectedItem(Constants.getRegistryProtocol(element));
            registryGroupText.setText(Constants.getRegistryGroup(element));
            registryUserNameText.setText(Constants.getRegistryUserName(element));
            registryPasswordText.setText(Constants.getRegistryPassword(element));
            addressText.setText(Constants.getAddress(element));
            registryTimeoutText.setText(Constants.getRegistryTimeout(element));
        }



        public void configureProtocol(TestElement element) {
            rpcProtocolText.setSelectedItem(Constants.getRpcProtocol(element));
        }

        public void configureConsumer(TestElement element) {
            versionText.setText(Constants.getAlias(element));
            timeoutText.setText(Constants.getTimeout(element));
            retriesText.setText(Constants.getRetries(element));
            groupText.setText(Constants.getGroup(element));
            connectionsText.setText(Constants.getConnections(element));
            loadbalanceText.setSelectedItem(Constants.getLoadbalance(element));
            asyncText.setSelectedItem(Constants.getAsync(element));
            clusterText.setText(Constants.getCluster(element));
        }

        public void configureInterface(TestElement element) {
            interfaceText.setText(Constants.getInterface(element));
            methodText.setText(Constants.getMethod(element));
            Vector<String> columnNames = new Vector<String>();
            columnNames.add("paramType");
            columnNames.add("paramValue");
            model.setDataVector(paserMethodArgsData(Constants.getMethodArgs(element)), columnNames);
            Vector<String> columnNamesAttachment = new Vector<String>();
            columnNamesAttachment.add("key");
            columnNamesAttachment.add("value");
            modelAttachment.setDataVector(paserMethodArgsData(Constants.getAttachmentArgs(element)), columnNamesAttachment);
        }

        public void modifyRegistry(TestElement element) {
            Constants.setRegistryProtocol(registryProtocolText.getSelectedItem().toString(), element);
            Constants.setRegistryGroup(registryGroupText.getText(), element);
            Constants.setRegistryUserName(registryUserNameText.getText(), element);
            Constants.setRegistryPassword(registryPasswordText.getText(), element);
            Constants.setAddress(addressText.getText(), element);
            Constants.setRegistryTimeout(registryTimeoutText.getText(), element);
        }

        public void modifyProtocol(TestElement element) {
            Constants.setRpcProtocol(rpcProtocolText.getSelectedItem().toString(), element);
        }
        public void modifyConsumer(TestElement element) {
            Constants.setTimeout(timeoutText.getText(), element);
            Constants.setAlias(versionText.getText(), element);
            Constants.setRetries(retriesText.getText(), element);
            Constants.setGroup(groupText.getText(), element);
            Constants.setConnections(connectionsText.getText(), element);
            Constants.setLoadbalance(loadbalanceText.getSelectedItem().toString(), element);
            Constants.setAsync(asyncText.getSelectedItem().toString(), element);
            Constants.setCluster(clusterText.getText(), element);
        }
        public void modifyInterface(TestElement element) {
            Constants.setInterfaceName(interfaceText.getText(), element);
            Constants.setMethod(methodText.getText(), element);
            Constants.setMethodArgs(getMethodArgsData(model.getDataVector()), element);
            Constants.setAttachmentArgs(getMethodArgsData(modelAttachment.getDataVector()), element);
        }
        public void clearRegistry() {
            registryProtocolText.setSelectedIndex(0);
            registryGroupText.setText("");
            registryUserNameText.setText("");
            registryPasswordText.setText("");
            addressText.setText("");
            registryTimeoutText.setText("");
        }

        public void clearProtocol() {
            rpcProtocolText.setSelectedIndex(0);
        }
        public void clearConsumer() {
            timeoutText.setText(Constants.DEFAULT_TIMEOUT);
            versionText.setText(Constants.DEFAULT_ALAIS);
            retriesText.setText(Constants.DEFAULT_RETRIES);
            clusterText.setText(Constants.DEFAULT_CLUSTER);
            groupText.setText("");
            connectionsText.setText(Constants.DEFAULT_CONNECTIONS);
            loadbalanceText.setSelectedIndex(0);
            asyncText.setSelectedIndex(0);
        }
        public void clearInterface() {
            interfaceText.setText("");
            methodText.setText("");
            model.setDataVector(null, columnNames);
            modelAttachment.setDataVector(null, columnNamesAttachment);
        }

        private List<MethodArgument> getMethodArgsData(Vector<Vector<String>> data) {
            List<MethodArgument> params = new ArrayList<MethodArgument>();
            if (!data.isEmpty()) {
                //处理参数
                Iterator<Vector<String>> it = data.iterator();
                while(it.hasNext()) {
                    Vector<String> param = it.next();
                    if (!param.isEmpty()) {
                        params.add(new MethodArgument(param.get(0), param.get(1)));
                    }
                }
            }
            return params;
        }

        private Vector<Vector<String>> paserMethodArgsData(List<MethodArgument> list) {
            Vector<Vector<String>> res = new Vector<Vector<String>>();
            for (MethodArgument args : list) {
                Vector<String> v = new Vector<String>();
                v.add(args.getParamType());
                v.add(args.getParamValue());
                res.add(v);
            }
            return res;
        }



        public JLabel makeHelper(String tooltip) {
            JLabel helpLable = new JLabel();
            helpLable.setIcon(new ImageIcon(getClass().getResource("/images/help.png")));
            helpLable.setToolTipText(tooltip);
            return helpLable;
        }

}
