package com.aps.ui;

import com.aps.model.Environment;
import com.aps.services.DownloadService;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainJPanel extends JPanel {

    private Environment selectedAmbiente = Environment.DEV;
    private String saveFilePath;

    public MainJPanel() {

        setName("JFrameContentPane");
        setLayout(new BorderLayout(20, 20));
        add("Center", getPainelConteudo());
        add("South", getPainelBotoes());
    }

    private JPanel getPainelBotoes() {
        final JPanel jPanelBotoes = new JPanel();
        jPanelBotoes.setLayout(new GridLayout(1, 2));
        jPanelBotoes.add(getJButtonBaixar());
        jPanelBotoes.add(getJButtonFechar());
        return jPanelBotoes;
    }

    private JPanel getPainelConteudo() {
        final JPanel jPanelConteudo = new JPanel();
        jPanelConteudo.setLayout(new GridLayout(2, 2, 10, 10));
        jPanelConteudo.add(new JLabel("Ambiente: "));
        jPanelConteudo.add(getJComboBoxAmbiente());
        jPanelConteudo.add(new JLabel("Salvar Em: "));
        jPanelConteudo.add(getButtonFileChoose());
        return jPanelConteudo;
    }

    private JComboBox getJComboBoxAmbiente() {
        final JComboBox<Environment> jComboBox = new JComboBox<>();
        jComboBox.setName("JComboBoxAmbiente");
        jComboBox.addItem(Environment.DEV);
        jComboBox.addItem(Environment.HOM);
        jComboBox.addItem(Environment.PRD);

        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                selectedAmbiente = jComboBox.getItemAt(jComboBox.getSelectedIndex());
            }
        });

        return jComboBox;
    }

    private JButton getButtonFileChoose() {
        final JButton jButton = new JButton();
        jButton.setName("JButtonFolder");
        jButton.setText("Escolher Pasta");
        jButton.addActionListener(e -> {
            final JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.showSaveDialog(null);
            saveFilePath = f.getSelectedFile().getPath();
        });

        return jButton;
    }

    private JButton getJButtonBaixar() {
        final JButton jButton = new JButton();
        jButton.setName("JButtonBaixar");
        jButton.setText("Baixar");
        jButton
            .addActionListener(
                e -> new DownloadService(selectedAmbiente.getUrls(), saveFilePath).downloadFiles());
        return jButton;
    }

    private JButton getJButtonFechar() {
        final JButton jButton = new JButton();
        jButton.setName("JButtonFechar");
        jButton.setText("Fechar");
        jButton.addActionListener(e -> System.exit(0));

        return jButton;
    }
}
