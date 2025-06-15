멀티캐스트를 사용해 이진 데이터를 전송하고, 수신 측에서 파일로 저장하는 구조.
초기 코드들은 다양한 환경에서 실행되지 않거나 오류가 발생했기 때문에, 범용적으로 동작하도록 수정했고, 안 되는 코드를 되게 만드는 것을 핵심 목표로 함.

기존 문제점 요약
구분	문제점
실행 오류	클래스 파일이 Java 21에서 컴파일되어 Java 8에서 실행 불가 (UnsupportedClassVersionError)
수신 실패	MulticastReceiver가 간헐적으로 패킷을 놓치거나 파일이 저장되지 않음
포트 충돌	송수신 양쪽에서 포트가 겹치거나 소켓 바인딩 실패 발생
저장 문제	WriteToFileEvent로 받은 데이터가 실제 파일에 저장되지 않거나 0byte 파일 생성
비호환성	경로, 바이트 처리 방식, 네트워크 설정이 환경별로 다르게 동작

해결 방법 요약
1. 버전 호환 정리
Java 8 사용 시 javac -source 8 -target 8로 다시 컴파일 가능

가능하면 Java 17 이상 설치 권장 (현재 기준 Java 21~24 안정)

2. 네트워크 구조 정비
MulticastSocket 설정 시 포트 재사용 옵션 제거

Receiver가 먼저 실행되어야 패킷 손실 없이 수신 가능

3. 저장 구조 명확화
WriteToFileEvent 클래스에 데이터 길이 필드 확인 로직 포함

저장 시 상대 경로 지정 (경로 오류 방지)

4. 디버깅을 위한 구조 분리
송신, 수신, 저장 처리 클래스를 명확히 나눠 구조화

각 클래스는 독립적으로 테스트 가능

주요 파일
파일	설명
MulticastSender.java	byte 배열 데이터를 멀티캐스트 전송
MulticastReceiver.java	수신된 데이터로 WriteToFileEvent 생성
SaveBinaryData.java	실제 바이너리 데이터를 파일로 저장
WriteToFileEvent.java	저장 요청용 클래스 (파일명, 데이터 포함)

실행 방법
javac *.java
java MulticastReceiver  # 먼저 실행
java MulticastSender    # 나중에 실행
수신된 데이터는 output_받은시간.bin 형태로 저장됨.

주의사항
멀티캐스트는 같은 네트워크 대역 안에서만 정상 작동
실행 순서 (Receiver → Sender)
파일 저장 경로는 상대 경로로 현재 폴더 기준
자바 버전
x64 Installer	205.85 MB	
https://download.oracle.com/java/24/latest/jdk-24_windows-x64_bin.exe (sha256)
