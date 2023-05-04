package assignment.Agent.GUI;

import assignment.Message.MessageUpdate;
import assignment.Model.Directory;
import assignment.Utility.Analyser.SourceAnalyzer;
import assignment.Utility.Pair;
import com.google.common.collect.Iterables;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;

public class GuiFormAgent extends AbstractVerticle {

	private final JFrame frame;
	private final JTextArea textAreaInterval;
	private final JTextArea textAreaFileLength;
	private final JButton btnStop;
	private final JButton btnSearch;
	private int N;
	private final SourceAnalyzer sourceAnalyser;

	/**
	 * Create the application.
	 */
	public GuiFormAgent(final SourceAnalyzer sourceAnalyzer) {

		this.sourceAnalyser = sourceAnalyzer;

		frame = new JFrame();
		frame.setBounds(100, 100, 922, 520);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);


		JLabel lblTitle = new JLabel("Progetto 2 - Event Loop");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitle.setBounds(10, 0, 913, 35);
		frame.getContentPane().add(lblTitle);

		JLabel lblDirectory = new JLabel("Directory:");
		lblDirectory.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDirectory.setBounds(10, 60, 79, 35);
		frame.getContentPane().add(lblDirectory);

		JLabel lblNumInterval = new JLabel("Numero intervalli:");
		lblNumInterval.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNumInterval.setBounds(287, 124, 128, 31);
		frame.getContentPane().add(lblNumInterval);

		JTextField textFieldDirectory = new JTextField();
		textFieldDirectory.setBackground(Color.WHITE);
		textFieldDirectory.setEditable(false);
		textFieldDirectory.setBounds(88, 69, 257, 20);
		textFieldDirectory.setText("C:/Users/david/Desktop/TestFolder2");
		frame.getContentPane().add(textFieldDirectory);
		textFieldDirectory.setColumns(10);

		JButton btnSfogliaDirectory = new JButton("Sfoglia");
		btnSfogliaDirectory.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = fileChooser.showOpenDialog(frame);
			if(option == JFileChooser.APPROVE_OPTION){
				File file = fileChooser.getSelectedFile();
				System.out.println("Directory chosen: " + file.getAbsolutePath());
				textFieldDirectory.setText(file.getAbsolutePath());
			}else{
				System.out.println("FileChooser closed");
			}
		});
		btnSfogliaDirectory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSfogliaDirectory.setBounds(367, 68, 89, 23);
		frame.getContentPane().add(btnSfogliaDirectory);

		JLabel lblMaxLength = new JLabel("Lunghezza massima:");
		lblMaxLength.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMaxLength.setBounds(538, 128, 158, 20);
		frame.getContentPane().add(lblMaxLength);

		JTextField textFieldMaxLength = new JFormattedTextField();
		textFieldMaxLength.setBounds(703, 128, 96, 20);
		textFieldMaxLength.setColumns(10);
		textFieldMaxLength.setText("1000");
		frame.getContentPane().add(textFieldMaxLength);

		JLabel lblMaxLengthGraph = new JLabel("Lunghezza file");
		lblMaxLengthGraph.setHorizontalAlignment(SwingConstants.CENTER);
		lblMaxLengthGraph.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMaxLengthGraph.setBounds(46, 252, 406, 16);
		frame.getContentPane().add(lblMaxLengthGraph);


		JLabel lblIntervalGraph = new JLabel("Intervalli");
		lblIntervalGraph.setHorizontalAlignment(SwingConstants.CENTER);
		lblIntervalGraph.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblIntervalGraph.setBounds(470, 252, 406, 16);
		frame.getContentPane().add(lblIntervalGraph);

		SpinnerNumberModel model1 = new SpinnerNumberModel(1, 1, 1000, 1);
		JSpinner spinnerNumInterval = new JSpinner(model1);
		spinnerNumInterval.setBounds(429, 127, 70, 22);
		frame.getContentPane().add(spinnerNumInterval);

		SpinnerNumberModel model2 = new SpinnerNumberModel(1, 1, 1000, 1);
		JSpinner spinnerNumRowsFile = new JSpinner(model2);
		spinnerNumRowsFile.setBounds(191, 129, 70, 20);
		frame.getContentPane().add(spinnerNumRowsFile);

		textAreaFileLength = new JTextArea();
		textAreaFileLength.setBounds(25, 279, 427, 171);
		frame.getContentPane().add(textAreaFileLength);
		textAreaFileLength.setFocusable(false);

		textAreaInterval = new JTextArea();
		textAreaInterval.setBounds(470, 279, 406, 171);
		frame.getContentPane().add(textAreaInterval);
		textAreaInterval.setFocusable(false);

		btnStop = new JButton("Stop");
		btnSearch = new JButton("Cerca");
		btnSearch.addActionListener(e -> {

			int textMaxLength = Integer.parseInt(textFieldMaxLength.getText());
			int numInterval = (Integer)spinnerNumInterval.getValue();
			N = (Integer)spinnerNumRowsFile.getValue();
			Directory d = new Directory(textFieldDirectory.getText());

			if (textMaxLength < numInterval && textMaxLength % numInterval != 0)
				JOptionPane.showMessageDialog(frame, "Numero di intervalli e/o lunghezza massima errati","Errore", JOptionPane.ERROR_MESSAGE);
			else {
				textAreaFileLength.setText("");
				textAreaFileLength.setEditable(true);

				textAreaInterval.setText("");
				textAreaInterval.setEditable(true);

				sourceAnalyser.restartVertx();
				sourceAnalyser.analyzeSources(d, textMaxLength, numInterval, this);

				btnSearch.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnSearch.setBounds(357, 180, 96, 35);
		frame.getContentPane().add(btnSearch);

		btnStop.addActionListener(e -> {
			btnStop.setEnabled(false);
			btnSearch.setEnabled(true);

			sourceAnalyser.stopExecution();

			textAreaFileLength.setEditable(false);
			textAreaInterval.setEditable(false);
		});
		btnStop.setEnabled(false);
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnStop.setBounds(476, 180, 104, 35);
		frame.getContentPane().add(btnStop);

		JScrollPane scrollPane = new JScrollPane(textAreaInterval);
		scrollPane.setBounds(470, 279, 406, 171);
		frame.getContentPane().add(scrollPane);

		JLabel lblNRowsFile = new JLabel("Numero file più grandi:");
		lblNRowsFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNRowsFile.setBounds(10, 129, 208, 23);
		frame.getContentPane().add(lblNRowsFile);

		JScrollPane scrollPane_1 = new JScrollPane(textAreaFileLength);
		scrollPane_1.setBounds(25, 279, 431, 171);
		frame.getContentPane().add(scrollPane_1);
	}


	public void setVisible(final boolean visible){
		frame.setVisible(visible);
	}

	@Override
	public void start(){
		System.out.println("Gui form agent started");
		EventBus eventBus = sourceAnalyser.getVertx().eventBus();

		eventBus.consumer("gui-update-topic", (Message<MessageUpdate> message) -> {
			MessageUpdate mex = message.body();
			if (mex.getTypeMessage().equals("File length mex")) {
				TreeSet<Pair<File, Long>> fileLengthTree = mex.getFileLengthMap();
				this.setFileLengthGui(fileLengthTree);
			} else {
				HashMap<Pair<Integer, Integer>, Integer> intervalMap = mex.getIntervalMap();
				this.setIntervalGui(intervalMap);
			}
		});

		eventBus.consumer("guiEnd-topic", res -> {
			this.setEndGui();
		});
	}

	private void setFileLengthGui(final TreeSet<Pair<File, Long>> fileTree){
		try {
			SwingUtilities.invokeLater(() -> {
				if (fileTree.size() > N) {
					textAreaFileLength.setText("");
					Iterables.limit(fileTree, N).forEach(pair -> textAreaFileLength.setText(textAreaFileLength.getText() + "\n" + "File: " + pair.getX().getName() + " - Len: " + pair.getY() + "\n"));
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setIntervalGui(final HashMap<Pair<Integer, Integer>, Integer> intervalMap){
		try {
			SwingUtilities.invokeLater(() -> {
				textAreaInterval.setText("");
				intervalMap.forEach((key, value) -> {
					if (key.getY().equals(-1)) {
						textAreaInterval.setText(textAreaInterval.getText() + "Intervallo [ " + key.getX() + " - inf ]: " + value + "\n");
					} else {
						textAreaInterval.setText(textAreaInterval.getText() + "Intervallo [ " + key.getX() + " - " + key.getY() + " ]: " + value + "\n");
					}
				});
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setEndGui(){
		try {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(frame, "Elaborazione terminata","Completato", JOptionPane.PLAIN_MESSAGE);
				btnStop.setEnabled(false);
				btnSearch.setEnabled(true);
			});
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
