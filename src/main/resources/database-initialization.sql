CREATE DATABASE css_java;
CREATE USER 'comp8117'@'%' IDENTIFIED BY 'qwe123';
GRANT ALL PRIVILEGES ON css_java.* TO 'comp8117'@'%';
FLUSH PRIVILEGES;