package create;

import java.io.*;
import java.util.Map;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JComboBox;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;


@SuppressWarnings("serial")
public class createH extends JFrame implements ActionListener {
	JSONObject jsonObject = new JSONObject();
	JFrame frame = new JFrame("yaml2Html");
	JTabbedPane tabPane = new JTabbedPane();// 选项卡布局
	Container con = new Container();// 布局1
	Container con1 = new Container();// 布局2
	JLabel label1 = new JLabel("yaml File:");
	JLabel label2 = new JLabel("Style:");
	JTextField text1 = new JTextField();
	JButton button1 = new JButton("...");
	JButton button2 = new JButton("create your html");
	JFileChooser jfc = new JFileChooser();// 文件选择器
	JComboBox comboBox = new JComboBox();// 下拉列表

	createH() {
		comboBox.addItem("    dark");
		comboBox.addItem("    light");
		jfc.setCurrentDirectory(new File("ymlFiles"));// 文件选择器的初始目录
		// 取得屏幕的高度和宽度
		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
		frame.setSize(300, 200);// 设定窗口大小
		frame.setContentPane(tabPane);// 设置布局
		// 设定标签的出现位置和高宽
		label1.setBounds(10, 30, 100, 20);
		text1.setBounds(80, 30, 120, 20);
		label2.setBounds(10, 60, 100, 20);
		comboBox.setBounds(80, 60, 120, 20);
		button1.setBounds(210, 30, 30, 20);
		button2.setBounds(65, 100, 160, 20);
		button1.addActionListener(this);
		button2.addActionListener(this);
		con.add(label1);
		con.add(text1);
		con.add(label2);
		con.add(button1);
		con.add(button2);
		con.add(jfc);
		con.add(comboBox);

		tabPane.add("options", con);// 添加布局
		frame.setVisible(true);// 窗口可见
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * 写入文件
	 * 
	 * @param fileName
	 * @return
	 */
	public void actionPerformed(ActionEvent e) {// 事件处理
		if (e.getSource().equals(button1)) {
			jfc.setFileSelectionMode(0);// 设定只能选择到文件
			int state = jfc.showOpenDialog(null);// 打开文件选择器界面的触发语句
			if (state == 1) {
				return;// 撤销则返回
			} else {
				File file = jfc.getSelectedFile();
				String xx = "";
				try {
					// 一次读一个字符
					InputStreamReader isr = new InputStreamReader(
							new FileInputStream(file), "UTF-8");
					BufferedReader read = new BufferedReader(isr);
					int tempchar;
					while ((tempchar = read.read()) != -1) {
						if (((char) tempchar) != '\r') {
							xx += (char) tempchar;
						}
					}
					read.close();

					// yaml转json
					Yaml yaml = new Yaml();
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) yaml
							.load(xx);

					JSONObject jsonObject2 = new JSONObject(map);
					jsonObject = jsonObject2;

				} catch (IOException exception) {
					exception.printStackTrace();
					return;
				}

				text1.setText(file.getAbsolutePath());
			}
		}

		if (e.getSource().equals(button2)) {
			if (text1.getText().trim().length() == 0) {
				return;
			} else {
				System.out.println("create!!!!!");
				System.out.println(jsonObject.toString());
				try {

					File newFile = new File("urHtml/create.html");
					if (newFile.exists()) {
						newFile.delete();
					}
					newFile.getParentFile().mkdirs();
					createFile(newFile);

					// 写入html文件内容
					writeTxtFile(newFile, "<!doctype html>");
					writeTxtFile(newFile, "  <html>");
					writeTxtFile(newFile, "    <head>");
					writeTxtFile(
							newFile,
							"      <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> ");
					writeTxtFile(newFile, "      <title>yourHtml</title>");
					writeTxtFile(newFile,
							"      <link rel=\"stylesheet\" type=\"text/css\" href=\"../css/"
									+ comboBox.getSelectedItem().toString()
											.trim() + ".css\"></script>");
					writeTxtFile(newFile,
							"      <script src=\"../js/jquery.min.js\"></script>");
					writeTxtFile(newFile, "      <script>  var json = "
							+ jsonObject.toString() + "</script>");
					writeTxtFile(newFile,
							"      <script src=\"../js/needs.js\"></script>");
					writeTxtFile(newFile, "    </head>");
					writeTxtFile(newFile, "    <body>");
					writeTxtFile(newFile, "    </body>");
					writeTxtFile(newFile, "  </html>");

					try {
						// 用系统默认方式打开文件
						java.awt.Desktop dp = java.awt.Desktop.getDesktop();
						dp.open(newFile);

					} catch (java.lang.NullPointerException e2) {
						e2.printStackTrace();
					} catch (java.io.IOException e2) {
						// 无法获取系统默认浏览器时
						e2.printStackTrace();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean createFile(File fileName) throws Exception {
		@SuppressWarnings("unused")
		boolean flag = false;
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 写入文件
	 * 
	 * @param fileName
	 * @return
	 */

	public static void writeTxtFile(File file, String content) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "utf-8"));
			out.write(content + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new createH();
	}
}
