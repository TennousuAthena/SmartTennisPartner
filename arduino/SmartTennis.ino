unsigned long time_now = 0;

// #include <Kalman.h>
#include <Wire.h>
#include <Math.h>
#include <SoftwareSerial.h>
#include <Adafruit_NeoPixel.h>
#include <Adafruit_MPU6050.h>

Adafruit_MPU6050 mpu;

Adafruit_NeoPixel led = Adafruit_NeoPixel(8, 6, NEO_GRB + NEO_KHZ800);
SoftwareSerial EEBlue(10, 11); // RX | TX

void setup() {
  Serial.begin(9600); //初始化串口，指定波特率
  EEBlue.begin(9600);
  Wire.begin(); //初始化Wire库
  led.begin();  // 打开led
  led.setBrightness(50); // 设置led的亮度为50，最大为255

  // Try to initialize!
  if (!mpu.begin()) {
    Serial.println("Failed to find MPU6050 chip");
  }
  mpu.setAccelerometerRange(MPU6050_RANGE_16_G);
  mpu.setGyroRange(MPU6050_RANGE_250_DEG);
  mpu.setFilterBandwidth(MPU6050_BAND_21_HZ);

}

uint32_t frame = 0;

uint8_t LEDMode = 0;    //LED模式
uint8_t red=0;
uint8_t green=0;
uint8_t blue=0;

uint32_t BTConn = 25;  //蓝牙连接状态心跳包

void LEDShow(){
  int speed = 10;    //数值越大呼吸越慢
  switch (LEDMode){
    //跑马灯模式
    case 0:{
      //熄灭全部
      for (uint16_t i = 0; i < 8; i++) {
        led.setPixelColor(i, led.Color(0, 0, 0));  
      }
      if(BTConn < 50){
        red = rand() % 256;    // 红色随机值
        green  = rand() % 256; // 绿色随机值
        blue = rand() % 256;   // 蓝色随机值
      }else{
        red = 30;
        green  = 0;
        blue = 0;
      }

      // 转圈圈
      led.setPixelColor(frame % 8, led.Color(red / 10, green / 10, blue / 10));     
      led.setPixelColor((frame + 1) % 8, led.Color(red, green, blue));
      led.setPixelColor((frame + 2) % 8, led.Color(red / 10, green / 10, blue / 10));
      
      led.show();
      break;
    };
    case 1:{
      //呼吸灯模式
      for (uint16_t i = 0; i < 8; i++) {
        uint8_t color;
        if(millis() / (speed *100) % 2){
          color = millis()/speed%100;
        }else{
          color = 100 - millis()/20%100;
        }
        if(BTConn < 50){
          led.setPixelColor(i, led.Color(0, color, 0));  
        }else{
          led.setPixelColor(i, led.Color(color, 0, 0));  
        }
        
      }
      
      led.show();
      break;
    }
  }
}

void loop() {
  int period = 50;

  if(millis() > time_now + period){
    time_now = millis();
    BTConn++;
    LEDShow();
      /* Get new sensor events with the readings */
    sensors_event_t a, g, temp;
    mpu.getEvent(&a, &g, &temp);
    String str = "{\"time\":"; 
    str.concat(String(time_now));
    str.concat(",\"AccelX\":");
    str.concat(String(a.acceleration.x, 3));
    str.concat(",\"AccelY\":");
    str.concat(String(a.acceleration.y, 3));
    str.concat(",\"AccelZ\":");
    str.concat(String(a.acceleration.z, 3));
    str.concat(",\"GyroX\":");
    str.concat(String(g.gyro.x, 3));
    str.concat(",\"GyroY\":");
    str.concat(String(g.gyro.y, 3));
    str.concat(",\"GyroZ\":");
    str.concat(String(g.gyro.z, 3));
    str.concat("}\n");

    int str_len = str.length() + 1; 
    char char_array[str_len];
    str.toCharArray(char_array, str_len);

    EEBlue.write(char_array);
    Serial.println(char_array);
    frame++;
  }
  if(EEBlue.available()){
    if(EEBlue.read()){
      BTConn=0;
    }
  }
}
