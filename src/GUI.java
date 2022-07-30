import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI {

  private JFrame frame;
  private Graphics g;
  private int mouseX, mouseY;
  private int currentColor = 0;
  private boolean changedColor = false;
  private boolean clicked = false;
  private boolean ignored = false;
  private static final String[] colorStrings = {"Red", "Green", "Blue", "Orange", "Yellow", "White"};
  private Color[] pickedColorArray = new Color[6];

  public static final Color IGNORE_COLOR = new Color(1, 0 ,0); //Default color to ignore

  public GUI() {
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Color Picker");
    frame.setSize(1200,650);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setLayout(null);
    try {
      File file = new File("app_icon/Washington_Icon.png");
      frame.setIconImage(ImageIO.read((file)));
    } catch (IOException e) {}

    for (int i = 0; i < colorStrings.length; i++) {
      pickedColorArray[i] = IGNORE_COLOR;
      addColorButton(colorStrings[i], i, 10 + (i * 120));
    }
    addRemoveButton();
    addSaveButton();
    g = frame.getGraphics();

    MouseListener mouseListener = new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        clicked = true;
      }

      @Override
      public void mousePressed(MouseEvent e) {}

      @Override
      public void mouseReleased(MouseEvent e) {}

      @Override
      public void mouseEntered(MouseEvent e) {}

      @Override
      public void mouseExited(MouseEvent e) {}
    };

    frame.addMouseListener(mouseListener);
  }

  private void addColorButton(String colorName, int colorNumber, int xCoord) {
    JButton button = new JButton(colorName);
    button.setLayout(null);
    button.setBounds(xCoord, 580, 100,25);
    button.setVisible(true);
    button.addActionListener(e -> {
      currentColor = colorNumber;
      changedColor = true;
    });
    frame.add(button);
  }

  private void addRemoveButton() {
    JButton button = new JButton("Remove");
    button.setLayout(null);
    button.setBounds(950, 580, 100,25);
    button.setVisible(true);
    button.addActionListener(e -> {
      ignored = true;
    });
    frame.add(button);
  }

  private void addSaveButton() {
    JButton button = new JButton("Save");
    button.setLayout(null);
    button.setBounds(1070, 580, 100,25);
    button.setVisible(true);
    button.addActionListener(e -> {
      Pixelator.finished = true;
    });
    frame.add(button);
  }

  public JFrame getFrame() {
    return frame;
  }

  public Color[] getPickedColorArray(BufferedImage image) {

    g.drawImage(image, 0, 0, null);
    g.setFont(new Font("Arial", Font.BOLD, 14));
    while (!Pixelator.finished) {
      if (changedColor) {
        g.clearRect(800, 600, 100, 50);
        changedColor = false;
        g.setColor(Pixelator.colorArray[currentColor]);
        g.drawString("Pick " + colorStrings[currentColor], 800, 628);
      }
      if (ignored) {
        ignored = false;
        pickedColorArray[currentColor] = IGNORE_COLOR;
        return pickedColorArray;
      }
      if (clicked) {
        clicked = false;
        if (mouseX < 600 && mouseY < 600) {
            pickedColorArray[currentColor] = new Color(image.getRGB(mouseX, mouseY));
        }
        return pickedColorArray;
      }
    }
    return pickedColorArray;
  }

  public void drawPixeledImage(Image image) {
    g.drawImage(image, 600, 0, null);
  }

  public void close() {
    g.dispose();
    frame.dispose();
  }

}