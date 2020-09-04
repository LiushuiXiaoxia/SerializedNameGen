package cn.mycommons.jsontool.ui;

import cn.mycommons.jsontool.core.IGenerateRule;
import cn.mycommons.jsontool.core.JsonType;
import cn.mycommons.jsontool.core.gen.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.util.Arrays;
import java.util.List;

public class ConfigDialog {

    private JPanel rootJPanel;
    private JPanel jsonTypeJPanel;
    private JPanel generatorRuleJPanel;

    private JButton okButton;
    private JButton cancelButton;

    private JRadioButton originRadioButton;
    private JRadioButton uppercaseRadioButton;
    private JRadioButton lowercaseRadioButton;
    private JRadioButton underlineRadioButton;
    private JRadioButton smartRadioButton;
    private JRadioButton firstUppercaseRadioButton;

    private JRadioButton jacksonRadioButton;
    private JRadioButton gsonRadioButton;
    private JRadioButton fastjsonRadioButton;

    private JsonType jsonType;
    private IGenerateRule generateRule = null;
    private OnCallback onCallback;

    private JDialog dialog;

    public ConfigDialog() {
        dialog = new JDialog();
        dialog.setContentPane(rootJPanel);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setAlwaysOnTop(true);

        init();
        initJsonTypeButtons();
        initRuleButtons();
    }

    void init() {
        okButton.addActionListener(e -> {
            dialog.hide();

            if (onCallback != null) {
                onCallback.onOk(jsonType, generateRule);
            }
        });
        cancelButton.addActionListener(e -> {
            dialog.hide();

            if (onCallback != null) {
                onCallback.onCancel();
            }
        });
    }

    private void initJsonTypeButtons() {
        List<JRadioButton> buttons = Arrays.asList(jacksonRadioButton, gsonRadioButton, fastjsonRadioButton);
        List<JsonType> types = Arrays.asList(JsonType.Jackson, JsonType.Gson, JsonType.FastJson);
        ChangeListener listener = e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            if (source.isSelected()) {
                updateButtons(buttons, source);
                jsonType = types.get(buttons.indexOf(source));
            }
        };
        for (JRadioButton button : buttons) {
            button.addChangeListener(listener);
        }
        jacksonRadioButton.setSelected(true);
    }

    private void initRuleButtons() {
        List<JRadioButton> buttons = Arrays.asList(
                originRadioButton, uppercaseRadioButton,
                lowercaseRadioButton, underlineRadioButton,
                smartRadioButton, firstUppercaseRadioButton
        );
        List<IGenerateRule> rules = Arrays.asList(
                new OriginGenerateImpl(), new UppercaseGenerateImpl(),
                new LowercaseGenerateImpl(), new UnderlineGenerateImpl(),
                new SmartGenerateImpl(), new FirstUppercaseGenerateImpl()
        );

        ChangeListener listener = e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            if (source.isSelected()) {
                generateRule = rules.get(buttons.indexOf(source));
                updateButtons(buttons, source);
            }
        };
        for (JRadioButton button : buttons) {
            button.addChangeListener(listener);
        }
        originRadioButton.setSelected(true);
    }

    private void updateButtons(List<JRadioButton> list, JRadioButton selected) {
        for (JRadioButton button : list) {
            if (button != selected) {
                button.setSelected(false);
            }
        }
    }

    public void setOnCallback(OnCallback onCallback) {
        this.onCallback = onCallback;
    }

    public void show() {
        dialog.setSize(600, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public interface OnCallback {

        void onOk(JsonType jsonType, IGenerateRule rule);

        void onCancel();
    }
}