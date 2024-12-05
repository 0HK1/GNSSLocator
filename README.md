# Documentação do Sistema de Localização de Satélites

## 1. Introdução
Este sistema de localização de satélites permite monitorar a posição e a qualidade dos sinais recebidos dos satélites visíveis para o dispositivo Android. O aplicativo utiliza a API de localização do Android para obter dados sobre os satélites visíveis, calcular a qualidade do sinal (Cn0DbHz), e exibir informações gráficas, incluindo a localização do usuário e a posição aproximada de cada satélite visível.

## 2. Funcionalidade

- **Monitoração em tempo real**: Através do `GnssStatus` e `LocationManager`, o sistema coleta dados sobre a posição do dispositivo e a qualidade do sinal dos satélites visíveis.
- **Exibição Gráfica**: O aplicativo exibe dados de qualidade de sinal de satélites visíveis em um gráfico de barras.
- **Visualização da Localização do Usuário**: A localização atual do usuário é exibida no mapa, e um círculo é desenhado em torno dessa posição para representar a área aproximada onde cada satélite pode ser visível, dependendo da sua altitude e posição orbital.
- **Posição Aproximada dos Satélites**: Utilizando os dados do `GnssStatus`, o aplicativo exibe a posição aproximada de cada satélite em um mapa, desenhando um círculo para representar sua posição relativa em relação à localização do usuário.
- **Filtros de Satélites**: O sistema permite que o usuário filtre os satélites por tipo de constelação (GPS, GLONASS, Galileo), além de poder selecionar se quer apenas satélites "em fix" (usados para o cálculo da posição).
- **Personalização de Formato de Localização**: O usuário pode escolher entre diferentes formatos de exibição da localização (graus, graus-minutos, ou graus-minutos-segundos).

## 3. Tecnologias Utilizadas
- **Android SDK**: Framework de desenvolvimento para Android.
- **API de Localização Android**: Para acessar os dados de localização e satélites.
- **GnssStatus**: Para monitoramento dos satélites e qualidade de sinal.
- **SharedPreferences**: Para armazenar as preferências do usuário, como formato de exibição da localização e filtros de satélites.
- **Google Maps API**: Para exibir a localização do usuário e a posição aproximada dos satélites no mapa.

## 4. Requisitos
- Android 6.0 (Marshmallow) ou superior.
- Permissão para acessar a localização do dispositivo (`ACCESS_FINE_LOCATION`).
- Conexão com serviços de GPS.
