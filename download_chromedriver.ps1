$url = "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/135.0.7049.85/win64/chromedriver-win64.zip"
$output = "chromedriver.zip"
$extractPath = "."

# Descargar ChromeDriver
Invoke-WebRequest -Uri $url -OutFile $output

# Extraer el archivo ZIP
Expand-Archive -Path $output -DestinationPath $extractPath -Force

# Mover el archivo chromedriver.exe a la raíz del proyecto
Move-Item -Path ".\chromedriver-win64\chromedriver.exe" -Destination ".\chromedriver.exe" -Force

# Limpiar archivos temporales
Remove-Item -Path $output -Force
Remove-Item -Path ".\chromedriver-win64" -Recurse -Force

Write-Host "ChromeDriver ha sido descargado y configurado correctamente." 