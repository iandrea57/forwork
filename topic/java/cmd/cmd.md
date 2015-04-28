
#### javac

    javac -encoding utf8 JavaTest.java
    javac -source 1.6 -target 1.6 JavaTest.java


#### java

    java -Xms32M -Xmx32M -cp . HeapOOM

    -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./gclogs

#### javap
    javap -verbose TestClass.class

