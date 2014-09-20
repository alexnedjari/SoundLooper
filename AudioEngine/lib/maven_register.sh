mvn deploy:deploy-file -Durl=file:~/.m2/repository -Dfile=NativeFmodEx.jar -DgroupId=nativefmod -DartifactId=NativeFmodEx -Dpackaging=jar -Dversion=1.5.0
mvn deploy:deploy-file -Durl=file:~/.m2/repository -Dfile=NativeFmodEx-linux.jar -DgroupId=nativefmod -DartifactId=NativeFmodEx-linux -Dpackaging=jar -Dversion=1.5.0
