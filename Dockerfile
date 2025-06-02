FROM openjdk:17-slim

# Переменные среды
ENV ANDROID_SDK_ROOT /opt/android-sdk
ENV PATH ${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools

# Установка зависимостей
RUN apt-get update && \
    apt-get install -y wget unzip git && \
    mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    cd ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip -O tools.zip && \
    unzip tools.zip -d temp && \
    mv temp/cmdline-tools latest && \
    rm tools.zip

# Установка SDK-компонентов
RUN yes | sdkmanager --licenses && \
    sdkmanager --sdk_root=${ANDROID_SDK_ROOT} \
        "platform-tools" \
        "platforms;android-34" \
        "build-tools;34.0.0"

# Копируем проект
WORKDIR /app
COPY . /app

# Сборка проекта
RUN ./gradlew build --no-daemon

CMD ["bash"]
