rm -rf target
mvn install
rm ~/.m2/repository/dev/mv/engine/0.5.3/engine-0.5.3.pom
cp installed.xml ~/.m2/repository/dev/mv/engine/0.5.3/engine-0.5.3.pom