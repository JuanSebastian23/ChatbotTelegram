/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.chatbot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import javax.swing.*;
import java.awt.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.URL;

/**
 *
 * @author juans
 */
public class NewJFrame extends javax.swing.JFrame {
    
    private static final Logger LOGGER = Logger.getLogger(NewJFrame.class.getName());
    private WebDriver driver;
    private final String[] conversaciones = new String[1000];
    private int cantConversaciones = 0;
    private String botToken = "";
    private TelegramBot bot;
    private Map<String, String> respuestas;
    private ExecutorService executorService;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static final int CORE_POOL_SIZE = 4;
    private static final int MAX_POOL_SIZE = 8;
    private static final long KEEP_ALIVE_TIME = 60L;

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
        inicializarRespuestas();
        initializeExecutorService();
        personalizarInterfaz();
    }

    private void personalizarInterfaz() {
        // Establecer un aspecto más moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "No se pudo establecer el look and feel", e);
        }

        // Personalizar colores y fuentes
        Color colorPrimario = new Color(0, 136, 204);
        Color colorFondo = new Color(240, 240, 240);
        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 14);
        Font fuenteNormal = new Font("Segoe UI", Font.PLAIN, 12);

        // Aplicar estilos
        this.getContentPane().setBackground(colorFondo);
        jPanel1.setBackground(colorFondo);
        jLabel1.setFont(fuenteTitulo);
        jLabel1.setForeground(colorPrimario);
        jTextField1.setFont(fuenteNormal);
        jCheckBox1.setFont(fuenteNormal);
        jCheckBox1.setBackground(colorFondo);
        jButton1.setFont(fuenteNormal);
        jButton1.setBackground(colorPrimario);
        jButton1.setForeground(Color.WHITE);
        jButton1.setFocusPainted(false);
        jButton1.setBorderPainted(false);

        // Cargar el icono de Telegram usando ruta absoluta
        try {
            File iconFile = new File("C:\\Users\\juans\\OneDrive\\Documentos\\NetBeansProjects\\ChatBot\\src\\img\\Telegram_2019_Logo.svg.png");
            if (iconFile.exists()) {
                ImageIcon iconoTelegram = new ImageIcon(iconFile.getAbsolutePath());
                // Redimensionar el icono si es necesario
                Image img = iconoTelegram.getImage();
                if (img != null) {
                    Image imgResized = img.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    iconoTelegram = new ImageIcon(imgResized);
                    setIconImage(iconoTelegram.getImage());
                    LOGGER.log(Level.INFO, "Icono de Telegram cargado correctamente");
                } else {
                    LOGGER.log(Level.WARNING, "No se pudo cargar la imagen del icono");
                }
            } else {
                LOGGER.log(Level.WARNING, "No se encontró el archivo de icono en: " + iconFile.getAbsolutePath());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al cargar el icono de Telegram", e);
        }
    }

    private void initializeExecutorService() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = defaultFactory.newThread(r);
                thread.setName("BotWorker-" + thread.getId());
                thread.setUncaughtExceptionHandler((t, e) -> 
                    LOGGER.log(Level.SEVERE, "Error no manejado en thread " + t.getName(), e));
                return thread;
            }
        };

        executorService = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bot de Telegram");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);

        jLabel1.setText("Token del Bot:");
        jPanel1.add(jLabel1, gbc);

        gbc.gridy = 1;
        jTextField1.setToolTipText("Ingresa el token del bot");
        jPanel1.add(jTextField1, gbc);

        gbc.gridy = 2;
        jSeparator1.setForeground(new Color(200, 200, 200));
        jPanel1.add(jSeparator1, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        jCheckBox1.setText("Activar Bot");
        jPanel1.add(jCheckBox1, gbc);

        gbc.gridx = 1;
        jButton1.setText("Ayuda");
        jPanel1.add(jButton1, gbc);

        getContentPane().add(jPanel1);
        pack();
        setLocationRelativeTo(null);

        // Agregar listeners
        jCheckBox1.addActionListener(evt -> jCheckBox1ActionPerformed(evt));
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jCheckBox1.isSelected()) {
            try {
                botToken = jTextField1.getText().trim();

                if (botToken.isEmpty()) {
                    throw new IllegalArgumentException("Por favor ingresa el token del bot");
                }

                if (!botToken.matches("^[0-9]+:[A-Za-z0-9_-]+$")) {
                    throw new IllegalArgumentException("El token del bot no tiene el formato correcto.\nDebe ser similar a: 123456789:ABCdefGHI-JKLmnopQRSTUvwxyz");
                }

                CompletableFuture.runAsync(() -> {
                    try {
                        bot = new TelegramBot();
                        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                        botsApi.registerBot(bot);
                        isRunning.set(true);
                        
                        // Iniciar ChromeDriver para Telegram Web
                        revisarTelegram();
                        
                        SwingUtilities.invokeLater(() -> 
                            JOptionPane.showMessageDialog(this, "Bot de Telegram activado correctamente"));
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error al inicializar el bot", e);
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, 
                                "Error al activar el bot: " + e.getMessage());
                            jCheckBox1.setSelected(false);
                        });
                    }
                }, executorService);

            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Error de validación", e);
                JOptionPane.showMessageDialog(this, e.getMessage());
                jCheckBox1.setSelected(false);
            }
        } else {
            try {
                isRunning.set(false);
                if (bot != null) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            // Cerrar ChromeDriver
                            if (driver != null) {
                                driver.quit();
                                driver = null;
                            }
                            // Limpiar recursos del bot
                            bot = null;
                            SwingUtilities.invokeLater(() -> 
                                JOptionPane.showMessageDialog(this, "Bot desactivado correctamente"));
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Error al desactivar el bot", e);
                            SwingUtilities.invokeLater(() -> 
                                JOptionPane.showMessageDialog(this, 
                                    "Error al desactivar el bot: " + e.getMessage()));
                        }
                    }, executorService);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al desactivar el bot", e);
                JOptionPane.showMessageDialog(this, "Error al desactivar el bot: " + e.getMessage());
            }
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String ayuda = "Para configurar el bot de Telegram:\n\n" +
                      "1. Busca @BotFather en Telegram\n" +
                      "2. Envía el comando /newbot\n" +
                      "3. Sigue las instrucciones para crear tu bot\n" +
                      "4. Copia el token que te proporciona BotFather\n" +
                      "5. Pega el token en el campo correspondiente\n" +
                      "6. Activa el bot con el checkbox";
        JOptionPane.showMessageDialog(this, ayuda, "Ayuda", JOptionPane.INFORMATION_MESSAGE);
    }

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {
        // No es necesario hacer nada aquí, solo mantener el método para el ActionListener
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    private String listarConversaciones() {
        if (cantConversaciones == 0) {
            return "No hay conversaciones activas.";
        }
        
        StringBuilder lista = new StringBuilder("Conversaciones activas:\n");
        for (int i = 0; i < cantConversaciones; i++) {
            lista.append((i + 1)).append(". ").append(conversaciones[i]).append("\n");
        }
        return lista.toString();
    }

    private void inicializarRespuestas() {
        respuestas = new HashMap<>();
        // Agregamos respuestas básicas
        respuestas.put("hola", "¡Hola! ¿En qué puedo ayudarte?");
        respuestas.put("buenos días", "¡Buenos días! ¿Cómo puedo ayudarte hoy?");
        respuestas.put("buenas tardes", "¡Buenas tardes! ¿En qué puedo servirte?");
        respuestas.put("buenas noches", "¡Buenas noches! ¿Necesitas ayuda con algo?");
        respuestas.put("ayuda", "Puedo ayudarte con:\n- Información general\n- Horarios\n- Contacto\n¿Qué necesitas?");
        respuestas.put("gracias", "¡De nada! Estoy aquí para ayudarte.");
        respuestas.put("adiós", "¡Hasta luego! Que tengas un excelente día.");
        respuestas.put("chao", "¡Hasta pronto! Si necesitas algo más, aquí estaré.");
        
        // Respuestas sobre el creador
        respuestas.put("quien es tu creador", "Mi creador es Juan Sebastian Quinto Hinestroza");
        respuestas.put("quien te creo", "Fui creado por Juan Sebastian Quinto Hinestroza");
        respuestas.put("quien te hizo", "Juan Sebastian Quinto Hinestroza es mi creador");
        respuestas.put("quien te programo", "Fui programado por Juan Sebastian Quinto Hinestroza");
        respuestas.put("quien te desarrollo", "Mi desarrollador es Juan Sebastian Quinto Hinestroza");
        respuestas.put("en que semestre esta tu creador", "Mi creador está en 5to semestre de ingeniería en sistemas");
        respuestas.put("que semestre cursa tu creador", "Mi creador está cursando el 5to semestre de ingeniería en sistemas");
        respuestas.put("en que semestre estudia tu creador", "Mi creador estudia en 5to semestre de ingeniería en sistemas");
        
        // Comando para listar conversaciones
        respuestas.put("lista de conversaciones", listarConversaciones());
        respuestas.put("mostrar conversaciones", listarConversaciones());
        respuestas.put("ver conversaciones", listarConversaciones());
    }

    private void pausa(long sleeptime) {
        try {
            Thread.sleep(sleeptime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.WARNING, "Interrupción durante la pausa", ex);
        }
    }

    private class TelegramBot extends TelegramLongPollingBot {
        @Override
        public void onUpdateReceived(Update update) {
            if (!isRunning.get()) return;

            if (update.hasMessage() && update.getMessage().hasText()) {
                CompletableFuture.runAsync(() -> {
                    try {
                        String messageText = update.getMessage().getText();
                        long chatId = update.getMessage().getChatId();
                        
                        // Procesar el mensaje y obtener respuesta
                        String response = NewJFrame.this.processMessage(messageText);

                        // Enviar respuesta
                        SendMessage message = new SendMessage();
                        message.setChatId(chatId);
                        message.setText(response);
                        execute(message);
                    } catch (TelegramApiException e) {
                        LOGGER.log(Level.SEVERE, "Error al enviar mensaje", e);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error procesando mensaje", e);
                    }
                }, executorService);
            }
        }

        @Override
        public String getBotUsername() {
            // No es necesario el username para el funcionamiento básico
            return "TuBot";
        }

        @Override
        public String getBotToken() {
            return botToken;
        }
    }

    @Override
    public void dispose() {
        try {
            if (executorService != null) {
                executorService.shutdown();
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            }
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Error al cerrar el ExecutorService", e);
            Thread.currentThread().interrupt();
        } finally {
            super.dispose();
        }
    }

    private void revisarTelegram() {
        try {
            if (driver == null) {
                String localPath = new File(".").getCanonicalPath();
                System.setProperty("webdriver.chrome.driver", localPath + "\\chromedriver.exe");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-notifications");
                options.addArguments("--user-data-dir=" + localPath + "\\chromeTelegram");
                driver = new ChromeDriver(options);
                driver.get("https://web.telegram.org/k/");
                
                // Esperar a que el usuario inicie sesión si es necesario
                pausa(5000);
            }

            // Verificar si hay nuevos mensajes
            List<WebElement> mensajesNuevos = driver.findElements(By.cssSelector(".unread"));
            
            for (WebElement mensaje : mensajesNuevos) {
                try {
                    // Obtener el chat
                    WebElement chat = mensaje.findElement(By.xpath("./ancestor::div[contains(@class, 'chat-item')]"));
                    String nombreChat = chat.findElement(By.cssSelector(".user-title")).getText();
                    
                    // Guardar la conversación si es nueva
                    boolean conversacionEncontrada = false;
                    for (int i = 0; i < cantConversaciones; i++) {
                        if (nombreChat.equals(conversaciones[i])) {
                            conversacionEncontrada = true;
                            break;
                        }
                    }
                    
                    if (!conversacionEncontrada && cantConversaciones < conversaciones.length) {
                        conversaciones[cantConversaciones++] = nombreChat;
                    }
                    
                    // Hacer clic en el chat
                    chat.click();
                    pausa(1000);
                    
                    // Obtener el último mensaje
                    WebElement ultimoMensaje = driver.findElement(By.cssSelector(".message-text:last-child"));
                    String textoMensaje = ultimoMensaje.getText();
                    
                    // Procesar el mensaje con el bot
                    String respuesta = this.processMessage(textoMensaje);
                    
                    // Enviar respuesta
                    WebElement inputMensaje = driver.findElement(By.cssSelector(".input-message-input"));
                    inputMensaje.sendKeys(respuesta);
                    inputMensaje.sendKeys(Keys.ENTER);
                    
                    pausa(1000);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error al procesar mensaje en Telegram Web", e);
                }
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar ChromeDriver", e);
            JOptionPane.showMessageDialog(this, "Error al iniciar el navegador: " + e.getMessage());
        }
    }

    protected String processMessage(String messageText) {
        if (messageText == null) return "Lo siento, no puedo procesar un mensaje vacío.";
        
        try {
            messageText = messageText.toLowerCase().trim();
            if (messageText.startsWith("/")) {
                return processCommand(messageText);
            }
            return respuestas.getOrDefault(messageText,
                "Lo siento, no entiendo ese mensaje. ¿Podrías reformularlo o usar /help para ver los comandos disponibles?");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error procesando mensaje: " + messageText, e);
            return "Lo siento, ocurrió un error al procesar tu mensaje.";
        }
    }

    protected String processCommand(String command) {
        if (command == null) return "Comando no válido";
        
        try {
            switch (command.toLowerCase().trim()) {
                case "/start":
                    return "¡Bienvenido! Soy tu asistente virtual. ¿En qué puedo ayudarte?";
                case "/help":
                    return "Comandos disponibles:\n" +
                           "/start - Iniciar conversación\n" +
                           "/help - Ver comandos disponibles\n" +
                           "/hora - Ver hora actual\n" +
                           "/info - Información general\n" +
                           "/conversaciones - Ver lista de conversaciones";
                case "/hora":
                    return "La hora actual es: " + 
                           LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                case "/info":
                    return "Soy un bot de asistencia creado para ayudarte.\n" +
                           "Puedes preguntarme sobre diversos temas o usar los comandos disponibles.";
                case "/conversaciones":
                    return listarConversaciones();
                default:
                    return "Comando no reconocido. Usa /help para ver los comandos disponibles.";
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error procesando comando: " + command, e);
            return "Lo siento, ocurrió un error al procesar el comando.";
        }
    }
}
