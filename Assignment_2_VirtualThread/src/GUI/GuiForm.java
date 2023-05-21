package GUI;

//import Controller.GuiController;
import Model.Directory;
import Monitor.GuiObserver;
import utility.Analyser.SourceAnalyzer;
import utility.Analyser.SourceAnalyzerImpl;
import utility.Pair;

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class GuiForm implements GuiObserver {

	private final JFrame frame;
	private final JTextArea textAreaInterval;
	private final JTextArea textAreaFileLength;
	private final JButton btnStop;
	private final JButton btnSearch;
	private int N;

	/**
	 * Create the application.
	 */
	public GuiForm() {

		SourceAnalyzer sourceAnalyzer = new SourceAnalyzerImpl(this);

		frame = new JFrame();
		frame.setBounds(100, 100, 789, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel("Progetto 2 - RxJava");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitle.setBounds(10, 0, 774, 35);
		frame.getContentPane().add(lblTitle);

		JLabel lblDirectory = new JLabel("Directory:");
		lblDirectory.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDirectory.setBounds(20, 74, 79, 35);
		frame.getContentPane().add(lblDirectory);

		JLabel lblNumInterval = new JLabel("Numero intervalli:");
		lblNumInterval.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNumInterval.setBounds(205, 142, 128, 20);
		frame.getContentPane().add(lblNumInterval);

		JTextField textFieldDirectory = new JTextField();
		textFieldDirectory.setBackground(Color.WHITE);
		textFieldDirectory.setEditable(false);
		textFieldDirectory.setBounds(98, 83, 257, 20);
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
		btnSfogliaDirectory.setBounds(377, 82, 89, 23);
		frame.getContentPane().add(btnSfogliaDirectory);

		JLabel lblMaxLength = new JLabel("Lunghezza massima:");
		lblMaxLength.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMaxLength.setBounds(437, 142, 136, 20);
		frame.getContentPane().add(lblMaxLength);

		JTextField textFieldMaxLength = new JFormattedTextField();
		textFieldMaxLength.setBounds(583, 142, 96, 20);
		textFieldMaxLength.setColumns(10);
		textFieldMaxLength.setText("1000");
		frame.getContentPane().add(textFieldMaxLength);

		SpinnerNumberModel model1 = new SpinnerNumberModel(1, 1, 1000, 1);
		JSpinner spinnerNumInterval = new JSpinner(model1);
		spinnerNumInterval.setBounds(329, 142, 70, 23);
		frame.getContentPane().add(spinnerNumInterval);

		SpinnerNumberModel model2 = new SpinnerNumberModel(1, 1, 1000, 1);
		JSpinner spinnerNumRowsFile = new JSpinner(model2);
		spinnerNumRowsFile.setBounds(109, 142, 70, 20);
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

				sourceAnalyzer.initSource(textMaxLength, numInterval);
				try {
					sourceAnalyzer.analyzeSources(new Directory(textFieldDirectory.getText()));
				} catch (InterruptedException ex) {
					throw new RuntimeException(ex);
				}


				btnSearch.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnSearch.setBounds(289, 196, 96, 35);
		frame.getContentPane().add(btnSearch);

		btnStop.addActionListener(e -> {
			btnStop.setEnabled(false);
			btnSearch.setEnabled(true);

			sourceAnalyzer.stopAnalyze();

			textAreaFileLength.setEditable(false);
			textAreaInterval.setEditable(false);
		});
		btnStop.setEnabled(false);
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnStop.setBounds(408, 196, 104, 35);
		frame.getContentPane().add(btnStop);

		JScrollPane IntervalscrollPanel = new JScrollPane(textAreaInterval);
		IntervalscrollPanel.setBounds(470, 279, 279, 298);
		frame.getContentPane().add(IntervalscrollPanel);


		JLabel lblIntervalGraph = new JLabel("Intervalli");
		IntervalscrollPanel.setColumnHeaderView(lblIntervalGraph);
		lblIntervalGraph.setHorizontalAlignment(SwingConstants.CENTER);
		lblIntervalGraph.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JScrollPane fileLengthScrollPanel = new JScrollPane(textAreaFileLength);
		fileLengthScrollPanel.setBounds(25, 279, 281, 298);
		frame.getContentPane().add(fileLengthScrollPanel);

		JLabel lblMaxLengthGraph = new JLabel("Lunghezza file");
		fileLengthScrollPanel.setColumnHeaderView(lblMaxLengthGraph);
		lblMaxLengthGraph.setHorizontalAlignment(SwingConstants.CENTER);
		lblMaxLengthGraph.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JLabel lblNewLabel = new JLabel("Numero file:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(20, 142, 79, 14);
		frame.getContentPane().add(lblNewLabel);

		JButton btnCloseButton = new JButton("Chiudi");
		btnCloseButton.setBounds(340, 602, 96, 35);
		btnCloseButton.addActionListener(e -> System.exit(0));
		frame.getContentPane().add(btnCloseButton);
		this.setVisible(true);
	}

	public void setVisible(final boolean visible){
		frame.setVisible(visible);
	}



	@Override
	public void guiFileLengthUpdated(TreeSet<Pair<File, Long>> fileLengthMap) {
		try {
			SwingUtilities.invokeLater(() -> {
				if (fileLengthMap.size() > N){
					textAreaFileLength.setText("");
					List<Pair<File, Long>> fileList = fileLengthMap.stream().toList().subList(0,N);
					fileList.forEach(pair -> textAreaFileLength.setText(textAreaFileLength.getText() + "\n" + "File: " + pair.getX().getName() + " - Len: " + pair.getY() + "\n"));
				}
			});
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}


	@Override
	public void guiIntervalUpdated(HashMap<Pair<Integer, Integer>, Integer> intervalMap) {
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
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void analyzeEnded() {
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
