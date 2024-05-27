-- 계정 생성
create user 'mbti_test'@'localhost' identified by '1234';
create user 'mbti_test'@'172.17.0.1' identified by '1234';

-- 권한 부여, 새로 로드
grant all privileges on *.* to 'mbti_test'@'localhost';
grant all privileges on *.* to 'mbti_test'@'172.17.0.1';
-- 도커 이슈 해결
GRANT ALL PRIVILEGES ON *.* TO 'mbti_test'@'localhost' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'mbti_test'@'172.17.0.1' WITH GRANT OPTION;
flush privileges;

-- 유저 삭제
DROP USER 'mbti_test'@'localhost';
DROP USER 'mbti_test'@'172.17.0.1';