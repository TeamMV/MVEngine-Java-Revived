rm -rf target
mvn install -P Linux,MacOS,"MacOS M1+",Windows
rm ~/.m2/repository/dev/mv/engine/0.5.3/engine-0.5.3.pom
cp installed.xml ~/.m2/repository/dev/mv/engine/0.5.3/engine-0.5.3.pom