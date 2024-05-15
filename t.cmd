::call gradlew.bat clean assemble
java -jar ./build/libs/jfc-app-publisher-0.0.1-SNAPSHOT.jar  --command=processImage --file=data/face_image.jpg
