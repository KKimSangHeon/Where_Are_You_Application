
# App 개발배경
-한 인원이 다수의 인원을 통제해야 하는 상황은 다양한 상황에서 발생할 수 있습니다. 가령 한 초등학교에서 현장체험학습의 일환으로 박물관을 방문하였을 경우, 교사 한명이 통제해야할 학생수는 약 30명에 이릅니다. 이는 교사에게 부담스러운 수가 될 수 있을것이라 판단하여 어플리케이션을 통해 극복하고자 하였습니다.

# App 소개
-본 어플리케이션은 BLE기반의 비콘을 이용하여 실내에서 다수 사용자의 위치를 파악하는 어플리케이션입니다. App을 사용하기 위해서는 User는 자신의 정보를 등록해야 하며 이때 입력하는 데이터는 이름, ClassID 입니다. Supervisor는 이름 혹은 (이름,ClassID)를 이용하여 User의 최근 위치를 파악할 수 있는데 ClassID로는 해당 ClassID를 갖는 모든 User가 조회되며, (이름,ClassID)으로는 해당 User의 이동경로를 파악할 수 있습니다.

# System Architecture
![System Architecture](https://kkimsangheon.github.io/2017/06/29/where-are-you/where-are-you-system-architecture.jpg) 

-본 어플리케이션의 이용자는 크게 User, Supervisor로 나뉘는데 User의 경우 FingerPrintingServer로 자신이 인식한 비콘들의 값을 전송합니다. 이를 수신한 FingerPrintingServer 서버는 User의 위치를 FingerPrinting 알고리즘을 적용하여 계산한 후 DB에 업데이트합니다. 이용자가 Supervisor 일 경우에는 StudentLocationPage에 접근하여 User들의 위치를 읽어옵니다.

# FingerPrinting Algorithm
-본 어플리케이션의 핵심 알고리즘은 FingerPrinting Algorithm 입니다. 이 알고리즘을 구현하기 위해 72개 지점에서 비콘인식 실험을 100번 간 진행하였습니다. 결과물을 토대로 평균, 분산, 표준편차 등을 곱하고 나누어서 가장 실제 위치와 근접한 값을 구할 수 있었으며 최종적으로 오차를 약 1.65m 까지 줄일 수 있었습니다.

-다음은 간단한 FingerPrinting Algorithm 적용 방식입니다. (서버에서 사용하는 알고리즘과는 다릅니다.)

![Algorithm적용 예시 [요약1]](https://kkimsangheon.github.io/2017/06/29/where-are-you/where-are-you-finger1.jpg)

핑거프린팅 기법은 사전에 측정된 각 좌표 별 비콘의 신호값을 토대로 현재 위치를 추측하는 기술입니다. 예를 들어 위와같이 각 좌표 별 사전에 측정된 비콘의 신호값(RSSI)이 존재한다고 가정 해 보겠습니다.
현재 위치에서 측정된 각 비콘의 RSSI 값이 B1: -86 B2: -91 B3: -100 일 때 Coordinate1에 대한 각 비콘 신호의 차이의 합을 구하면 (-86, -87과의 차이) + (-91, -90과의 차이) + (-100, -101과의 차이) 3입니다. 이 과정을 Coordinate2 또한 반복하면 결과값은 14이며 , Coordinate3 은 26입니다.
이 중 오차가 가장 작은 즉 최소값을 갖는 좌표가 현재 위치라 판단할 수 있을 것 이므로 현재 위치는 Coordinate1이라 추측할 수 있겠습니다.


# Result
![ClassID로 조회[그림1]](https://kkimsangheon.github.io/2017/06/29/where-are-you/where-are-you-result1.png) 

ClassID로 조회하였을 경우 위와같이 해당 User들이 최근에 위치한 좌표값을 확인할 수 있습니다.

![(이름,ClassID)로 조회[그림2]](https://kkimsangheon.github.io/2017/06/29/where-are-you/where-are-you-result2.png) 

(이름,ClassID)로 조회하였을 경우 해당 User의 이동경로가 위와같이 출력됩니다.

![카카오톡으로 조회[그림3]](https://kkimsangheon.github.io/2017/06/29/where-are-you/where-are-you-result3.png)

User의 위치는 카카오톡 yelloID (ID:WhereAreYou)로도 확인이 가능한데 위와같이 카카오톡을 이용하여 위치 조회가 가능합니다.

# 소스코드 (Github Repository)
[-안드로이드 어플리케이션 소스코드](https://github.com/KKimSangHeon/Where_Are_You_Application)

[-카카오톡 자동응답 서버 소스코드](https://github.com/KKimSangHeon/Where_Are_You_KakaoServer)

[-웹페이지 서버 소스코드](https://github.com/KKimSangHeon/Where_Are_You_JSONPage)

[-소켓서버 소스코드](https://github.com/KKimSangHeon/Where_Are_You_SocketServer)
