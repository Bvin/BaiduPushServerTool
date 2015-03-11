package cn.bvin.desktop.app.baidupush.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.constants.BaiduChannelConstants;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.google.gson.Gson;

import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PushFrame extends JFrame {

	private JPanel contentPane;
	private JTable table_3;

	private String[] requestParamsHeader = {"参数名","参数值"};
	private String[][] requestParams;
	private JTabbedPane tabbedPane;
	
	private BaiduChannelClient channelClient;
	String apiKey = "6VcQVy58uM1v2GQA0YBsupl7";
    String secretKey = "luFEGyoNPPWAfK5BRlM2MNsU5z0aT5Ib";
    private JTable tableBroadcast;
    private JTable tableUnicast;
    private JTable tableTag;
    private JTextArea taMessage;
    private JTextField tfOpenUrl;
    private JTextField tfIconUrl;
    private JPanel msgBar;
    private JTextField tfShortcutName;
    private JCheckBox cbNotification;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PushFrame frame = new PushFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PushFrame() {
		setTitle("百度推送后台工具");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 342);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		// 1. 设置developer平台的ApiKey/SecretKey
		ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
		 // 2. 创建BaiduChannelClient对象实例
        channelClient = new BaiduChannelClient(pair);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				switch (tabbedPane.getSelectedIndex()) {
					case 3:
						if (msgBar!=null) {
							msgBar.setVisible(false);
						}
						break;
					default:
						if (msgBar!=null) {
							msgBar.setVisible(true);
						}
						break;
				}
			}
		});
		
		JScrollPane spAppSetting = new JScrollPane();
		spAppSetting.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spAppSetting.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		spAppSetting.setViewportBorder(null);
		
		JLabel label = new JLabel("应用设置");
		
		table_3 = new JTable();
		table_3.setModel(new DefaultTableModel(
			new Object[][] {
				{"apiKey", apiKey},
				{"secretKey", secretKey},
			},
			new String[] {"",""}
		));
		table_3.setFillsViewportHeight(true);
		table_3.getTableHeader().setVisible(false);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();  
        renderer.setPreferredSize(new Dimension(0, 0));
        table_3.getTableHeader().setDefaultRenderer(renderer);
		fitTableColumns(table_3);
		spAppSetting.setViewportView(table_3);
		
		JScrollPane spUnicast = new JScrollPane();
		tabbedPane.addTab("单播", null, spUnicast, null);
		
		tableUnicast = new JTable();
		tableUnicast.setFillsViewportHeight(true);
		spUnicast.setViewportView(tableUnicast);
		
		JScrollPane spTag = new JScrollPane();
		tabbedPane.addTab("组播", null, spTag, null);
		
		tableTag = new JTable();
		tableTag.setFillsViewportHeight(true);
		spTag.setViewportView(tableTag);
		
		JScrollPane spBroadcast = new JScrollPane();
		tabbedPane.addTab("广播", null, spBroadcast, null);
		
		tableBroadcast = new JTable()
		;
		tableBroadcast.setFillsViewportHeight(true);
		spBroadcast.setViewportView(tableBroadcast);
		
		requestParams = new String[11][];
		requestParams[0] = new String[] {BaiduChannelConstants.DEVICE_TYPE,"3"};
		requestParams[1] = new String[] {BaiduChannelConstants.CHANNEL_ID,"4036228052917871861"};
		requestParams[2] = new String[] {BaiduChannelConstants.USER_ID,"963160616567526359"};
		requestParams[3] = new String[] {BaiduChannelConstants.PUSH_TYPE,""};
		requestParams[4] = new String[] {BaiduChannelConstants.MESSAGE_TYPE,""};
		requestParams[5] = new String[] {BaiduChannelConstants.MSG_KEYS,""};
		requestParams[6] = new String[] {BaiduChannelConstants.DEPLOY_STATUS,""};
		DefaultTableModel dataModel = new DefaultTableModel(requestParams, requestParamsHeader);
		tableUnicast.setModel(dataModel);
		fitTableColumns(tableUnicast);
		
		String[][] t1Data = new String[2][];
		t1Data[0] = new String[] {BaiduChannelConstants.DEVICE_TYPE,"3"};
		t1Data[1] = new String[] {BaiduChannelConstants.TAG_NAME,""};
		DefaultTableModel dataModel1 = new DefaultTableModel(t1Data, requestParamsHeader);
		tableTag.setModel(dataModel1);
		fitTableColumns(tableTag);
		
		String[][] t2Data = new String[1][];
		t2Data[0] = new String[] {BaiduChannelConstants.DEVICE_TYPE,"3"};
		DefaultTableModel dataModel2 = new DefaultTableModel(t2Data, requestParamsHeader);
		tableBroadcast.setModel(dataModel2);
		fitTableColumns(tableBroadcast);
		
		JPanel panelShortcut = new JPanel();
		tabbedPane.addTab("推送快捷方式", null, panelShortcut, null);
		
		JLabel lblLurl = new JLabel("快捷方式目标url:");
		
		tfOpenUrl = new JTextField();
		tfOpenUrl.setColumns(10);
		
		JLabel lblurl = new JLabel("快捷方式图标url:");
		
		tfIconUrl = new JTextField();
		tfIconUrl.setColumns(10);
		
		JLabel label_1 = new JLabel("快捷方式名称");
		
		tfShortcutName = new JTextField();
		tfShortcutName.setColumns(10);
		
		cbNotification = new JCheckBox("是否在通知栏显示");
		GroupLayout gl_panelShortcut = new GroupLayout(panelShortcut);
		gl_panelShortcut.setHorizontalGroup(
			gl_panelShortcut.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelShortcut.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelShortcut.createParallelGroup(Alignment.LEADING)
						.addComponent(tfIconUrl, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
						.addComponent(lblurl)
						.addComponent(tfOpenUrl, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
						.addComponent(lblLurl)
						.addComponent(label_1)
						.addComponent(tfShortcutName, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbNotification)
					.addGap(15))
		);
		gl_panelShortcut.setVerticalGroup(
			gl_panelShortcut.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelShortcut.createSequentialGroup()
					.addComponent(label_1)
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addComponent(tfShortcutName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLurl)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfOpenUrl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblurl)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfIconUrl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(8))
				.addGroup(Alignment.LEADING, gl_panelShortcut.createSequentialGroup()
					.addContainerGap()
					.addComponent(cbNotification)
					.addContainerGap(120, Short.MAX_VALUE))
		);
		panelShortcut.setLayout(gl_panelShortcut);
		
		msgBar = new JPanel();
		
		JButton button = new JButton("发送");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				switch (tabbedPane.getSelectedIndex()) {
					case 0:
						pushUnicastMessage(tableUnicast.getModel(), taMessage.getText());
						break;
					case 1:
						pushTagMessage(tableTag.getModel(), taMessage.getText());
						break;
					case 2:
						pushBroadcastMessage(tableBroadcast.getModel(), taMessage.getText());
						break;
					case 3:
						if (tfOpenUrl.getText().isEmpty()|| tfIconUrl.getText().isEmpty()) {
							break;
						}
						ShortcutMeta shortcut = new ShortcutMeta(tfShortcutName.getText(),tfOpenUrl.getText(),
								tfIconUrl.getText(),cbNotification.isSelected());
						sendShortcut(tableUnicast.getModel(), shortcut);
						break;
						
					default:
						break;
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(msgBar, GroupLayout.PREFERRED_SIZE, 339, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(button)
								.addGap(12))
							.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
							.addComponent(label, Alignment.LEADING))
						.addComponent(spAppSetting, 0, 0, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spAppSetting, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(msgBar, 0, 0, Short.MAX_VALUE)
						.addComponent(button, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(21, Short.MAX_VALUE))
		);
		
		JSpinner snMessageType = new JSpinner();
		snMessageType.setModel(new SpinnerListModel(new String[] {"\u6D88\u606F", "\u901A\u77E5"}));
		taMessage = new JTextArea();
		GroupLayout gl_msgBar = new GroupLayout(msgBar);
		gl_msgBar.setHorizontalGroup(
			gl_msgBar.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_msgBar.createSequentialGroup()
					.addComponent(snMessageType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(taMessage, GroupLayout.PREFERRED_SIZE, 296, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(102, Short.MAX_VALUE))
		);
		gl_msgBar.setVerticalGroup(
			gl_msgBar.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_msgBar.createSequentialGroup()
					.addGroup(gl_msgBar.createParallelGroup(Alignment.BASELINE)
						.addComponent(snMessageType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(taMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		msgBar.setLayout(gl_msgBar);
		contentPane.setLayout(gl_contentPane);
	}

	//单播  阻塞的请求?
	private void pushUnicastMessage(TableModel option,String message) {
		 channelClient = createBaiduChannelClient(table_3.getModel());
		 PushUnicastMessageRequest request = new PushUnicastMessageRequest();
		 request.setDeviceType(Integer.valueOf(option.getValueAt(0,1).toString())); // device_type => 1: web 2: pc 3:android
         // 4:ios 5:wp
        request.setChannelId(Long.valueOf(option.getValueAt(1, 1).toString()));
        request.setUserId(option.getValueAt(2, 1).toString());
        
        request.setMessage(message);
        try {
			PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
			// 6. 认证推送成功
            System.out.println("push amount : " + response.getSuccessAmount());
		} catch (ChannelClientException | ChannelServerException e) {
			e.printStackTrace();
		}
	} 
	
	private void sendShortcut(TableModel option,ShortcutMeta shortcut) {
		pushUnicastMessage(option, new Gson().toJson(shortcut));
	}
	
	//组播 
	private void pushTagMessage(TableModel option,String message) {
         String tag = option.getValueAt(1,1).toString();
         if (!tag.isEmpty()) {
        	 channelClient = createBaiduChannelClient(table_3.getModel());
        	 PushTagMessageRequest request = new PushTagMessageRequest();
             request.setDeviceType(Integer.valueOf(option.getValueAt(0,1).toString()));
             request.setTagName(tag);
             request.setMessage(message);
             // 5. 调用pushMessage接口
             PushTagMessageResponse response;
			try {
				response = channelClient.pushTagMessage(request);
				// 6. 认证推送成功
	             System.out.println("push amount : " + response.getSuccessAmount());
			} catch (ChannelClientException | ChannelServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
		 }
         
	}

	//广播 
	private void pushBroadcastMessage(TableModel option,String message) {
		channelClient = createBaiduChannelClient(table_3.getModel());
		PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
        request.setDeviceType(Integer.valueOf(option.getValueAt(0,1).toString())); 
        request.setMessage(message);
        // 5. 调用pushMessage接口
        PushBroadcastMessageResponse response;
		try {
			response = channelClient.pushBroadcastMessage(request);
	        // 6. 认证推送成功
	        System.out.println("push amount : " + response.getSuccessAmount());
		} catch (ChannelClientException | ChannelServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private BaiduChannelClient createBaiduChannelClient(TableModel channelConfig) {
		String appKey = channelConfig.getValueAt(0, 1).toString();
		String secretKey = channelConfig.getValueAt(1, 1).toString();
		// 1. 设置developer平台的ApiKey/SecretKey
		ChannelKeyPair pair = new ChannelKeyPair(appKey, secretKey);
		return channelClient = new BaiduChannelClient(pair);
	}
	
	public void fitTableColumns(JTable myTable){
		  JTableHeader header = myTable.getTableHeader();
		     int rowCount = myTable.getRowCount();

		     Enumeration columns = myTable.getColumnModel().getColumns();
		     while(columns.hasMoreElements()){
		         TableColumn column = (TableColumn)columns.nextElement();
		         int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
		         int width = (int)myTable.getTableHeader().getDefaultRenderer()
		                 .getTableCellRendererComponent(myTable, column.getIdentifier()
		                         , false, false, -1, col).getPreferredSize().getWidth();
		         for(int row = 0; row<rowCount; row++){
		             int preferedWidth = (int)myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
		               myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
		             width = Math.max(width, preferedWidth);
		         }
		         header.setResizingColumn(column); // 此行很重要
		         column.setWidth(width+myTable.getIntercellSpacing().width+10);
		     }
	 }
}
