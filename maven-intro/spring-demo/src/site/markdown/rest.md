REST api
=============================

Получение списка процессов
-----------------------

- Метод `GET`
- URL   `/api/linux/proc`
- Аунтификация: `BASIC`

**Пример запроса**

```
curl -u usr1:pswd1 -X GET http://localhost:8088/api/linux/proc
```

**Пример ответа**

```json
[
	{
		"id": "14472",
		"sessionid": "4294967295",
		"cmdline": [
			"/usr/lib/jvm/bellsoft-java14-full-amd64/bin/java",
			"-agentlib:jdwp=transport=dt_socket,address=127.0.0.1:47127,suspend=y,server=n",
			"-javaagent:/opt/apps/idea-ce/default/plugins/java/lib/rt/debugger-agent.jar",
			"-Dfile.encoding=UTF-8",
			"-classpath",
			"/home/user/code/samples/maven-intro/spring-demo/target/classes:/home/user/.m2/repository/org/springframework/boot/spring-boot-starter-web/2.4.1/spring-boot-starter-web-2.4.1.jar:/home/user/.m2/repository/org/springframework/boot/spring-boot-starter/2.4.1/spring-boot-starter-2.4.1.jar:/home/user/.m2/repository/org/springframework/boot/spring-boot/2.4.1/spring-boot-2.4.1.jar:/home/user/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.4.1/spring-boot-autoconfigure-2.4.1.jar:/home/user/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.4.1/spring-boot-starter-logging-2.4.1.jar:/home/user/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar:/home/user/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar:/home/user/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar:/home/user/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:/home/user/.m2/repository/org/slf4j/jul-to-slf4j/1.7.30/jul-to-slf4j-1.7.30.jar:/home/user/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/home/user/.m2/repository/org/yaml/snakeyaml/1.27/snakeyaml-1.27.jar:/home/user/.m2/repository/org/springframework/boot/spring-boot-starter-json/2.4.1/spring-boot-starter-json-2.4.1.jar:/home/user/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.11.3/jackson-databind-2.11.3.jar:/home/user/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.11.3/jackson-annotations-2.11.3.jar:/home/user/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.11.3/jackson-core-2.11.3.jar:/home/user/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jdk8/2.11.3/jackson-datatype-jdk8-2.11.3.jar:/home/user/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.11.3/jackson-datatype-jsr310-2.11.3.jar:/home/user/.m2/repository/com/fasterxml/jackson/module/jackson-module-parameter-names/2.11.3/jackson-module-parameter-names-2.11.3.jar:/home/user/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat/2.4.1/spring-boot-starter-tomcat-2.4.1.jar:/home/user/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/9.0.41/tomcat-embed-core-9.0.41.jar:/home/user/.m2/repository/org/glassfish/jakarta.el/3.0.3/jakarta.el-3.0.3.jar:/home/user/.m2/repository/org/apache/tomcat/embed/tomcat-embed-websocket/9.0.41/tomcat-embed-websocket-9.0.41.jar:/home/user/.m2/repository/org/springframework/spring-web/5.3.2/spring-web-5.3.2.jar:/home/user/.m2/repository/org/springframework/spring-beans/5.3.2/spring-beans-5.3.2.jar:/home/user/.m2/repository/org/springframework/spring-webmvc/5.3.2/spring-webmvc-5.3.2.jar:/home/user/.m2/repository/org/springframework/spring-context/5.3.2/spring-context-5.3.2.jar:/home/user/.m2/repository/org/springframework/spring-expression/5.3.2/spring-expression-5.3.2.jar:/home/user/.m2/repository/org/springframework/boot/spring-boot-starter-security/2.4.1/spring-boot-starter-security-2.4.1.jar:/home/user/.m2/repository/org/springframework/spring-aop/5.3.2/spring-aop-5.3.2.jar:/home/user/.m2/repository/org/springframework/security/spring-security-config/5.4.2/spring-security-config-5.4.2.jar:/home/user/.m2/repository/org/springframework/security/spring-security-core/5.4.2/spring-security-core-5.4.2.jar:/home/user/.m2/repository/org/springframework/security/spring-security-web/5.4.2/spring-security-web-5.4.2.jar:/home/user/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar:/home/user/.m2/repository/org/springframework/spring-core/5.3.2/spring-core-5.3.2.jar:/home/user/.m2/repository/org/springframework/spring-jcl/5.3.2/spring-jcl-5.3.2.jar:/home/user/.m2/repository/xyz/cofe/fs/1.1/fs-1.1.jar:/home/user/.m2/repository/xyz/cofe/cbuffer/1.1-SNAPSHOT/cbuffer-1.1-SNAPSHOT.jar:/home/user/.m2/repository/xyz/cofe/iofun/1.0/iofun-1.0.jar:/home/user/.m2/repository/xyz/cofe/ecolls/1.7/ecolls-1.7.jar:/opt/apps/idea-ce/idea-IC-201.8743.12/lib/idea_rt.jar",
			"org.example.springdemo.SpringDemoApplication"
		],
		"environ": [
			"PATH=/home/user/.cargo/bin:/home/linuxbrew/.linuxbrew/bin:/home/linuxbrew/.linuxbrew/sbin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin",
			"LC_MEASUREMENT=ru_RU.UTF-8",
			"XAUTHORITY=/home/user/.Xauthority",
			"LC_TELEPHONE=ru_RU.UTF-8",
			"XDG_DATA_DIRS=/usr/share/xfce4:/usr/share/xubuntu:/usr/local/share:/usr/share:/var/lib/snapd/desktop:/usr/share",
			"GDMSESSION=xubuntu",
			"MANDATORY_PATH=/usr/share/gconf/xubuntu.mandatory.path",
			"LC_TIME=ru_RU.UTF-8",
			"PAPERSIZE=a4",
			"DBUS_SESSION_BUS_ADDRESS=unix:path=/run/user/1000/bus",
			"DEFAULTS_PATH=/usr/share/gconf/xubuntu.default.path",
			"XDG_CURRENT_DESKTOP=XFCE",
			"SSH_AGENT_PID=2941",
			"CLUTTER_BACKEND=x11",
			"LC_PAPER=ru_RU.UTF-8",
			"SESSION_MANAGER=local/user-Modern-14-A10RB:@/tmp/.ICE-unix/2628,unix/user-Modern-14-A10RB:/tmp/.ICE-unix/2628",
			"LOGNAME=user",
			"PWD=/home/user/code/samples/maven-intro/spring-demo",
			"INFOPATH=/home/linuxbrew/.linuxbrew/share/info:",
			"LANGUAGE=ru:en",
			"SHELL=/bin/bash",
			"LC_ADDRESS=ru_RU.UTF-8",
			"GIO_LAUNCHED_DESKTOP_FILE=/home/user/.local/share/applications/jetbrains-idea-ce.desktop",
			"HOMEBREW_CELLAR=/home/linuxbrew/.linuxbrew/Cellar",
			"OLDPWD=/opt/apps/idea-ce/default/bin",
			"GTK_MODULES=gail:atk-bridge",
			"XDG_SESSION_PATH=/org/freedesktop/DisplayManager/Session0",
			"XDG_SESSION_DESKTOP=xubuntu",
			"SHLVL=0",
			"LC_IDENTIFICATION=ru_RU.UTF-8",
			"LC_MONETARY=ru_RU.UTF-8",
			"MANPATH=/home/linuxbrew/.linuxbrew/share/man:",
			"XDG_CONFIG_DIRS=/etc/xdg/xdg-xubuntu:/etc/xdg:/etc/xdg",
			"LANG=ru_RU.UTF-8",
			"XDG_SEAT_PATH=/org/freedesktop/DisplayManager/Seat0",
			"HOMEBREW_PREFIX=/home/linuxbrew/.linuxbrew",
			"XDG_SESSION_ID=c2",
			"XDG_SESSION_TYPE=x11",
			"DISPLAY=:0.0",
			"HOMEBREW_REPOSITORY=/home/linuxbrew/.linuxbrew/Homebrew",
			"LC_NAME=ru_RU.UTF-8",
			"XDG_SESSION_CLASS=user",
			"GDM_LANG=ru",
			"XDG_GREETER_DATA_DIR=/var/lib/lightdm-data/user",
			"GPG_AGENT_INFO=/run/user/1000/gnupg/S.gpg-agent:0:1",
			"DESKTOP_SESSION=xubuntu",
			"USER=user",
			"XDG_MENU_PREFIX=xfce-",
			"GIO_LAUNCHED_DESKTOP_FILE_PID=7598",
			"QT_ACCESSIBILITY=1",
			"LC_NUMERIC=ru_RU.UTF-8",
			"SSH_AUTH_SOCK=/run/user/1000/keyring/ssh",
			"XDG_SEAT=seat0",
			"GTK_OVERLAY_SCROLLING=0",
			"QT_QPA_PLATFORMTHEME=gtk2",
			"GSETTINGS_SCHEMA_DIR=/home/user/data",
			"XDG_VTNR=7",
			"XDG_RUNTIME_DIR=/run/user/1000",
			"HOME=/home/user"
		],
		"status": "Name:\tjava\nUmask:\t0002\nState:\tS (sleeping)\nTgid:\t14472\nNgid:\t0\nPid:\t14472\nPPid:\t7640\nTracerPid:\t0\nUid:\t1000\t1000\t1000\t1000\nGid:\t1000\t1000\t1000\t1000\nFDSize:\t1024\nGroups:\t4 24 27 30 46 120 131 132 135 1000 \nNStgid:\t14472\nNSpid:\t14472\nNSpgid:\t2628\nNSsid:\t2628\nVmPeak:\t 8889088 kB\nVmSize:\t 8884708 kB\nVmLck:\t       0 kB\nVmPin:\t       0 kB\nVmHWM:\t  300808 kB\nVmRSS:\t  287340 kB\nRssAnon:\t  259216 kB\nRssFile:\t   28124 kB\nRssShmem:\t       0 kB\nVmData:\t  425592 kB\nVmStk:\t     136 kB\nVmExe:\t       4 kB\nVmLib:\t   21620 kB\nVmPTE:\t    1132 kB\nVmSwap:\t       0 kB\nHugetlbPages:\t       0 kB\nCoreDumping:\t0\nTHP_enabled:\t1\nThreads:\t45\nSigQ:\t0/63191\nSigPnd:\t0000000000000000\nShdPnd:\t0000000000000000\nSigBlk:\t0000000000000004\nSigIgn:\t0000000000000000\nSigCgt:\t2000000181005ccf\nCapInh:\t0000000000000000\nCapPrm:\t0000000000000000\nCapEff:\t0000000000000000\nCapBnd:\t0000003fffffffff\nCapAmb:\t0000000000000000\nNoNewPrivs:\t0\nSeccomp:\t0\nSpeculation_Store_Bypass:\tthread vulnerable\nCpus_allowed:\tff\nCpus_allowed_list:\t0-7\nMems_allowed:\t00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000001\nMems_allowed_list:\t0\nvoluntary_ctxt_switches:\t1\nnonvoluntary_ctxt_switches:\t5\n",
		"exe": "/usr/lib/jvm/bellsoft-java14-full-amd64/bin/java"
	}
]
```