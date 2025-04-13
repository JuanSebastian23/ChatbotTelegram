# ChatBot de Telegram

Este es un bot de Telegram desarrollado en Java que proporciona una interfaz gráfica para la gestión y control de un bot de Telegram.

## Características

- Interfaz gráfica moderna y fácil de usar
- Integración con Telegram API
- Sistema de respuestas automáticas
- Manejo de comandos personalizados
- Registro de conversaciones
- Soporte para múltiples chats
- Manejo de excepciones y logging
- Procesamiento asíncrono de mensajes

## Requisitos Previos

- Java JDK 8 o superior
- Maven
- ChromeDriver
- Cuenta de Telegram
- Token de bot de Telegram (obtenido a través de @BotFather)

## Dependencias Principales

- selenium-java: 4.18.1
- selenium-chrome-driver: 4.18.1
- selenium-devtools: 4.18.1
- telegrambots: última versión estable

## Instalación

1. Clona el repositorio:
```bash
git clone [URL_DEL_REPOSITORIO]
```

2. Navega al directorio del proyecto:
```bash
cd ChatBot
```

3. Compila el proyecto con Maven:
```bash
mvn clean install
```

## Configuración

1. Obtén un token de bot de Telegram:
   - Busca @BotFather en Telegram
   - Envía el comando /newbot
   - Sigue las instrucciones para crear tu bot
   - Guarda el token proporcionado

2. Asegúrate de tener ChromeDriver instalado y en el path del sistema

## Uso

1. Ejecuta la aplicación
2. Ingresa el token del bot en el campo correspondiente
3. Activa el bot usando el checkbox
4. El bot comenzará a responder automáticamente a los mensajes

### Comandos Disponibles

- `/start` - Inicia la conversación
- `/help` - Muestra la lista de comandos disponibles
- `/hora` - Muestra la hora actual
- `/info` - Muestra información general del bot
- `/conversaciones` - Muestra la lista de conversaciones activas

### Respuestas Automáticas

El bot responde automáticamente a:
- Saludos (hola, buenos días, etc.)
- Preguntas sobre el creador
- Agradecimientos
- Despedidas

## Características de la Interfaz

- Diseño moderno con tema del sistema
- Indicadores visuales de estado
- Manejo de errores con mensajes informativos
- Ayuda integrada
- Iconos personalizados

## Seguridad

- Validación de token
- Manejo seguro de sesiones
- Logging de errores
- Cierre seguro de recursos

